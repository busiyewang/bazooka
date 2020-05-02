package protocol.http;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;


public class HttpServer {

      private static  HttpServer INSTANCE = new HttpServer();

      private HttpServer(){};


      public static HttpServer getINSTANCE(){
          return  INSTANCE;
      }

    /**
     *  初始化tomcat容器
     *  初始化servlet 容器
     * @param hostname  主机名
     * @param port
     */
    public void start(String hostname, Integer port){
        Tomcat tomcat = new Tomcat();
        Server server =  tomcat.getServer();
        Service service = server.findService ( "Tomcat" );

        // 创建连接器
        Connector connector= new Connector (  );
        connector.setPort ( port );

        //tomcat的servlet引擎
        Engine engine = new StandardEngine ();
        engine.setDefaultHost ( hostname );

        Host host = new StandardHost ();
        // 设置Host
        host.setName(hostname);

        // 配置tomcat上下文
        String contextpath = "";
        Context context = new StandardContext ();
        context.setPath ( contextpath );
        //生命周期监听
        context.addLifecycleListener ( new Tomcat.FixContextListener () );

        host.addChild ( context );
        engine.addChild ( host );

        service.setContainer ( engine );
        service.addConnector ( connector );

        // 配置请求拦截转发
        tomcat.addServlet ( contextpath,"dispatcher",new DispatcherServlet () );
        //servlet 映射
        context.addServletMappingDecoded ( "/*","dispatcher" );


        try {
            // 启动tomcat
            tomcat.start ();
            // 保持tomcat的启动状态
            tomcat.getServer ().await ();
        } catch (LifecycleException e) {
            e.printStackTrace ();
        }


    }

}
