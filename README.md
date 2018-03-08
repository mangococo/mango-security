## 描述
    基于 spring-security、spring-security-oauth2实现的应用安全认证控制及社交登录。
    
## 说明
- 引入依赖(pom.xml)
```xml
<dependency>
    <groupId>stu.mango.security</groupId>
    <artifactId>mango-security-browser</artifactId>
    <version>${mango.security.version}</version>
</dependency>
```

- 配置系统(参见 application-example.properties)

- 增加UserDetailsService接口实现

- 如果需要记住我功能，需要创建数据库表(参见 db.sql)

- 如果需要社交登录功能，需要以下步骤
<br/>1).配置app-id和app-secret
<br/>2).创建并配置用户注册页面，并实现注册服务(需要配置访问权限)，注意在服务中要调用ProviderSignInUtils的doPostSignUp方法。
<br/>3).添加SocialUserDetailsService接口实现
<br/>4).创建社交登录用的表 (参见 db.sql) 