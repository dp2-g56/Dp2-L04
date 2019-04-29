<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme"  tagdir="/WEB-INF/tags"%>

	<display:table pagesize="5" name="pictures" id="row" class="displaytag" 
					requestURI="anonymous/item/listPictures.do">
		
		<display:column titleKey="item.pictures">
			<jstl:out value="${row}"/>
		</display:column> 
	
	</display:table>
	
	<acme:cancel url="anonymous/item/list.do" code="provider.back" /> 
