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

      <!-- REPORTING MONTH control -->
      <g:form>
        <p>Reporting Month
          <g:select
            name="reportingPeriod.id"
            from="${periodList}"
            optionKey="id"
            value="${reportingPeriod?.id}" />
        </p>        
      </g:form>

      <!-- STAFF EFFORT-->
      <g:form action="update">

        <g:hiddenField name="reportinPeriod" value="${reportingPeriod?.id}" />

        <table>

          <thead>
            <tr>
              <th colspan="11">
                
                (Period ID: ${reportinPeriod?.id})
              </th>
            </tr>
            <tr>
              <th></th>
              <th>Staff Name (Code)</th>
              <th colspan="3">Assigned Effort</th>
              <th colspan="2">Committed Effort *</th>
              <th colspan="4">Email Notification</th>
            </tr>
            <tr>
              <th>Copy Previous<br />to Current</th>
              <th>Previous<br />Perod</th>
              <th>Current<br />Perod</th>
              <th>%</th>
              <th>Date</th>
              <th>Date<br />Initial Sent</th>
              <th>Date Last<br />Reminder<br />Send</th>
              <th>Total<br />Sent</th>
              <th>Send<br />Now</th>
            </tr>
          </thead>

          <tbody>
            <g:each var="ai" in="${reportingPeriod?.assignedEfforts}" >
              <tr>
                <td>${ai.reportingStaff?.fullName}</td>
                <td><input type="checkbox" /></td>
                <td>${ai.previousAssignedEffort.assignedEffort}</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
            </g:each>
          </tbody>

        </table>

      </g:form>

  </body>

</html>
