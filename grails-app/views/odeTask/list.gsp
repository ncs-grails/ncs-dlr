
<%@ page import="edu.umn.ncs.OdeTask" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'odeTask.label', default: 'OdeTask')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'odeTask.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'odeTask.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="obsolete" title="${message(code: 'odeTask.obsolete.label', default: 'Obsolete')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'odeTask.dateCreated.label', default: 'Date Created')}" />
                        
                            <g:sortableColumn property="userCreated" title="${message(code: 'odeTask.userCreated.label', default: 'User Created')}" />
                        
                            <g:sortableColumn property="appCreated" title="${message(code: 'odeTask.appCreated.label', default: 'App Created')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${odeTaskInstanceList}" status="i" var="odeTaskInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${odeTaskInstance.id}">${fieldValue(bean: odeTaskInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: odeTaskInstance, field: "name")}</td>
                        
                            <td><g:formatBoolean boolean="${odeTaskInstance.obsolete}" /></td>
                        
                            <td><g:formatDate date="${odeTaskInstance.dateCreated}" /></td>
                        
                            <td>${fieldValue(bean: odeTaskInstance, field: "userCreated")}</td>
                        
                            <td>${fieldValue(bean: odeTaskInstance, field: "appCreated")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${odeTaskInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
