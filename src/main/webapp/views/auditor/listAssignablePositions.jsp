<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<display:table name="positions" id="row">

	<display:column property="title" titleKey="possition.title" />
	<display:column property="description" titleKey="possition.description" />
	<display:column property="deadline" titleKey="possition.deadline" />
	<display:column property="requiredProfile"
		titleKey="possition.requiredProfile" />
	<display:column property="requiredSkills"
		titleKey="possition.requiredSkills" />
	<display:column property="requiredTecnologies"
		titleKey="possition.requiredTecnologies" />
	<display:column property="offeredSalary"
		titleKey="possition.offeredSalary" />
	<display:column property="ticker" titleKey="possition.ticker" />

	<display:column titleKey="position.sponsorship">
		<jstl:if test="${randomSpo.get(row.id).id>0}">
			<a href="${randomSpo.get(row.id).targetUrl}"><img
				src="${randomSpo.get(row.id).banner}"
				style="width: auto; height: 50px;"
				alt="<spring:message code='position.sponsorship'/>" /></a>
		</jstl:if>
	</display:column>

	<display:column titleKey="possition.problems">
		<spring:url var="createUrl0"
			value="/anonymous/problem/list.do?positionId={positionId}">
			<spring:param name="positionId" value="${row.id}" />
			<spring:param name="assignable" value="${true}" />
		</spring:url>
		<a href="${createUrl0}"><spring:message code="annonymous.problems" /></a>
	</display:column>

	<display:column titleKey="position.applications">

		<spring:url var="applicationsUrl"
			value="/anonymous/application/list.do?positionId={positionId}">
			<spring:param name="positionId" value="${row.id}" />
			<spring:param name="assignable" value="${true}" />
		</spring:url>

		<a href="${applicationsUrl}"> <spring:message
				var="viewApplications1" code="position.viewApplications" /> <jstl:out
				value="${viewApplications1}" />
		</a>

	</display:column>

	<display:column titleKey="position.audits">

		<spring:url var="auditsUrl"
			value="/anonymous/audit/list.do?positionId={positionId}">
			<spring:param name="positionId" value="${row.id}" />
			<spring:param name="assignable" value="${true}" />
		</spring:url>

		<a href="${auditsUrl}"> <spring:message var="viewAudits"
				code="position.viewAudits" /> <jstl:out value="${viewAudits}" />
		</a>

	</display:column>


	<display:column titleKey="annonymous.companies">
		<spring:url var="createUrl1"
			value="/anonymous/company/listOne.do?positionId={positionId}">
			<spring:param name="positionId" value="${row.id}" />
			<spring:param name="assignable" value="${true}" />
		</spring:url>
		<a href="${createUrl1}"><spring:message code="annonymous.company" /></a>
	</display:column>

	<security:authorize access="hasRole('AUDITOR')">
		<display:column titleKey="position.assign">

			<button type="button"
				onclick="javascript: relativeRedir('audit/auditor/create.do?positionId='+${row.id})">
				<spring:message code="position.assign" />
			</button>

		</display:column>
	</security:authorize>




</display:table>
