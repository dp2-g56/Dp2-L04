<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="permitAll">


	<form name="word" id="word" action="anonymous/filtered/positions.do" method="post" >
	
	<spring:message code="filter.word" /> <input type="text" name="word" value="${word}" required><br>
	
	<input type="submit" name="save" value="<spring:message code="anonymous.create" />" />
	</form>
	
	<acme:cancel url="/anonymous/position/list.do"
			code="annonymous.listAll" />
	
		
	<display:table pagesize="5" name="publicPositions" id="row" requestURI="${requestURI}" >
			
		<display:column property="title" titleKey="possition.title" /> 
		<display:column property="description" titleKey="possition.description" /> 
		<display:column property="deadline" titleKey="possition.deadline" /> 
		<display:column property="requiredProfile" titleKey="possition.requiredProfile" /> 
		<display:column property="requiredSkills" titleKey="possition.requiredSkills" /> 
		<display:column property="requiredTecnologies" titleKey="possition.requiredTecnologies" /> 
		<display:column property="offeredSalary" titleKey="possition.offeredSalary" /> 
		<display:column property="ticker" titleKey="possition.ticker" /> 
		
		
		<display:column  titleKey="possition.problems" > 
			<spring:url var="createUrl0"
				value="/anonymous/problem/list.do?positionId={positionId}">
				<spring:param name="positionId" value="${row.id}" />
				<spring:param name="assignable" value="${false}" />
			</spring:url>
			<a href="${createUrl0}"><spring:message
				code="annonymous.problems" /></a>
		</display:column>
		
		 <display:column titleKey="position.applications">
    

    		
    		
       		<spring:url var="applicationsUrl" value="/anonymous/application/list.do?positionId={positionId}">
            	<spring:param name="positionId" value="${row.id}"/>
            	<spring:param name="assignable" value="${false}" />
        	</spring:url>
        	
        	<a href="${applicationsUrl}">
              <spring:message var ="viewApplications1" code="position.viewApplications" />
             <jstl:out value="${viewApplications1}" />   
        	</a>
        	
        </display:column>
  
		
		<display:column  titleKey="annonymous.companies" > 
			<spring:url var="createUrl1"
				value="/anonymous/company/listOne.do?positionId={positionId}">
				<spring:param name="positionId" value="${row.id}" />
				<spring:param name="assignable" value="${false}" />
			</spring:url>
			<a href="${createUrl1}"><spring:message
				code="annonymous.company" /></a>
		</display:column>
		
		
		<!-- AUDITOR -->
		<display:column titleKey="position.audits">

       		<spring:url var="auditsUrl" value="/anonymous/audit/list.do?positionId={positionId}">
            	<spring:param name="positionId" value="${row.id}"/>
            	<spring:param name="assignable" value="${false}" />
        	</spring:url>
        	
        	<a href="${auditsUrl}">
              <spring:message var ="viewAudits" code="position.viewAudits" />
             <jstl:out value="${viewAudits}" />   
        	</a>
        	
        </display:column>
		
		<security:authorize access="hasRole('AUDITOR')">
			<display:column>
			
				<spring:url var="createAuditUrl" value="/audit/auditor/create.do?positionId={positionId}">
            	<spring:param name="positionId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${createAuditUrl}">
              <spring:message var ="createAudit" code="position.createAudit" />
             <jstl:out value="${createAudit}" />   
        	</a>
				
			</display:column>
		</security:authorize>
	
	</display:table>
	
</security:authorize>
