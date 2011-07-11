
<%@ page import="edu.umn.ncs.ReportingStaff" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'reportingStaff.label', default: 'ReportingStaff')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'reportingStaff.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="username" title="${message(code: 'reportingStaff.username.label', default: 'Username')}" />
                        
                            <g:sortableColumn property="firstName" title="${message(code: 'reportingStaff.firstName.label', default: 'First Name')}" />
                        
                            <g:sortableColumn property="middleInit" title="${message(code: 'reportingStaff.middleInit.label', default: 'Middle Init')}" />
                        
                            <g:sortableColumn property="lastName" title="${message(code: 'reportingStaff.lastName.label', default: 'Last Name')}" />
                        
                            <th><g:message code="reportingStaff.laborCategory.label" default="Labor Category" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${reportingStaffInstanceList}" status="i" var="reportingStaffInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${reportingStaffInstance.id}">${fieldValue(bean: reportingStaffInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: reportingStaffInstance, field: "username")}</td>
                        
                            <td>${fieldValue(bean: reportingStaffInstance, field: "firstName")}</td>
                        
                            <td>${fieldValue(bean: reportingStaffInstance, field: "middleInit")}</td>
                        
                            <td>${fieldValue(bean: reportingStaffInstance, field: "lastName")}</td>
                        
                            <td>${fieldValue(bean: reportingStaffInstance, field: "laborCategory")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${reportingStaffInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
