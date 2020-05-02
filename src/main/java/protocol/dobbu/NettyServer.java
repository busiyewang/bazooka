package protocol.dobbu;

import java.util.concurrent.Executor;

import static java.util.concurrent.Executors.*;

public class NettyServer {

    private static NettyServer INSTANCE = new NettyServer ();

    //todo 创建线程池子
    private static Executor executor = newCachedThreadPool ();

    private final static int MESSAGE_LENGTH = 4;

    private NettyServer() {
    }

    public static NettyServer getInstance() {
        return INSTANCE;
    }

  //private SerializeType serializeType = SerializeType.queryByType(Configuration.getInstance().getSerialize());

    public static void submit(Runnable t){
        executor.execute(t);
    }




}
