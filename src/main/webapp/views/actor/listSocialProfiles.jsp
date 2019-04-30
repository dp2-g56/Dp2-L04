<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<br/>
	<!-- Actor Data -->
	<table>
		<tr>
			<td><spring:message code="actor.fullName" /></td>
			<td><jstl:out
					value="${actor.name} ${actor.surname}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.VATNumber" /></td>
			<td><jstl:out value="${actor.VATNumber}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.photo" /></td>
			<td><jstl:out value="${actor.photo}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.email" /></td>
			<td><jstl:out value="${actor.email}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.phone" /></td>
			<td><jstl:out value="${actor.phone}" /></td>
		</tr>

		<tr>
			<td><spring:message code="actor.address" /></td>
			<td><jstl:out value="${actor.address}" /></td>
		</tr>
		
		<jstl:if test="${score}">
		<tr>
			<td><spring:message code="actor.score" /></td>
			<td><jstl:out value="${actor.score}" /></td>
		</tr>
	</jstl:if> 
	</table>
	
	<!-- Export Data -->

	<security:authorize access="hasAnyRole('ADMIN')">
	<jstl:if test="${sameActorLogged || trueValue}">
		<acme:cancel url="/export/admin.do?id=${actor.id}"
			code="export" />
	</jstl:if> 
	</security:authorize>

	<security:authorize access="hasAnyRole('ROOKIE')">
	<jstl:if test="${sameActorLogged || trueValue}">
		<acme:cancel url="/export/rookie.do?id=${actor.id}"
			code="export" />
	</jstl:if> 
	</security:authorize>

	<security:authorize access="hasAnyRole('COMPANY')">
	<jstl:if test="${sameActorLogged || trueValue}">
		<acme:cancel url="/export/company.do?id=${actor.id}"
			code="export" />
	</jstl:if> 	
	</security:authorize>
	
	<security:authorize access="hasAnyRole('AUDITOR')">
	<jstl:if test="${sameActorLogged || trueValue}">
		<acme:cancel url="/export/auditor.do?id=${actor.id}"
			code="export" />
	</jstl:if> 	
	</security:authorize>
	
	<security:authorize access="hasAnyRole('PROVIDER')">
	<jstl:if test="${sameActorLogged || trueValue}">
		<acme:cancel url="/export/provider.do?id=${actor.id}"
			code="export" />
	</jstl:if> 	
	</security:authorize>


	<!-- Social Profiles -->
	<h2>
		<spring:message code="socialProfile.mySocialProfiles" />
	</h2>

	<display:table pagesize="5" name="socialProfiles" id="socialProfile"
		requestURI="${requestURI}">

		<display:column property="nick" titleKey="socialProfile.nick" />

		<display:column property="name" titleKey="socialProfile.name" />
	

		<display:column property="profileLink"
			titleKey="socialProfile.profileLink" />
			
			
	<security:authorize access="isAuthenticated()">		
		
		<jstl:if test="${trueValue}">
 	
		<display:column>

			<a
				href="authenticated/socialProfile/edit.do?socialProfileId=${socialProfile.id}">
				<spring:message code="socialProfile.edit" />
			</a>

		</display:column>
		
		</jstl:if> 	
		
	</security:authorize>		
		
	</display:table>
	
	<security:authorize access="isAuthenticated()">		
	<jstl:if test="${trueValue}">

	<a href="authenticated/socialProfile/create.do"><spring:message
			code="socialProfile.create" /></a>
	</jstl:if> 	
	</security:authorize>	
	<br/>
  
  <br/>
  
  	<!-- Provider's Items -->
	
	<jstl:if test="${itemValues}">
	
	<display:table pagesize="5" name="items" id="row" class="displaytag" >
			
		<display:column property="name" titleKey="item.name" />
		
		<display:column property="description" titleKey="item.description" />
		
		<display:column titleKey="item.links">
			<spring:url var="links" value="/anonymous/item/listLinks.do">
				<spring:param name="itemId" value="${row.id}"/>
			</spring:url>
			<a href="${links}">
				<spring:message code="item.links.show" var ="show" />
				<jstl:out value="${show} (${row.links.size()})"/>
			</a>
		</display:column>
		
		<display:column titleKey="item.pictures">
			<spring:url var="pictures" value="/anonymous/item/listPictures.do">
				<spring:param name="itemId" value="${row.id}"/>
			</spring:url>
			<a href="${pictures}">
				<spring:message code="item.pictures.show" var ="show" />
				<jstl:out value="${show} (${row.pictures.size()})"/>
			</a>
		</display:column>
	
	</display:table>
	
	  <a href="anonymous/item/list.do"><spring:message code="position.backToPublicData" /></a>
	
	
	</jstl:if>
	
	
	<!-- Backs -->
  
  <jstl:if test="${publicValue && !assignable}">
  <a href="anonymous/position/list.do"><spring:message code="position.backToPublicData" /></a>
  </jstl:if> 	
  
  
  <security:authorize access="hasAnyRole('AUDITOR')">
    <jstl:if test="${assignable}">
  <a href="position/auditor/listAssignablePositions.do"><spring:message code="position.backToAssignablePositions" /></a>
  </jstl:if> 	
  
  </security:authorize>
  

