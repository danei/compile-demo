<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Result</title>
</head>
<body>
<% String class_path="/internal/"+request.getParameter("name").replace("\\","/")+".class"; %>
<p><a href="<%=class_path%>"><%=class_path%></a></p>
<pre>${requestScope.log}</pre>
</body>
</html>
