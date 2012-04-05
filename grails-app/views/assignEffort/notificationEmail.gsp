<%@ page contentType="text/html"%><html>

	<head>
		<title>NCS Direct Labor Reporting Notification Email</title>
	</head>

	<body>

		Dear ${reportingStaffInstance.firstName} ${reportingStaffInstance.lastName}:

		<p>Please submit your EFFORT for work done <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /> for the National Children's Study, using the University of Minnesota - National 
			Children's Study system. <strong>Because there have been changes in effort for various staff members, please keep these important issues in mind when 
			completing your DLR</strong>.
			<ul>
				<li>Your total effort or allocation of effort among projects for the National Children's Study may have changed starting in <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" />.</li>
				<li>Some staff who did not previously report effort on one of the formative research projects (e.g., Executive Function, Nutrition, etc) 
					may now need to report effort for formative research, in addition to any effort on the hi-lo project.
				</li>
				<li>If you have effort on the hi-lo project, select the "High-Low Recruitment Vanguard" activity and <strong>use as few "tasks" as feasible 
					to describe your effort</strong> (i.e., when selecting from the drop down menu, try not to choose more than 3 tasks per activity).
				</li>
				<li>If you have effort on one or more of the formative projects, select the appropriate "Activity" specific to that project (e.g.,
					"Executive Function"," "Dietary Assessment", etc.) and <strong>use as few "tasks" as feasible</strong> to describe your effort.
				</li>
				<li>Your committed effort should include the total of <u>all NCS effort</u> (i.e., all NCS projects you are paid on).</li>
				<li><strong>Please contact your NCS supervisor if you have questions regarding your effort on any of the given project</strong>.</li>
			</ul>
		</p>

		To access this system:
		<ol>
			<li>Open Mozilla Firefox browser.</li>
			<li>Go to the <em>secure</em> <strong>University of Minnesota - National Children's Study</strong> 
				login page (<strong><a href="https://secure.ncs.umn.edu/ncs-dlr/">https://secure.ncs.umn.edu/ncs-dlr/</a></strong>).
			</li>
			<li>Login with your University of Minnesota Internet ID (x.500 username) <strong>${reportingStaffInstance.username}</strong> and password.</li>
		</ol>

		USER SUPPORT<br/>
		phone: 612-626-8160 (ask for NCS IT support)<br/>
		email: help@ncs.umn.edu<br/>

	</body>

</html>
