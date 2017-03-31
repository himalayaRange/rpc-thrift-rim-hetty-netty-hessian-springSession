web.xml配置注意：
<context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-mvc.xml</param-value>
</context-param>
<!-- 分布式Session共享Filter -->
<filter>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSessionRepositoryFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>

注意：1.spring的这个配置文件一定要写在web.xml的<context-param>部分，写在其他地方不行。

　　   2.filter的名字必须是springSessionRepositoryFilter

　　   3.如果使用了shiro，web.xml中一定要把<context-param>放在最前面，然后写shiro的Filter配置，再写spring-session的Filter配置。后面就是其他的编码和servlet配置了。


说明：
	Spring加入Filter,本质是对每一个请求都会被DelegatingFilterProxy进行一次封装，Controller中拿到的request.getSessio(),实际上是redis中的session

 