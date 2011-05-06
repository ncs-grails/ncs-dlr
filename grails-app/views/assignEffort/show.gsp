<%@ page contentType="text/html;charset=UTF-8" %>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>Effort Assignment</title>
  </head>

  <body>

      <h1>Effort Assignment</h1>
      
      <p class="breadcrumbs">
        <g:link controller="main" action="index">DLR Home</g:link>
        &nbsp;&nbsp;&gt&nbsp;&nbsp;
        <g:link controller="applicationManagement" action="index">Application Management</g:link>
        &nbsp;&nbsp;&gt&nbsp;&nbsp;
        Staff
        &nbsp;&nbsp;::&nbsp;&nbsp;
        <g:link controller="assignedEffort" action="show">Effort Assignment</g:link>
        &nbsp;&nbsp;::&nbsp;&nbsp;
        Report Generation
        &nbsp;&nbsp;::&nbsp;&nbsp;
        Report Archive   
      </p>

      <!-- REPORTING MONTH control -->
      <g:form action="show">
        
        <g:hiddenField name="reportingPeriod" value="${reportingPeriodInstance}" />

        <div class="padding">
          <span class="controlBackground">Reporting Month
            <g:select class="basic"
              name="reportingPeriodInstance.id"
              from="${periodSelectiontList}"
              optionKey="id"
              optionValue="name"
              value="${reportingPeriodInstance.id}" />&nbsp;
            <g:actionSubmit class="buttonBasic" value="GO" constructor="" action="show" />          
          </span>
        </div>

      </g:form>

      <!-- STAFF EFFORT-->
      <g:form action="update">
        <table>

          <thead>
            <tr>
              <th class="basic" colspan="11">
                <strong>
                  <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" />&nbsp;
                    (Period ID: ${reportingPeriodInstance?.id})
                </strong>
              </th>
            </tr>
            <tr>
              <th class="basic" rowspan="2"></th>
              <th class="basic" rowspan="2">Staff Name (Staff Id)</th>
              <th class="basic" colspan="3">Assigned Effort</th>
              <th class="basic" colspan="2">Committed Effort *</th>
              <th class="basic" colspan="2">Email Notification</th>
            </tr>
            <tr>
              <th class="basic">Previous<br />Perod</th>
              <th class="basic">Copy Previous<br />to Current</th>
              <th class="basic">Current<br />Perod</th>
              <th class="basic">%</th>
              <th class="basic">Date</th>
              <th class="basic">Dates<br />Email Sent</th>
              <th class="basic">Send<br />Now</th>
            </tr>
          </thead>

          <tbody>
            
            <g:each var="ea" in="${effortAssignmentList}" >
              
              <tr>
                
                <!-- row number -->
                <td class="basic" style="text-align:right;">
                  ${ea.rowNum}
                </td>

                <!-- staff name & id -->
                <td class="basic">
                  ${ea.fullName}&nbsp;&nbsp;(${ea.staffId})
                </td>

                <!-- previous effort -->
                <td class="basic" style="text-align:right;">
                  <g:formatNumber number="${ea.previousPeriodEffort}" type="percent" />
                </td>

                <!-- checkbox: copy previous to current -->
                <td class="basic" style="text-align:center;">
                  <input type="checkbox" name="staff-${ea.staffId}.copyPreviousToCurrent"/>
                </td>

                <!-- textbox: this period's effort -->
                <td class="basic" style="text-align:right;">
                  <g:if test="${ea.isCommitted}" >
                    ${ea.thisPeriodPeriodEffort}
                  </g:if>
                  <g:else>
                    <g:textField 
                      name="staff-${ea.staffId}.thisPeriodEffort" 
                      value="${ea.thisPeriodEffort}" 
                      class="textfieldBasic" 
                      size="3" 
                      style="text-align:right;"
                    /> %
                  </g:else>                  
                </td>

                <!-- effort committed -->
                <td class="basic" style="text-align:right;">
                  <g:formatNumber number="${ea.percentCommitted}" type="percent" />
                </td>

                <!-- date effort committed -->
                <td class="basic" style="text-align:right;">
                  ${ea.dateCommitted}
                </td>

                <!-- dates email sent -->
                <td class="basic">
                  ${ea.datesEmailsSent?.join(', ')}
                </td>

                <!-- checkbox: send email -->
                <td class="basic" style="text-align:center;">
                  <input type="checkbox" name="staff-${ea.staffId}.sendNow"/>
                </td>
                
              </tr>
              
            </g:each>
          
          </tbody>

        </table>
        
        <div class="tableFooterNote">* Effort not committed yet appear in brackets []</div>
        
        <g:actionSubmit class="buttonBasic" value="SUBMIT" action="update"/>

      </g:form>

  </body>

</html>
