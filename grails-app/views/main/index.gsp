sqv<%@ page contentType="text/html;charset=UTF-8" %>
<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS Direct Labor Reporting</title>
  </head>

  <body>

    <g:form name="directLaborReporting" method="post" controller="main" action="add">
      
      <h1>Direct Labor Reporting</h1>

      <p class="breadcrumbs">
        <g:link controller="applicationManagement" action="index">Application Management</g:link>
      </p>
    
      <p class="fontMaroon">
        Welcome, <strong>${reportingStaffInstance.fullName}!</strong>
      </p>
      
      <!-- <fieldset class="maroonRoundRect"> -->

      <!--
        <legend>
          <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM" /> ${reportingPeriodInstance.year}
        </legend>
      -->

        <div class="prop">
          
            <!-- if there are ASSIGNED effort and they have not been committed yet, alert user to complete them -->
            <g:if test="${assignedEffortInstance}">
              <!-- if there is a COMMITTED DATE, then user is done with effort reporting -->
              <g:if test="${committedDateInstance}">
                <span class="fontMaroon">You have committed your effort for this this reporting month.</span>
              </g:if>
              <g:else>
                <g:include
                  controller="main"
                  action="show"
                  params="[
                    'staff_id':reportingStaffInstance.id,
                    'period_id':reportingPeriodInstance.id,
                    'assigned_effort_id': assignedEffortInstance.id
                  ]"
                />
              </g:else>
            </g:if>
            <g:else>
              <p class="fontMaroon">You do not have any effort to report.</p>
            </g:else>

        </div>

      <!-- </fieldset> -->

    </g:form>

  </body>

</html>
