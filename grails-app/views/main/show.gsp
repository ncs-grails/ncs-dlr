<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS Direct Labor Reporting</title>
  </head>

  <body>

    <g:form name="directLaborReporting" method="post" controller="main">
      
      <!-- FIELDSET -->
      <fieldset class="roundRectSilver">					
        
        <!-- LEGEND -->
        <legend class="legendMaroon"><g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></legend>
                
        <!-- if there are ASSIGNED effort that has not been committed yet, user must complete dlr -->
        <g:if test="${assignedEffortInstance && !committedDateInstance}">
          
          <!-- display parameters
          period: ${reportingPeriodInstance}
            &nbsp;&nbsp;&nbsp;
          staff: ${reportingStaffInstance}
            &nbsp;&nbsp;&nbsp;
          assigned effort: ${assignedEffortInstance}
            &nbsp;&nbsp;&nbsp;
          committed date: ${committedDateInstance}
          -->          

          <g:hiddenField name="period_id" value="${reportingPeriodInstance.id}" />
          <g:hiddenField name="staff_id" value="${reportingStaffInstance.id}" />
          <g:hiddenField name="assigned_effort_id" value="${assignedEffortInstance.id}" />

          <div class="clearCenterPadding">
            <span class="messageBoxOrange">Assigned: <g:formatNumber number="${assignedEffortInstance.assignedEffort}" type="percent" maxFractionDigits="2"/></span>          
            &nbsp;&nbsp;&nbsp;
            <span class="messageBoxOrange">Reported: <g:formatNumber number="${assignedEffortInstance.assignedEffort}" type="percent" maxFractionDigits="2"/></span>
            &nbsp;&nbsp;&nbsp;
            <span class="messageBoxRed">Committed: 
              <g:if test="${!committedDateInstance}">0%</g:if>
            </span>
          </div>

          <p class="clearCenterPadding">
            <div class="messageBoxPowderblue">Please submit your effort for this month.</div>
          </p>

          <div class="clearCenterPadding">
            <g:actionSubmit class="buttonBasic" value="Add" action="add"/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <g:actionSubmit class="buttonBasic" value="Delete" action="delete"/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <g:actionSubmit class="buttonBasic" value="Edit" action="edit"/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <g:actionSubmit class="buttonBasic" value="Commit" action="commit"/>
          </div>
                  
        </g:if>        
        
      </fieldset>
      
      <!-- TODO: display previous effort reported -->
      <h2>Past COMMITTED Effort Reported</h2>

    </g:form>      

  </body>
  
</html>
