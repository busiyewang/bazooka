# bazooka  一个rpc的基础实现


思考问题：
1、为什么用rpc不用http
对于直接使用 HTTP 请求来说，适合接口较少，服务之间相互调用不多的情况，而且需要指定地址、端口等信息。优点就是简单直接，但是无法应对高并发及复杂服务交互的情况。
RPC 就是为了解决上面的问题而生的，首先用户只需要调用服务即可，连接和发送数据交给底层协议去完成，另外对于高并发支持非常好，另外提供服务治理、注册中心等功能。适合大型网站，服务复杂、高并发的情况。

2、rpc 如何处理序列化的实现的

##中文简介
1. RPC 基于 Java 编写，网络通信依赖与 netty，http，socket。
2. 支持基于配置的底层协议切换，可以选择 netty，http，socket。
3. 基于 Spring 开发，接口代理类自动注入到客户端，使用 @Autowired 注入即可，用户无需关注底层实现。
4. 支持 Spring xml 格式配置，通过 xml 完成代理类注入，服务端启动，通信协议选择。
5. 服务端使用线程池提高并发能力。
6. 客户端使用 channel 缓存提高并发能力。
7. 支持多序列化协议，多负载均衡协议选择。


##模块介绍
rpc-procotol： 基于http，nio的reactor模型，netty框架异步通信模块 
rpc-spring：框架是基于 Spring 开发的，这个模块是将Spring 整合起来，比如自动注入代理 Bean，启动服务端 Server 等。 
rpc-register：注册中心模块，负责服务发现和容错。 
rpc-monitor：将注册中心的信息显示在网页上。 
rpc-consumer：消费端模块，用于测试。 
rpc-provider：服务端模块，用于测试。





