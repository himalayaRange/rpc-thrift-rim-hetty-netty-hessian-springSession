HRPC:一个高性能的RPC框架，基于Netty和Zookepper，序列化通过Google的protostuf

特点：
1.通过protostuf序列化对象
2.高可用，负载均衡，故障切换
3.在Zookepper上注册和订阅服务端
4.支持同步和异步调用远程方法
5.保持活连接，自动重新连接到服务器
6.由cglibd代理替换jdk自身的代理（.java动态代理是通过反射机制生成一个实现代理接口的匿名类，在具体调用方法前调用InvokeHandler来处理；cglib通过生成类的子类来处理，对于没有接口的类，只能选择cglib代理）
    性能比较：cglib创建对象时间较长，但执行效率高很多，所以对于单例对象采用cglib，反之采用jdk代理	
7.配置少
8.支持spring继承
