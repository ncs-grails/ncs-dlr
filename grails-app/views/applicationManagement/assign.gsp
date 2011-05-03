<%@ page contentType="text/html;charset=UTF-8" %>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS DLR &gt; Application Management &gt; Effort Assignment</title>
  </head>

  <body>

      <h1>DLR Application Management : Effort Assignment</h1>

      <g:form action="update">
        
        <g:hiddenField name="reportingPeriod" value="${reportingPeriodInstance}" />

      <!-- REPORTING MONTH control -->
        <div class="padding">
          <span class="controlBackground">Reporting Month
            <g:select class="basic"
              name="reportingPeriodInstance.id"
              from="${periodSelectList}"
              optionKey="id"
              optionValue="name"
              value="${reportingPeriodInstance.id}" />&nbsp;
            <g:actionSubmit class="buttonBasic" value="GO" />          
          </span>
        </div>

        <!-- STAFF EFFORT-->
        <table>

          <thead>
            <tr>
              <th class="basic" colspan="11">
                <strong><g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" />  (Period ID: ${reportingPeriodInstance?.id})</strong>
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
              <tr><td class="basic" style="text-align:right;">${ea.rowNum}</td>
                <td class="basic">${ea.fullName}&nbsp;&nbsp;(${ea.staffId})</td>
                <td class="basic" style="text-align:right;"><g:formatNumber number="${ea.previousPeriodEffort}" type="percent" /></td>
                <td class="basic" style="text-align:center;"><input type="checkbox" /></td>
                <td class="basic" style="text-align:right;">
                  <g:if test="${ea.isCommitted}" >${ea.currentPeriodEffort}</g:if>
                  <g:else><g:textField name="staff-${ea.staffId}.currentPeriodEffort" value="${ea.currentPeriodEffort}" class="textfieldBasic" size="3" style="text-align:right;"/> %</g:else>
                </td>
                <td class="basic" style="text-align:right;"><g:formatNumber number="${ea.percentCommitted}" type="percent" /></td>
                <td class="basic" style="text-align:right;">${ea.dateCommitted}</td>
                <td class="basic">${ea.datesEmailSent?.join(', ')}</td>
                <td class="basic" style="text-align:center;"><input type="checkbox"/></td>
              </tr>
            </g:each>
          </tbody>

        </table>
        
        <div class="tableFooterNote">* Effort not committed yet appear in brackets []</div>

      </g:form>

  </body>

</html>
