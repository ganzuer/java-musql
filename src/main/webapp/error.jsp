
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"  %>
<html>
<head>
    <title>error</title>
</head>
<body>
</body>
<script>
    let error=${error};
    if(error==101){
        alert("账号或密码错误！");
        window.location.href="./index.html";
    }
    else if(error==102){
        alert("无权限！");
        window.location.href="/file?action=show";

    }else {
        window.location.href="./index.html";
    }
</script>
</html>
