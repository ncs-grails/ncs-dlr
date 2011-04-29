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
              from="${periodList}"
              optionKey="id"
              value="reportingPeriod" />
          </span>
        </div>

        <!-- STAFF EFFORT-->
        <table>

          <thead>
            <tr>
              <th class="basic" colspan="11">
                <strong>${reportingPeriodInstance}  (Period ID: ${reportingPeriodInstance?.id})</strong>
              </th>
            </tr>
            <tr>
              <th class="basic" rowspan="2"></th>
              <th class="basic" rowspan="2">Staff Name (Staff Id)</th>
              <th class="basic" colspan="3">Assigned Effort</th>
              <th class="basic" colspan="2">Committed Effort *</th>
              <th class="basic" colspan="4">Email Notification</th>
            </tr>
            <tr>
              <th class="basic">Copy Previous<br />to Current</th>
              <th class="basic">Previous<br />Perod</th>
              <th class="basic">Current<br />Perod</th>
              <th class="basic">%</th>
              <th class="basic">Date</th>
              <th class="basic">Date<br />Initial Sent</th>
              <th class="basic">Date Last<br />Reminder<br />Send</th>
              <th class="basic">Total<br />Sent</th>
              <th class="basic">Send<br />Now</th>
            </tr>
          </thead>

          <tbody>
            <g:each var="ai" in="${reportingPeriodInstance?.assignedEfforts}" >
              <tr>
                <td class="basic">${AssignedEffort.reportingStaff?.fullName}</td>
                <td class="basic"><input type="checkbox" /></td>
                <td class="basic">${ai.previousAssignedEffort.assignedEffort}</td>
                <td class="basic"></td>
                <td class="basic"></td>
                <td class="basic"></td>
                <td class="basic"></td>
                <td class="basic"></td>
                <td class="basic"></td>
                <td class="basic"></td>
                <td class="basic"></td>
              </tr>
            </g:each>
          </tbody>

        </table>
        <span class="tableFooterNote">* Effort not committed yet appear in brackets []</span>

      </g:form>

  </body>

</html>
