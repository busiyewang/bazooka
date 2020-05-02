package nettyDemo;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class NettyDemo {
    public static void main(String[] args) {
        //允许注册channel的，多线程，事件循环
        EventLoopGroup boss = new NioEventLoopGroup ();
        EventLoopGroup woker  = new NioEventLoopGroup (  );





    }

}
