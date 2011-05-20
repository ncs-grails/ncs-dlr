sqv<%@ page contentType="text/html;charset=UTF-8" %>
<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS Direct Labor Reporting</title>
  </head>

  <body>

    <g:form name="dlr" method="post" controller="main">
      
      <p class="breadcrumbs">
        <g:link controller="applicationManagement" action="index">Application Management</g:link>
      </p>
    
      <h1>Direct Labor Reporting</h1>

      <p class="fontMaroon">
        Welcome, <strong>${reportingStaffInstance.firstName} ${reportingStaffInstance.lastName}!</strong>
      </p>

      <g:if test="${!assignedEffortInstance}">
        <!-- if there is no assigned effort for this period, then user does not need to complete dlr -->
        <p>
            You do not have to report your effort for <strong>
            <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>.
        </p>              
      </g:if>
      
      <g:elseif test="${assignedEffortInstance && committedDateInstance}">
        <!-- if there is a COMMITTED DATE for this period, then user is done with effort reporting -->
        <p>
          You have completed your effort reporting for this 
          <strong><g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>. Thank you!
        </p>              
      </g:elseif>
      
      <g:else>
          <!-- otherwise, display section to complete effort -->
          <g:include 
            controller="main"
            action="show"
            params="[
              'period_id':reportingPeriodInstance.id,
              'staff_id':reportingStaffInstance.id,
              'assigned_effort_id': assignedEffortInstance.id
            ]"
          />              
      </g:else>        

    </g:form>

    <div class="spacing"> </div>
    
  </body>

</html>
