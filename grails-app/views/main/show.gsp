<%@ page contentType="text/html;charset=UTF-8" %>
<html>  
  
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
    <title>NCS Direct Labor Reporting</title>
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css', file:'custom.css')}" />
    <g:javascript src="dlr.js" />
  </head>

  <body>

      <!-- navigation -->
      <p class="breadcrumbs">
        <g:link controller="applicationManagement" action="index">Application Management</g:link>
      </p>
    
      <!-- page title -->
      <h1>Direct Labor Reporting</h1>

      <!-- Greeting -->
      <span class="fontMaroon">
        Welcome, <strong>${reportingStaffInstance.firstName} ${reportingStaffInstance.lastName}</strong>!
      </span>

      <!-- CURRENT EFFORT REPORTING SECTION -->
      <h2>
        <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /> Effort Reporting
      </h2>

        <!-- DISPLAY MESSAGE/SECTIONS BASED ON ASSIGNED AND COMMITTED EFFORT -->

        <!-- if there is NO assigned effort for this period, let user know -->
        <g:if test="${!assignedEffortInstance}">
          <p>You have not been assigned effort to report for <strong>
            <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>.
          </p>              
        </g:if>

        <!-- if this period's assigned effort is already COMMITTED, then user is done with effort reporting -->
        <g:if test="${assignedEffortInstance && committedDateInstance}">
          <p>You have completed your effort reporting for this <strong>
            <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>. Thank you!
          </p>              
        </g:if>
        
        <!-- container for various FORMS (reportedEffort.main) -->
        <div id="remoteFormContainer">
          <g:include 
            controller="reportedEffort" 
            action="main" 
            params="${[reportingStaffId: reportingStaffInstance?.id, reportingPeriodId: reportingPeriodInstance?.id, assignedEffortId: assignedEffortInstance?.id]}" 
          />
        </div>

        <!-- PAST COMMITTED EFFORT SECTION -->
        <g:include 
          id="${reportingStaffInstance.id}" 
          controller="assignedEffort" 
          action="showPast" 
        />

<!-- spacing before footer -->  
    <div class="pageSpacing"> </div>
 
  </body>
  
</html>
