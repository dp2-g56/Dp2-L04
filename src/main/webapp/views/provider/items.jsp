<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('PROVIDER')">
	
	<display:table pagesize="5" name="items" id="row" class="displaytag" 
					requestURI="/item/provider/list.do">
			
		<display:column property="name" titleKey="item.name" />
		
		<display:column property="description" titleKey="item.description" />
		
		<display:column titleKey="item.links">
			<spring:url var="links" value="/item/provider/listLinks.do">
				<spring:param name="itemId" value="${row.id}"/>
			</spring:url>
			<a href="${links}">
				<spring:message code="item.links.show" var ="show" />
				<jstl:out value="${show} (${row.links.size()})"/>
			</a>
		</display:column>
		
		<display:column titleKey="item.pictures">
			<spring:url var="pictures" value="/item/provider/listPictures.do">
				<spring:param name="itemId" value="${row.id}"/>
			</spring:url>
			<a href="${links}">
				<spring:message code="item.pictures.show" var ="show" />
				<jstl:out value="${show} (${row.pictures.size()})"/>
			</a>
		</display:column>
		
	</display:table>
	
	<spring:url var="newItemUrl" value="/item/provider/new.do"/>
	<p><a href="${newItemUrl}">
		<spring:message code="item.new" var="newItem" />
		<jstl:out value="${newItem}"/>
	</a></p>
	
</security:authorize>
