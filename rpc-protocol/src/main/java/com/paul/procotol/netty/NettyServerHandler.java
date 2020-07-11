package com.paul.procotol.netty;

import java.lang.reflect.Method;

import com.paul.framework.RpcRequest;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

	private ChannelHandlerContext context;


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
		System.out.println("server channelRead...");
		System.out.println(ctx.channel().remoteAddress() + "->server:" + rpcRequest.toString());
		InvokeTask it = new InvokeTask(rpcRequest,ctx);
		NettyServer.submit(it);
	}


	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
		this.context = ctx;		
	}
	
}
