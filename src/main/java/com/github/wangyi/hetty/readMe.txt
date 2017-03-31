Hetty是一款构建于Netty和Hessian基础上的高性能RPC框架，采用是二进制的RPC协议，非常轻量级且速度快
Netty是一款基于事件驱动的NIO框架，用于开发高性能的，高可用性的网络服务器和客户端程序。Hetty客户端完全由Hessian实现，只是用Netty重新实现了服务端

HettyConfig类中254行路径要随着项目进行修改

Hetty是一款集成了Hessian和Netty框架的高性能的RPC框架。使用Hessian作为客户端，保证了Hetty的简单、快捷和跨语言的特性；使用Netty作为服务器端，使Hetty的性能更加出色。在结合了两个框架所有的优点外，Hetty添加了权限认证、开发模式、服务版本等新功能。

Hetty 1.2.3包含如下更新：

1.使用logback替换log4j。

2.修复方法重载时调用的BUG。

3.增加调试模式。

4.增加权限配置部分。

5.对代码包结构进行了调整。