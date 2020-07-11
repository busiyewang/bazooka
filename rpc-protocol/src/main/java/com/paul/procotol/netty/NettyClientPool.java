package com.paul.procotol.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClientPool {

    public  static Map<String,NettyClientHandler> holder = new ConcurrentHashMap<>();
}
