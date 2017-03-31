JAVA RMI:
1.接口必须继承java.util.Remote接口
2.方法必须抛出java.rmi.RemoteException;

Spring RMI整合：
1.可以不实现java.util.Remote接口，spring已经实现
2.方法可以不抛出异常

总结：1. spring 容器发布一个远程服务 是通过InitializingBean接口驱动起来的
    2. spring 包装了JDK Rmi  也就是说 服务端是spring暴露   客户端也可以用Jdk rmi调用 没有任何问题
    3. spring对没有实现Remote接口的远程服务 用RmiInvocationWrapper做了包装  RmiInvocationWrapper实现了Remote接口



注解开发参考：https://git.oschina.net/damivip/spring-remoting-annotation



***************************************************************************************************************************************

RMI作为服务的提供者

Zookepper作为服务的发布者

客户端消费者

注意：在分布式系统中，所有的服务都将服务注册到Zookepper中间件中，调用的调用都需要经过Zookepper来调用服务
	Zookepper就可以给调用者颁发Token，并监控已注册服务的调用情况与次数等数据。


*************************************************************************************************************************************


RMI通过JNDI和Zookepper，来实现RMI的高可用，如果想要通过spring发布并且将服务注册到Zookepper，需要改造源码或者进行扩展：	
>>RMI 使用了 Java 默认的序列化方式，对于性能要求比较高的系统，可能需要使用其它序列化方案来解决（例如：Protobuf）。
>>RMI 服务在运行时难免会存在出故障，例如，如果 RMI 服务无法连接了，就会导致客户端无法响应的现象。(高可用性)，解决高可用问题即通过Zookepper充当服务的注册地址

zookepper提供 “服务注册” 和“心跳检测”




