
<%@ page import="edu.umn.ncs.ReportingPeriod" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'reportingPeriod.label', default: 'ReportingPeriod')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="reportingPeriod.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: reportingPeriodInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="reportingPeriod.referenceInvoiceNumber.label" default="Reference Invoice Number" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: reportingPeriodInstance, field: "referenceInvoiceNumber")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="reportingPeriod.periodDate.label" default="Period Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${reportingPeriodInstance?.periodDate}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="reportingPeriod.preparedDate.label" default="Prepared Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${reportingPeriodInstance?.preparedDate}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="reportingPeriod.completedReportDate.label" default="Completed Report Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${reportingPeriodInstance?.completedReportDate}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="reportingPeriod.assignedEfforts.label" default="Assigned Efforts" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${reportingPeriodInstance.assignedEfforts}" var="a">
                                    <li><g:link controller="assignedEffort" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${reportingPeriodInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
