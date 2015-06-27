package br.ufpb.pas.sisa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="disciplinas")
public class Disciplina {
	
	@Id
	private String codigo;
	private String nome;
	private String quantCreditos;
	private String cargaHoraria;
	private String periodo;
	private String media;
	private String situacao;
	
	public Disciplina(String codigo, String nome, String quantCreditos, String cargaHoraria, String periodo, String media, String situacao) {
		this.codigo = codigo;
		this.nome = nome;
		this.quantCreditos = quantCreditos;
		this.cargaHoraria = cargaHoraria;
		this.periodo = periodo;
		this.media = media;
		this.situacao = situacao;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getQuantCreditos() {
		return quantCreditos;
	}
	
	public void setQuantCreditos(String quantCréditos) {
		this.quantCreditos = quantCréditos;
	}
	
	public String getCargaHoraria() {
		return cargaHoraria;
	}
	
	public void setCargaHoraria(String cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	public String getPeriodo() {
		return periodo;
	}
	
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public String getMedia() {
		return media;
	}
	
	public void setMedia(String media) {
		this.media = media;
	}
	
	public String getSituacao() {
		return situacao;
	}
	
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

}
