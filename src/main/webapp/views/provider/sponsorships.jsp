<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('PROVIDER')">
	
	<display:table pagesize="5" name="sponsorship" id="row" class="displaytag" 
					requestURI="/sponsorship/provider/list.do">
			
		<display:column property="creditCard.number" titleKey="sponsorship.creditCard" />
		
		<display:column property="position.title" titleKey="sponsorship.position.title" />
		
		<display:column property="banner" titleKey="sponsorship.banner" />
		
		<display:column property="targetUrl" titleKey="sponsorship.targetUrl" />
		
		<display:column>
			<spring:url var="deleteUrl" value="/sponsorship/provider/delete.do">
				<spring:param name="sponsorshipId" value="${row.id}"/>
			</spring:url>
			<spring:url var="editUrl" value="/sponsorship/provider/edit.do">
				<spring:param name="sponsorshipId" value="${row.id}"/>
			</spring:url>
			<a href="${editUrl}">
				<spring:message code="sponsorship.edit" var = "editMessage" />
				<jstl:out value="${editMessage}"/>
			</a> / 
			<a href="${deleteUrl}" onclick="return confirm('<spring:message code="sponsorship.delete.confirmation" />')">
				<spring:message code="sponsorship.delete" var = "deleteMessage" />
				<jstl:out value="${deleteMessage}"/>
			</a>
		</display:column>
		
	</display:table>
	
	<spring:url var="newSponsorshipUrl" value="/sponsorship/provider/create.do"/>
	<p><a href="${newSponsorshipUrl}">
		<spring:message code="sponsorship.create" var="createSponsorship" />
		<jstl:out value="${createSponsorship}"/>
	</a></p>
	
</security:authorize>
