<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>Reporting Period Report</title>
		<style type="text/css">
			body {font-size: 9pt; }
			table { border-collapse: collapse; }
			td {
				border-style: solid;
				border-style: solid;
				border-color: black;
				border-width: thin;
				border-spacing: 0px;
				padding: 0.1em;
				vertical-align: top;
			}
			#periodDetails {
				font-size: 12pt;
			}
			@page {
    			size: 11in 8.5in;
  			}
		</style>
	</head>
	<body>
		<h1>Reporting Period Report</h1>
		<hr/>
		<div id="periodDetails">
		<dl>
			<dt>Period:</dt>
			<dd>${reportingPeriodInstance}</dd>
			
			<dt>Invoice #:</dt>
			<dd>${referenceInvoiceNumber}</dd>
			
			<dt>Date Prepared:</dt>
			<dd>${preparedDate}</dd>
			
			<dt>Completed Report Date:</dt>
			<dd>${completedReportDate}</dd>
			
		</dl>
		</div>
		
		<table>
			<thead>
				<tr>
					<th>Staff</th>
					<th>Assigned</th>
					<th>Category</th>
					<th>Committed</th>
					<th>Activity</th>
					<th>Task</th>
					<th>Percent</th>
				</tr>
			</thead>
			<tbody>
			<g:each var="ae" in="${reportingPeriodInstance?.assignedEfforts.sort{ it.reportingStaff.lastName }}">
				<tr>
					<td rowspan="${ae.reportedEffort.size()}">
						${ae.reportingStaff}</td>
					<td rowspan="${ae.reportedEffort.size()}">
						<g:formatNumber number="${ae.assignedEffort}" type="percent" /></td>
					<td rowspan="${ae.reportedEffort.size()}">
						${ae.laborCategory}</td>
					<td rowspan="${ae.reportedEffort.size()}">
						<g:formatDate date="${ae.dateCommitted}" format="M/d/yyyy" /></td>
					<g:each status="i" var="re" in="${ae.reportedEffort}">
	    				<g:if test="${i > 0}">
	    					<!-- If this is the second row, let's shift it in. -->
							${"</tr><tr>"}
	    				</g:if>
						<td>${re.activity}</td>
						<td>${re.task}</td>
						<td>
							<g:formatNumber number="${re.percentEffort}" type="percent" />
						</td>
					</g:each>
					<g:if test="${ ! ae.reportedEffort}">
						<td/><td/><td/>
					</g:if>
				</tr>
			</g:each>
			</tbody>
		</table>
		
		
	</body>
</html>