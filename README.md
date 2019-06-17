zdora介绍
===

### 环境

1. jdk1.8 
2. springboot websocket
3. maven 

### 功能

脚本执行平台,在节点机器上执行命令，并时时回显执行命令的过程。

### 打包

工程下有两个分支:master/client,master是server端，client是客户端，代码下载下来后，通过maven打成两个jar包:zdora-1.0.jar/zdora-client-jar-with-dependencies.jar

### 启动

1. server端启动,server端默认启动的端口是8080：

![image](https://raw.githubusercontent.com/zhangfei19841004/zdora/master/img/1.png)

2. client端启动:

`java -cp E:\workspace\zdora-client\target\zdora-client-jar-with-dependencies.jar com.zf.zdora.client.Run 192.168.1.3 8080`

  > 其中192.168.1.3是服务端的ip地址，8080是服务端的端口

### 使用

1. 打开http://192.168.1.3:8080/index(以启动服务端的IP为准)

![image](https://raw.githubusercontent.com/zhangfei19841004/zdora/master/img/2.png)

2. 在命令与参数中分别填写java -version点击执行

![image](https://raw.githubusercontent.com/zhangfei19841004/zdora/master/img/3.png)

3. 点击：

![image](https://raw.githubusercontent.com/zhangfei19841004/zdora/master/img/4.png)

4. 查看执行过程:

![image](https://raw.githubusercontent.com/zhangfei19841004/zdora/master/img/5.png)

### 应用场景

自动化多台机器执行控制



