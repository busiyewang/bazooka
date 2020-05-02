# bazooka  一个rpc的基础实现

中文简介
1、RPC 基于 Java 编写，网络通信依赖与 netty，http，socket。
2、支持基于配置的底层协议切换，可以选择 netty，http，socket。
3、基于 Spring 开发，接口代理类自动注入到客户端，使用 @Autowired 注入即可，用户无需关注底层实现。
4、支持 Spring xml 格式配置，通过 xml 完成代理类注入，服务端启动，通信协议选择。
5、服务端使用线程池提高并发能力。
6、客户端使用 channel 缓存提高并发能力。
7、支持多序列化协议，多负载均衡协议选择。
