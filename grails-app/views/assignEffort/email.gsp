<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>NCS Direct Labor Reporting Notification Email</title>
  </head>
  
  <body>

    <p>Please submit your EFFORT for work done in 
      <strong><g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>
      for the <em>National Children's Study</em>, using the University of Minnesota - National Children's Study system.
    </p>
    
    To access this system:
    
    <ol>
      <li>Go to the <em>University of Minnesota - National Children's Study</em> login page (https://www.cccs.umn.edu/ncs/).</li>
      <li>Login with your University of Minnesota Internet ID (x.500 username) ${reportingStaffInstance.username} and password.</li>
    </ol>
    
    <p>Instructions for using the NCS Direct Labor Reporting application is available once you successfully login.</p>
    
    USER SUPPORT<br/>
    phone: 612-625.3453<br/>
    email: ncs_user_support@ncs.umn.edu<br/>
        
  </body>

</html>
