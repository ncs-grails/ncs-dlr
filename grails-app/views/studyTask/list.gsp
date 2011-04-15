
<%@ page import="edu.umn.ncs.StudyTask" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'studyTask.label', default: 'StudyTask')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'studyTask.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'studyTask.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="obsolete" title="${message(code: 'studyTask.obsolete.label', default: 'Obsolete')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'studyTask.dateCreated.label', default: 'Date Created')}" />
                        
                            <g:sortableColumn property="userCreated" title="${message(code: 'studyTask.userCreated.label', default: 'User Created')}" />
                        
                            <g:sortableColumn property="appCreated" title="${message(code: 'studyTask.appCreated.label', default: 'App Created')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${studyTaskInstanceList}" status="i" var="studyTaskInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${studyTaskInstance.id}">${fieldValue(bean: studyTaskInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: studyTaskInstance, field: "name")}</td>
                        
                            <td><g:formatBoolean boolean="${studyTaskInstance.obsolete}" /></td>
                        
                            <td><g:formatDate date="${studyTaskInstance.dateCreated}" /></td>
                        
                            <td>${fieldValue(bean: studyTaskInstance, field: "userCreated")}</td>
                        
                            <td>${fieldValue(bean: studyTaskInstance, field: "appCreated")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${studyTaskInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
