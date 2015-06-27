<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SISA</title>
</head>
<body>
	<h2>SISA - Sistema de Apoio ao Reajuste de Matrícula</h2>
	<br>
	Bem-Vindo, ${aluno.nome}
	<br>
	Matrícula: ${aluno.matricula} 
	<br>
	Período de Egresso: ${aluno.periodoEgresso}
	<br>
	CRE: ${aluno.CRE}
	<br>
	<br>
	
	<h3>Dados das suas disciplinas matriculadas anteriormente</h3>
	
	<c:forEach var="disciplina" items="${aluno.disciplinasMatriculadas}" varStatus="i">
	
		Código: ${disciplina.codigo}
		<br>
		Nome: ${disciplina.nome}
		<br>
		Quantidade de créditos: ${disciplina.quantCreditos}
		<br>
		Carga horária: ${disciplina.cargaHoraria}
		<br>
		Período: ${disciplina.periodo}
		<br>
		Média: ${disciplina.media}
		<br>
		Situação: ${disciplina.situacao}
		<br>
		<br>

	</c:forEach>
</body>
</html>