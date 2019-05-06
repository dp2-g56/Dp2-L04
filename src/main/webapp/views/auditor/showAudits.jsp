<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<p><spring:message code="position.audits.list" /></p>

	<display:table pagesize="5" name="audits" id="row" requestURI="${requestURI}" >
	
	<display:column titleKey="position.status" >
		<jstl:out value="${row.position.title}"></jstl:out>
	</display:column>
	<jstl:if test="${locale == 'EN' }">
		<display:column property="momentCreation" titleKey="audit.momentCreation" />
	</jstl:if>
	<display:column property="freeText" titleKey="audit.freeText" />	
	
	<display:column titleKey="audit.score">
		<jstl:choose>
			<jstl:when test="${row.score == 10.0}">
				<jstl:out value="${68509}"></jstl:out>
			</jstl:when><jstl:otherwise>
				<jstl:out value="${row.score}"></jstl:out>
			</jstl:otherwise>
		</jstl:choose>		
	</display:column>
	
	<display:column titleKey="position.status">
        <jstl:choose>
        	<jstl:when test="${row.isDraftMode}">
        		<spring:message code="position.draft" />
        	</jstl:when>
        	
        	<jstl:otherwise>
        		<spring:message code="position.final" />
        	</jstl:otherwise>
        	
        </jstl:choose>
    </display:column>
    
    
    <display:column>
		<jstl:if test="${row.isDraftMode}">
			<a href="audit/auditor/edit.do?auditId=${row.id}">
				<spring:message code="position.edit" />
			</a>
		</jstl:if>

	</display:column>
	
	</display:table>
<br />



	
