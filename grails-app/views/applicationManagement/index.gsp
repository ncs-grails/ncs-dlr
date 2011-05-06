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
    <title>NCS DLR Application Management</title>
  </head>

  <body>
      <h1>DLR Application Management</h1>

      <p class="breadcrumbs">
        <g:link controller="main" action="index">DLR Home</g:link>
        &nbsp;&nbsp;&gt&nbsp;&nbsp;
        Application Management
        &nbsp;&nbsp;&gt&nbsp;&nbsp;
        Staff
        &nbsp;&nbsp;::&nbsp;&nbsp;
        <g:link controller="assignEffort" action="show">Effort Assignment</g:link>
        &nbsp;&nbsp;::&nbsp;&nbsp;
        Report Generation
        &nbsp;&nbsp;::&nbsp;&nbsp;
        Report Archive   
      </p>

  </body>

</html>
