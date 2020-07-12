package com.paul.procotol.nio;


import sun.awt.image.ImageWatched;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 封装了selector.select()等事件轮询的代码
 */
abstract  class ReactorThread extends Thread{

    private Selector selector;
    private  LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue();


    /**
     * select 监听到有事件后，调用这个方法
     */
    public abstract  void handler(SelectableChannel channel) throws Exception;

    public ReactorThread() throws IOException{
        this.selector =  Selector.open();
    }

    volatile boolean running = false;

    @Override
    public void run() {
        // 轮询Selector事件
        while (running) {
            try {
                // 执行队列中的任务
                Runnable task;
                while ((task = taskQueue.poll()) != null) {
                    task.run();
                }
                selector.select(1000);

                // 获取查询结果
                Set<SelectionKey> selected = selector.selectedKeys();
                // 遍历查询结果
                Iterator<SelectionKey> iter = selected.iterator();
                while (iter.hasNext()) {
                    // 被封装的查询结果
                    SelectionKey key = iter.next();
                    iter.remove();
                    int readyOps = key.readyOps();
                    // 关注 Read 和 Accept两个事件
                    if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0) {
                        try {
                            SelectableChannel channel = (SelectableChannel) key.attachment();
                            channel.configureBlocking(false);
                            handler(channel);
                            if (!channel.isOpen()) {
                                key.cancel(); // 如果关闭了,就取消这个KEY的订阅
                            }
                        } catch (Exception ex) {
                            key.cancel(); // 如果有异常,就取消这个KEY的订阅
                        }
                    }
                }
                selector.selectNow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SelectionKey register(SelectableChannel channel) throws Exception {
        // 为什么register要以任务提交的形式，让reactor线程去处理？
        // 因为线程在执行channel注册到selector的过程中，会和调用selector.select()方法的线程争用同一把锁
        // 而select()方法实在eventLoop中通过while循环调用的，争抢的可能性很高，为了让register能更快的执行，就放到同一个线程来处理
        FutureTask<SelectionKey> futureTask = new FutureTask<>(() -> channel.register(selector, 0, channel));
        taskQueue.add(futureTask);
        return futureTask.get();
    }
    public void doStart() {
        if (!running) {
            running = true;
            start();
        }
    }
}
