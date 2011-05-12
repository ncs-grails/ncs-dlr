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
        <span class="fontMaroon">Effort Assignment</span>
      </p>

      <!-- start FORM -->
      <g:form>

        <!-- REPORTING MONTH control -->
        
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

        <!-- STAFF EFFORT-->
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
                  <g:formatNumber number="${ea.previousPeriodEffort}" type="percent" maxFractionDigits="2"/>
                  <g:hiddenField name="staff-${ea.staffId}.previousPeriodEffort" value="${ea.previousPeriodEffort}" />
                </td>

                <!-- checkbox: copy previous to current -->
                <td class="basic" style="text-align:center;">
                  <g:if test="${ea.previousPeriodEffort}">
                    <input type="checkbox" name="staff-${ea.staffId}.copyPreviousToCurrent"/>
                  </g:if>
                </td>

                <!-- textbox: this period's effort -->
                <td class="basic" style="text-align:right;">
                  <g:if test="${ea.isCommitted}" >
                    <g:formatNumber number='${ea.thisPeriodAssignedEffort}' type='percent' />                    
                  </g:if>
                <g:else>
                    <g:textField 
                      name="staff-${ea.staffId}.thisPeriodAssignedEffort" 
                      class="textfieldBasic" 
                      style="text-align:right;"
                      size="3" 
                      value="${g.formatNumber(number:(ea.thisPeriodAssignedEffort ?: 0) * 100, maxFractionDigits:2)}"
                    /> %
                  </g:else>

                </td>

                <!-- effort committed -->
                <td class="basic" style="text-align:right;">
                  <g:if test="${ea.percentCommitted && !ea.isCommitted}" >[</g:if>
                  <g:formatNumber number="${ea.percentCommitted}" type="percent" />
                  <g:if test="${ea.percentCommitted && !ea.isCommitted}" >]</g:if>
                </td>

                <!-- date effort committed -->
                <td class="basic" style="text-align:right;">
                  ${ea.dateCommitted}
                </td>

                <!-- dates email sent -->
                <td class="basic">
                  <br />${ea.datesEmailsSent?.join(', ')}
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

      <!-- end FORM -->
      </g:form>
      
      <div class="pageSpacing"> </div>

      
  </body>

</html>
