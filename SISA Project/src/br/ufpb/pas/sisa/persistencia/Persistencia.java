package br.ufpb.pas.sisa.persistencia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.ufpb.pas.sisa.Aluno;
import br.ufpb.pas.sisa.Disciplina;

public class Persistencia {
	
	private static Persistencia singleton;
	
	private Persistencia() { }
	
	public static Persistencia getInstance() {
		if(singleton == null) {
			singleton = new Persistencia();
		}
		return singleton;
	}
	
	public void insereAluno(Aluno aluno) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("sisaBD");
		EntityManager manager = factory.createEntityManager();
					
		manager.getTransaction().begin();
		manager.persist(aluno);
		manager.getTransaction().commit();

		manager.close();
	}
	
	public void insereDisciplina(Disciplina disciplina) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("sisaBD");
		EntityManager manager = factory.createEntityManager();
		
		manager.getTransaction().begin();
		manager.persist(disciplina);
		manager.getTransaction().commit();

		manager.close();
	}

}


