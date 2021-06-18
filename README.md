# IRBL：A system for Information-Retrieval-based Bug Localization


### 可视化项目部署地址

[http://101.132.253.222/](http://101.132.253.222/)



### 本地运行操作方案

在application.yml的同路径下创建文件application-dev.yml

输入：

spring:
  datasource:
    username: （数据库账户）
    password: （数据库密码）
    url: jdbc:mysql://localhost:3306/irbl?serverTimezone=CTT&characterEncoding=UTF-8
    driver-class-name: com.mysql.cj.jdbc.Driver



预处理好之后的文件的存放路径：IRBL/data/preprocess

测试所用文件存放路径：IRBL/data/test



### Jenkins

[http://101.132.148.43:8080/](http://101.132.148.43:8080/)

U: irbl

P: jenkins@irbl

注：当push到release分支时会触发构建



### Harbor

101.132.148.43

U: admin

P: Harbor12345@irbl



### 覆盖率报告

[http://101.132.148.43:9090](http://101.132.148.43:9090/)



### 迭代一展示视频

[https://se3-ibrl.oss-cn-beijing.aliyuncs.com/video-1.mp4](https://se3-ibrl.oss-cn-beijing.aliyuncs.com/video-1.mp4)



### 迭代二展示视频

[https://se3-ibrl.oss-cn-beijing.aliyuncs.com/video-2.mp4](https://se3-ibrl.oss-cn-beijing.aliyuncs.com/video-2.mp4)