package nettyDemo;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class NioServer {
    static class ClientProcessor implements Runnable {
        /**
         * 选择器使用
         * 1、通道将自身注册到一个选择器上
         * 2、选择器执行select方法阻塞等待事件发生，或者selectNow等待事件发生并且在无事件时快速返回
         * 3、选择器执行selectedKeys方法获得select或selectNow方法返回期间发生的IO事件。
         */
        private Selector selector;

        private ClientProcessor(Selector Selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select ();
                    // 返回此选择器的选定键集。
                    Set <SelectionKey> selectionKeys = selector.selectedKeys ();
                    //返回此集合中元素的迭代器
                    Iterator <SelectionKey> iterator = selectionKeys.iterator ();

                    while (iterator.hasNext ()) {
                        SelectionKey key = iterator.next ();
                        if (!key.isValid ()) {
                            continue;
                        }

                        if (key.isValid ()) {
                            //将字节数组包装到缓冲区中
                            ByteBuffer buffer = ByteBuffer.wrap ( new byte[16] );
                            SocketChannel clientChannel = (SocketChannel) key.channel ();
                            int read = clientChannel.read ( buffer );

                            if (read == -1) {//关闭分支
                                key.channel ();
                                clientChannel.close ();
                            } else {//读写分支
                                buffer.flip ();
                                // ByteBuffer的位置，或者说读写开始的下标。
                                int              position = buffer.position();
                                //ByteBuffer的position的终点，或者说position的增长不能超出limit
                                int              limit    = buffer.limit();
                                List<ByteBuffer> buffers  = new ArrayList<> ();

                                //新增二：按照协议从流中分割出消息
                                /**从buffer确认每一个字节，发现分割符则切分出一个消息**/
                                for (int i = position; i < limit; i++){
                                    //读取到消息结束符
                                    if (buffer.get(i) == '\r'){
                                        ByteBuffer message = ByteBuffer.allocate(i - buffer.position()+1);
                                        buffer.limit(i+1);
                                        message.put(buffer);
                                        buffer.limit(limit);
                                        message.flip();
                                        buffers.add(message);
                                        buffer.limit(limit);
                                    }
                                }
                                /**从readBuffer确认每一个字节，发现分割符则切分出一个消息**/
                                /**将所有得到的消息发送出去**/
                                for (ByteBuffer bufferr : buffers)
                                {
                                    //新增三
                                    while (bufferr.hasRemaining())
                                    {
                                        clientChannel.write(bufferr);
                                    }
                                }
                                /**将所有得到的消息发送出去**/
                                //新增四：压缩readBuffer，压缩完毕后进入写入状态。并且由于长度是256，压缩之后必然有足够的空间可以写入一条消息
                                buffer.compact();
                                clientChannel.write ( buffer );
                            }
                        }
                        iterator.remove ();
                    }

                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }

        }
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        try {
            //打开服务器套接字通道。
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open ();
            serverSocketChannel.configureBlocking ( false );
            final Selector[] selectors = new Selector[Runtime.getRuntime ().availableProcessors ()];

            for (int i = 0; i < selectors.length; i++) {
                final Selector selector = Selector.open ();
                selectors[i] = selector;
                new Thread ( new ClientProcessor ( selector ) ).start ();
            }
            AtomicInteger id = new AtomicInteger ();
            Selector selector = Selector.open ();
            serverSocketChannel.register ( selector, SelectionKey.OP_ACCEPT );


            while (true) {
                selector.select ();
                Set <SelectionKey> selectionKeys = selector.selectedKeys ();
                Iterator <SelectionKey> iterator = selectionKeys.iterator ();
                while (iterator.hasNext ()) {
                    iterator.next ();
                    SocketChannel socketChannel = serverSocketChannel.accept ();
                    socketChannel.register ( selectors[id.getAndIncrement () % selectors.length], SelectionKey.OP_READ );
                    iterator.remove ();
                }
            }


        } catch (IOException e) {
            e.printStackTrace ();
        }


    }


}





