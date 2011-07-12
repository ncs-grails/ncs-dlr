<%@ page import="edu.umn.ncs.ReportingStaff" %>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="layout" content="ncs" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
        <g:set var="entityName" value="${message(code: 'reportingStaff.label', default: 'ReportingStaff')}" />
		<title>DLR Reporting Staff</title>		
    </head>
    
    <body>
    
		<!-- NAVIGATION -->
		<p class="breadcrumbs">
			<g:link controller="main" action="index">DLR Home</g:link>
				&nbsp;&nbsp;&gt&nbsp;&nbsp;
			<g:link controller="applicationManagement" action="index">Application Management</g:link>
				&nbsp;&nbsp;&gt&nbsp;&nbsp;
			<span class="fontMaroon">Reporting Staff</span>
		</p>

		<h1>Reporting Staff</h1>

		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		
		<table>
	    
			<thead>
			    <tr>
					<g:sortableColumn class="basic" property="id" title="${message(code: 'reportingStaff.id.label', default: 'Id')}" />						
					<g:sortableColumn class="basic" property="lastName" title="${message(code: 'reportingStaff.lastName.label', default: 'Last Name')}" />
					<g:sortableColumn class="basic" property="firstName" title="${message(code: 'reportingStaff.firstName.label', default: 'First Name')}" />
					<g:sortableColumn class="basic" property="middleInit" title="${message(code: 'reportingStaff.middleInit.label', default: 'Middle Init')}" />					
					<g:sortableColumn class="basic" property="laborCategory" title="${message(code: 'reportingStaff.laborCategory.label', default: 'Labor Category')}" />
					<g:sortableColumn class="basic" property="email" title="${message(code: 'reportingStaff.email.label', default: 'Email')}" />
					<g:sortableColumn class="basic" property="reportsEffort" title="${message(code: 'reportingStaff.reportsEffort.label', default: 'Reports Effort')}" />
					<g:sortableColumn class="basic" property="isTestAccount" title="${message(code: 'reportingStaff.reportsEffort.label', default: 'Test Account')}" />
					<g:sortableColumn class="basic" property="dateCreated" title="${message(code: 'reportingStaff.dateCreated.label', default: 'Created')}" />			    
			    </tr>
			</thead>
			
	        <tbody>
		        <g:each in="${reportingStaffInstanceList}" status="i" var="reportingStaffInstance">
		            <tr>
		                <td class="basic" style="text-align:right;">
		                	<g:link action="edit" id="${reportingStaffInstance.id}">${fieldValue(bean: reportingStaffInstance, field: "id")}</g:link>
		                </td>
		                <td class="basic">${fieldValue(bean: reportingStaffInstance, field: "lastName")}</td>
		                <td class="basic">${fieldValue(bean: reportingStaffInstance, field: "firstName")}</td>
		                <td class="basic">${fieldValue(bean: reportingStaffInstance, field: "middleInit")}</td>
		                <td class="basic">${fieldValue(bean: reportingStaffInstance, field: "laborCategory")}</td>
		                <td class="basic">${fieldValue(bean: reportingStaffInstance, field: "email")}</td>
		                <td class="basic">${fieldValue(bean: reportingStaffInstance, field: "reportsEffort")}</td>
		                <td class="basic">${fieldValue(bean: reportingStaffInstance, field: "isTestAccount")}</td>
		                <td class="basic">
		                	<g:formatDate date="${fieldValue(bean: reportingStaffInstance, field: "dateCreated")}" format="MM-dd-yyyy" />&nbsp;(${fieldValue(bean: reportingStaffInstance, field: "userCreated")})
		                </td>
		            </tr>
		        </g:each>		        
	        </tbody>

	    </table>
	    <span class="tableFooterNote">Total Reporting Staff: ${reportingStaffInstanceTotal}</span>
	    
	    <div class="pageSpacing"> </div>

	</body>

</html>
