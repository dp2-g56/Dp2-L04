<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

	
	<display:table name="providers" id="row" pagesize="5" class="displaytag" 
					requestURI="/anonymous/provider/list.do">
	
		<display:column property="make" titleKey="provider.make" sortable="true"/> 
		<display:column property="name" titleKey="companies.name" /> 
		<display:column property="VATNumber" titleKey="companies.VATNumber" sortable="true"/> 
		<display:column property="photo" titleKey="companies.photo" sortable="true"/> 
		<display:column property="email" titleKey="companies.email" sortable="true"/> 
		<display:column property="phone" titleKey="companies.phone" sortable="true"/> 
		<display:column property="address" titleKey="companies.address" sortable="true"/> 
		
		
		<display:column titleKey="provider.items">
    		
       		<spring:url var="itemsUrl" value="/anonymous/item/list.do?providerId={providerId}">
            	<spring:param name="providerId" value="${row.id}"/>
        	</spring:url>
        	
        	<a href="${itemsUrl}">
              <spring:message var ="viewItems" code="provider.viewItems" />
             <jstl:out value="${viewItems}" />   
        	</a>
        	
        </display:column>
	
	</display:table>
	

