<%@ page contentType="text/html;charset=UTF-8" %>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS DLR Application Management</title>
  </head>

  <body>

    <p class="breadcrumbs">
        <g:link controller="main" action="index">DLR Home</g:link>
        &nbsp;&nbsp;&gt&nbsp;&nbsp;
        <span class="fontMaroon">Application Management</span>
      </p>
      
      <h1>Reports</h1>
		<!-- What did you do to the CSS??? -->
		<fieldset>
			<legend>Generate Report for a specific Reporting Period</legend>
			<g:form controller="export" action="reportingPeriod">
				<label for="id">Generate Report for period</label>
				<g:select name="id" from="${reportingPeriodInstanceList}" optionKey="id" />
				<label for="format">In Format</label>
				<g:select name="format" from="${reportFormats }" />
				<g:submitButton name="submit" value="Generate" />
			</g:form>
		</fieldset>
		

      <div class="pageSpacing"> </div>

  </body>

</html>
</html>