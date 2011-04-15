<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS Direct Labor Reporting</title>
  </head>

  <body>

    <g:form name="directLaborReporting" method="post" controller="main" action="add">

      <fieldset class="maroonRoundRect">

        <legend>Direct Labor Reporting for [March 2011]</legend>

        <div class="prop">
          Welcome, ${reportingStaffInstance.firstName}



        </div>

      </fieldset>



    </g:form>

  </body>

</html>
