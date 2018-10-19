<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<html>
<head>
    <%@ include file="/WEB-INF/jsp/common/resource.jspf"%>
    <%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
    <title>zdora</title>
    <script type="text/javascript">
        var socket;
        if (typeof(WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
        } else {
            console.log("您的浏览器支持WebSocket");
            var cid = '${cid}';
            console.log('${ctx}');
            console.log('${pageContext.request.contextPath}');
            //实现化WebSocket对象，指定要连接的服务器地址与端口 建立连接
            // 等同于socket = new WebSocket("ws://localhost:8083/checkcentersys/websocket/20");
            socket = new WebSocket(("http://localhost:8080/websocket/" + cid).replace("http", "ws"));
            //打开事件
            socket.onopen = function () {
                console.log(cid + " Socket 已打开");
                //socket.send("这是来自客户端的消息" + location.href + new Date());
            };
            //获得消息事件
            socket.onmessage = function (msg) {
                $("#content").text(msg.data);
                //发现消息进入 开始处理前端触发逻辑
            };
            //关闭事件
            socket.onclose = function () {
                console.log("Socket已关闭");
            };
            //发生了错误事件
            socket.onerror = function () {
                alert("Socket发生了错误");
                //此时可以尝试刷新页面
            }
        }
    </script>
</head>
<body>
<span id="content"></span>
</body>
</html>

