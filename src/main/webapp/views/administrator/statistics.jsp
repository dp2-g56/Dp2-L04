<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
 
 

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

<p><spring:message code="administrator.statistics" /></p>




<spring:message code="statistics.curriculum" />	
<table style="width: 100%">
	<tr> 
		<td><b><spring:message code="statistics.average" /></b></td> 
		<td><jstl:out value="${statisticsCurriculum.get(0)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.minimum"/></b></td> 
		<td><jstl:out value="${statisticsCurriculum.get(1)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.maximum"/></b></td> 
		<td><jstl:out value="${statisticsCurriculum.get(2)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.standardDeviation"/></b></td> 
		<td><jstl:out value="${statisticsCurriculum.get(3)}" /> </td>
	</tr>
</table>
<br />


<spring:message code="statistics.finder" />	
<table style="width: 100%">
	<tr> 
		<td><b><spring:message code="statistics.average" /></b></td> 
		<td><jstl:out value="${statisticsFinder.get(0)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.minimum"/></b></td> 
		<td><jstl:out value="${statisticsFinder.get(1)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.maximum"/></b></td> 
		<td><jstl:out value="${statisticsFinder.get(2)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.standardDeviation"/></b></td> 
		<td><jstl:out value="${statisticsFinder.get(3)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.emptyVsNoEmpty"/></b></td> 
		<td><jstl:out value="${statisticsFinder.get(4)}" /> </td>
	</tr>
</table>
<br />

<spring:message code="statistics.statisticsPositionsCompany" />	
<table style="width: 100%">
	<tr> 
		<td><b><spring:message code="statistics.average" /></b></td> 
		<td><jstl:out value="${statisticsPositionsCompany.get(0)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.minimum"/></b></td> 
		<td><jstl:out value="${statisticsPositionsCompany.get(1)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.maximum"/></b></td> 
		<td><jstl:out value="${statisticsPositionsCompany.get(2)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.standardDeviation"/></b></td> 
		<td><jstl:out value="${statisticsPositionsCompany.get(3)}" /> </td>
	</tr>
</table>
<br />

<spring:message code="statistics.statisticsApplicationsRookie" />	
<table style="width: 100%">
	<tr> 
		<td><b><spring:message code="statistics.average" /></b></td> 
		<td><jstl:out value="${statisticsApplicationsRookie.get(0)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.minimum"/></b></td> 
		<td><jstl:out value="${statisticsApplicationsRookie.get(1)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.maximum"/></b></td> 
		<td><jstl:out value="${statisticsApplicationsRookie.get(2)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.standardDeviation"/></b></td> 
		<td><jstl:out value="${statisticsApplicationsRookie.get(3)}" /> </td>
	</tr>
</table>
<br />

<spring:message code="statistics.statisticsSalaries" />	
<table style="width: 100%">
	<tr> 
		<td><b><spring:message code="statistics.average" /></b></td> 
		<td><jstl:out value="${statisticsSalaries.get(0)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.minimum"/></b></td> 
		<td><jstl:out value="${statisticsSalaries.get(1)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.maximum"/></b></td> 
		<td><jstl:out value="${statisticsSalaries.get(2)}" /> </td>
	</tr>
	<tr>
		<td><b><spring:message code="statistics.standardDeviation"/></b></td> 
		<td><jstl:out value="${statisticsSalaries.get(3)}" /> </td>
	</tr>
</table>
<br />

<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.companiesMorePositions" />:</b></td>
		</tr>
		<jstl:forEach items="${companiesMorePositions}" var="company">
			<tr>
				<td><jstl:out value="${company.companyName}" /></td>
			</tr>
		</jstl:forEach>
</table>
<br />

<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.rookiesMoreApplications" />:</b></td>
		</tr>
		<jstl:forEach items="${rookiesMoreApplications}" var="rookie">
			<tr>
				<td><jstl:out value="${rookie.name } ${rookie.surname}" /></td>
			</tr>
		</jstl:forEach>
</table>
<br />

<table style="width: 100%">
		<tr>
			<td><b><spring:message code="statistics.bestPositionsSalary" />:</b></td>
		</tr>
		<jstl:forEach items="${bestPositionsSalary}" var="bSalary">
			<tr>
				<td><jstl:out value="${bSalary.title}" /></td>
			</tr>
		</jstl:forEach>
		<tr>
			<td><b><spring:message
						code="statistics.worstPositionsSalary" />:</b></td>
		</tr>
		<jstl:forEach items="${worstPositionsSalary}" var="wSalary">
			<tr>
				<td><jstl:out value="${wSalary.title}" /></td>
			</tr>
		</jstl:forEach>

</table>
<br />


</security:authorize>



