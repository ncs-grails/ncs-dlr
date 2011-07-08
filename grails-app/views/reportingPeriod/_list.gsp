
<%@ page import="edu.umn.ncs.ReportingPeriod" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'reportingPeriod.label', default: 'ReportingPeriod')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'reportingPeriod.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="referenceInvoiceNumber" title="${message(code: 'reportingPeriod.referenceInvoiceNumber.label', default: 'Reference Invoice Number')}" />
                        
                            <g:sortableColumn property="periodDate" title="${message(code: 'reportingPeriod.periodDate.label', default: 'Period Date')}" />
                        
                            <g:sortableColumn property="preparedDate" title="${message(code: 'reportingPeriod.preparedDate.label', default: 'Prepared Date')}" />
                        
                            <g:sortableColumn property="completedReportDate" title="${message(code: 'reportingPeriod.completedReportDate.label', default: 'Completed Report Date')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${reportingPeriodInstanceList}" status="i" var="reportingPeriodInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${reportingPeriodInstance.id}">${fieldValue(bean: reportingPeriodInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: reportingPeriodInstance, field: "referenceInvoiceNumber")}</td>
                        
                            <td><g:formatDate date="${reportingPeriodInstance.periodDate}" /></td>
                        
                            <td><g:formatDate date="${reportingPeriodInstance.preparedDate}" /></td>
                        
                            <td><g:formatDate date="${reportingPeriodInstance.completedReportDate}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${reportingPeriodInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
