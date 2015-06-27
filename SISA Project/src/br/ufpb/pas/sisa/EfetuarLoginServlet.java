package br.ufpb.pas.sisa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import br.ufpb.pas.sisa.persistencia.Persistencia;

@WebServlet("/login")
public class EfetuarLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Map<String, String> dados;
	private static List<Disciplina> disciplinasMatriculadas;
	private static Aluno aluno;
       
    public EfetuarLoginServlet() {
        super();
        dados = new HashMap<String, String>();
        disciplinasMatriculadas = new ArrayList<Disciplina>();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Random random = new Random();
		
		// Define os par�metros que ser�o enviados na requisi��o de conex�o
		dados.put("matricula", request.getParameter("matricula"));
		dados.put("senha", request.getParameter("senha"));
		dados.put("x", Integer.toString(random.nextInt(30))); // Define um n�mero aleat�rio para esse par�metro
		dados.put("y", Integer.toString(random.nextInt(30))); // Define um n�mero aleat�rio para esse par�metro
		
		try {
			// Abre conex�o com a p�gina de login do Aluno Online
			Connection connection = Jsoup.connect("https://www.ufpb.br/AutoServico/AuthDiscente");
			// Define qual ser� o m�todo (POST) usado para efetuar a conex�o
			connection.method(Connection.Method.POST);
			// Define os dados que dever�o ser enviados na requisi��o 
			connection.data(dados);
			// Executa a conex�o
			Connection.Response resposta = connection.execute();
			
			acessarHistorico(resposta.cookies().get("JSESSIONID"), resposta.url().toString());
			
			request.setAttribute("aluno", aluno);
            request.getRequestDispatcher("ExibirDados.jsp").forward(request, response);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Acessa o hist�rico do aluno para obten��o dos seus dados
	 * @param sessao - sess�o gerada na conex�o com o sistema
	 * @param url - URL gerada na conex�o com o sistema
	 */
	public static void acessarHistorico(String sessao, String url) {
		// Limpa o mapa para novo uso
		dados.clear();
		
		// getCtrl(url) - Obt�m o controle da conex�o contido na URL da resposta
		dados.put("ctrl", getCtrl(url));
		dados.put("sess", sessao);
		// A op��es 6 � a de acesso ao hist�rico do aluno
		dados.put("opcao", "6");
		
		try {
			// Abre conex�o com a p�gina da �rea de trabalho do aluno no site do Aluno Online
			Connection connection = Jsoup.connect(url);
			// Define qual ser� o m�todo (POST) usado para efetuar a conex�o
			connection.method(Connection.Method.POST);
			// Define o cookie de sess�o da conex�o
			connection.cookie("JSESSIONID", sessao);
			// Define os dados que dever�o ser enviados na requisi��o 
			connection.data(dados);
			// Executa a conex�o e pega o texto do hist�rico do aluno
			String txtHistorico = connection.execute().parse().getElementsByTag("pre").text();
			
			// L� os dados referentes as disciplinas do aluno
			lerDadosDasDisciplinas(txtHistorico);
			// L� os dados referentes ao aluno
			lerDadosDoAluno(txtHistorico);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Obt�m apenas o n�mero de controle da conex�o do site definido na URL de resposta
	 * @param url - URl da p�gina de resposta
	 * @return o valor do controle contido na URL de resposta
	 */
	private static String getCtrl(String url) {
		
		StringTokenizer token = new StringTokenizer(url,"?");
		token.nextToken();
		return token.nextToken();
	}
	
	public static void lerDadosDasDisciplinas(String txtHistorico) {
		StringTokenizer token = new StringTokenizer(txtHistorico,"=");
		Disciplina disciplina;
		token.nextToken();
		token.nextToken();
		Scanner leitor = new Scanner(token.nextToken()); // Ser� lido a string com os dados de todas as disciplinas matriculadas pelo aluno
		
		String codigo, media, situacao, cargaHoraria, periodo, nomeDisciplina = ""; 
		int quantCreditos = 0;
		boolean lancou = true;

		while(leitor.hasNextLine()) {
			
			codigo = leitor.next();
			
			// O la�o ir� parar quando for lido a quantidade de cr�ditos da disciplina
			while(lancou) {
				try{
					
					quantCreditos = leitor.nextInt();
					lancou = false;
					
				} catch(Exception e) {
					nomeDisciplina += leitor.next()+" ";
					
				}
			}
			
			cargaHoraria = leitor.next();
			periodo = leitor.next()+"."+leitor.next();
			media = leitor.next();
			
			// Verifica se a disciplina em quest�o est� sendo cursada atualmente
			if(media.equals("-----")) {
				break;
			}
			
			situacao = leitor.next();
			
			disciplina = new Disciplina(codigo, nomeDisciplina, Integer.toString(quantCreditos), cargaHoraria, periodo, media, situacao);
			
			// Insere a disciplina no banco de dados
			Persistencia.getInstance().insereDisciplina(disciplina);
			
			/*
			System.out.println("C�digo disciplina: "+disciplina.getCodigo());
			System.out.println("Nome disciplina: "+disciplina.getNome());
			System.out.println("Quantidade de cr�ditos: "+disciplina.getQuantCreditos());
			System.out.println("Carga Hor�ria: "+disciplina.getCargaHoraria());
			System.out.println("Per�odo: "+disciplina.getPeriodo());			
			System.out.println("M�dia: "+disciplina.getMedia());
			System.out.println("Situa��o: "+disciplina.getSituacao()+"\n");
			*/
			
			disciplinasMatriculadas.add(disciplina);

			nomeDisciplina = "";
			lancou = true;
		}
		
		leitor.close();
	}
	
	private static void lerDadosDoAluno(String txtHistorico) {
		Scanner leitor = new Scanner(txtHistorico);
		leitor.nextLine();
		leitor.nextLine();
		String txt = leitor.nextLine();
		
		Aluno a = new Aluno(getMatriculaAluno(txt), getNomeAluno(txt), getCRE(txtHistorico));
		a.setDisciplinasMatriculadas(disciplinasMatriculadas);
		
		aluno = a;
		
		// Insere o aluno no banco de dados
		Persistencia.getInstance().insereAluno(aluno);
		
		/*
		System.out.println("Matr�cula Aluno", aluno.getMatricula());
		System.out.println("Nome Aluno: "+aluno.getNome());
		System.out.println("CRE Aluno: "+aluno.getCRE());
		System.out.println("Per�odo Egresso Aluno: "+aluno.getPeriodoEgresso());
		*/
		
		leitor.close();
	}
	
	private static String getMatriculaAluno(String txt) {
		StringTokenizer token = new StringTokenizer(txt," ");
		
		token.nextToken();
		token.nextToken();
		
		StringTokenizer token2 = new StringTokenizer(token.nextToken(),"--");
		
		return token2.nextToken();
	
	}

	private static String getNomeAluno(String txt) {
		StringTokenizer token = new StringTokenizer(txt,"--");
		token.nextToken();
		
		return token.nextToken();
	}

	private static String getCRE(String txtHistorico) {
		
		Scanner leitor = new Scanner(txtHistorico);
		leitor.useDelimiter("\\s*CRE:\\s*");
		leitor.next();
		
		StringTokenizer token = new StringTokenizer(leitor.next(), "F");

		leitor.close();
		
		return token.nextToken();
	}
	
}
