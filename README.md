# 商城
(pinyougou) :

SOA架构项目学习.

品优购商城项目,是基于SOA架构实现的分布式商城.

分布式调用落地采取的是传统的Dubbox

整个系统,分为web端,服务端以及对应的底层数据支撑.

服务端注册到dubbox的注册中心,供给各个web端应用进行调用.


# 最终系统架构搭建目标:

![image](https://raw.githubusercontent.com/wiki/cynen/pinyougou/最终目标1.png)

// 服务太多了,分开展示:
![image](https://raw.githubusercontent.com/wiki/cynen/pinyougou/最终目标2.png)


# 使用框架和技术

```
SpringDataRedis
SpringDataSolr
SpringSecurity
SpringTask
FastDFS
JMS
Dubbox
CAS
```

# 
dubbo-service 是dubbo提供方.

dubbo-consumer 是dubbo调用方.



# CAS 服务器的配置

1.下载当前项目中的 cas文件夹.

2.将下载好的cas文件夹全部拷贝到 Tomcat(建议tomcat7以上)的 `webapps` 目录下

3.修改Tomcat的服务器端口.  conf/server.xml 键默认的8080修改成9100 (自定义)

4.修改cas中 `WEB-INF/cas.properties` 配置文件,修改server.name 的端口 (和tomcat一致)

5.修改 `WEB-INF/deployerConfigContext.xml` 文件中的最后配置的数据库.

6.启动tomcat

7.访问 localhost:9100/cas/login 即可看到登录界面.
