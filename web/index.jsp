<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>ResponseAndRequset</title>
    <link rel="stylesheet" type="text/css" href="./css/cascadingstylesheet.css">
  </head>
  <body>
  <div>
  <div class="div">

    <table>
      <tr>
        <td><a href="/servlet/html/login.html">loginTest</a></td>
      </tr>
      <tr>
        <td>

          <a href="./html/refresh.html">refresh.html</a>
        </td>
      </tr>
      <tr>
        <td> <form action="/servlet/refreshservlet" method="post" class="login-form">
          <input type="submit" value="跳 转 页 面" class="login-submit">
        </form></td>
      </tr>
      <tr>
        <td> <form action="/servlet/nocacheservlet" method="post" class="login-form">
          <input type="submit" value="当前时间禁用缓存" class="login-submit">
        </form></td>
      </tr>
      <tr>
        <td> <form action="/servlet/downloadservlet" method="post" class="login-form">
          <input type="submit" value="下载图片" class="login-submit">
        </form></td>
      </tr>
    </table>
  </div>
  </div>
  </body>
</html>