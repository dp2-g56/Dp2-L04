<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('AUDITOR')">

	<form:form modelAttribute="audit"
		action="audit/auditor/edit.do">
		<!--Hidden Attributes -->
		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="position"/>


		<acme:textarea code="audit.freeText" path="freeText" />
		<acme:boolean code="position.isDraftMode" trueCode="position.true" falseCode="position.false" path="isDraftMode"/>	
		<br />
		<acme:selectNumber max="10" min="0" code="audit.score" path="score"/>
		<acme:submit name="edit" code="rookie.edit" />


	</form:form>

	<acme:cancel url="/position/auditor/listAssignablePositions.do" code="rookie.cancel" />

</security:authorize>