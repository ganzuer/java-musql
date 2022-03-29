
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>operation</title>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        .main {
            width: 1200px;
            height: 800px;
            background-color: antiquewhite;
            margin: 0 auto;
        }
        form{
            display: inline;
        }
        table{
            text-align: center;
        }
    </style>
    <script src="./js/jquery-3.6.0.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
<form action="./index.html" method="post"><input type="submit" value="登录" id="login"></form>
<form action="/login?action=leave" method="post"><input type="submit" value="注销"></form>
<div class="main">

    <form action="/file?action=upload" method="post" enctype="multipart/form-data">
        <label>上传的文件:</label><br>
        <input type="file"  size="20" maxlength="80" required name="upload">
        <input type="submit" value="上传"  id="up" name="submit">
    </form>


    <br>
    <hr>
    <br>

    <label>存储的文件信息:</label><br>
    <table cellspacing="0" border="1px soild black">
        <thead>
        <tr>
            <td>文件名</td>
            <td>文件大小</td>
            <td width="200px">上传日期</td>
            <td> 操作 </td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${ files }">
            <tr>
                <td> ${ item.fileName }</td>
                <td>
                    <fmt:formatNumber type="number" value="${item.fileSize/1024}" maxFractionDigits="1" minFractionDigits="1"/>  KB
                </td>
                <td>
                    <fmt:formatDate type="time" value="${item.uploadTime}" pattern="yyyy-MM-dd hh:mm:ss" />
                </td>
                <td>
                    <form action="/file?action=delete&id=${item.id}" method="post">
                        <input type="submit" value="删除" id="delete">
                    </form>
                    <form action="/file?action=download&id=${item.id}" method="post">
                        <input type="submit" value="下载" >
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <br>
    <hr>
    <br>

    <form action="/file?action=catch" method="post" enctype="multipart/form-data">
        <label>识别内容的文件:</label><br>
        <input type="file" name="read" required>
        <input type="submit" value="识别" name="submit"> <br>
        <label>识别的内容:</label><br>
        <textarea rows="20" cols="100" disabled="disabled">${mess}</textarea>
    </form>

</div>
</body>
<script>
    $('#up').attr("disabled",${online});
    $('#delete').attr("disabled",${online});
    $('#login').attr("disabled",!${online});
</script>
</html>
