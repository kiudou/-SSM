# 秒杀系统-SSM
根据https://www.imooc.com/u/2145618/courses?sort=publish
## 做SSM项目时，遇到了无法连接本地数据库的问题 ##
机子环境：mysql 版本 8.0.11

原因分析：数据库版本太高，新版本的数据库驱动与旧的数据库驱动书写稍有不同，并且，部分jar包需更新到最新版本

jdbc.properties

  jdbc.driver=com.mysql.cj.jdbc.Driver
  
  jdbc.url=jdbc:mysql://127.0.0.1:3306/seckill？useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC

pom.xml里面

JDBC版本应为8.0.11

c3p0版本应为0.9.5.2
   
