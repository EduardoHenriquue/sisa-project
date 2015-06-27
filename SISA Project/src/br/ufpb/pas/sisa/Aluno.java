package br.ufpb.pas.sisa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="alunos")
public class Aluno {
	
	@Id
	private String matricula;
	private String nome;
	private String CRE;
	@OneToMany 
	private List<Disciplina> disciplinasMatriculadas;

	public Aluno(String matricula, String nome, String CRE) {
		this.matricula = matricula;
		this.nome = nome;
		this.CRE = CRE;
		this.disciplinasMatriculadas = new ArrayList<Disciplina>();
	}

	public String getMatricula() {
		return matricula;
	}

	public String getNome() {
		return nome;
	}

	public String getCRE() {
		return CRE;
	}

	public String getPeriodoEgresso() {
		// O período de egresso do aluno é o período da sua primeira disciplina matriculada
		return this.disciplinasMatriculadas.get(0).getPeriodo();
	}
	
	public void setDisciplinasMatriculadas(List<Disciplina> disciplinas) {
		this.disciplinasMatriculadas = disciplinas;
	}

	public List<Disciplina> getDisciplinasMatriculadas() {
		return disciplinasMatriculadas;
	}

}
