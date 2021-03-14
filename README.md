# A system for Information-Retrieval-based Bug Localization
在application.yml底下创建一个application-dev.yml

输入：

spring:
  datasource:
    password: （数据库密码）
    
## 文件保存路径说明
util包下的SavePath类定义了项目资源文件的保存路径，应修改为本地对应路径