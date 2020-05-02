package protocol.http;

import framework.RpcRequest;
import framework.RpcResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RPC框架的精髓在于动态代理和反射，通过它们使得远程调用“本地化”，对用户透明且友好
 * RPC远程调用像本地调用一样干净简洁，但其他方式对代码的侵入性就比较强；
 * 一般使用RPC框架实现远程通信效率比其他方式效率要高一些。
 */

public class HttpServletHandler {

    public void handler(HttpServletRequest req, HttpServletResponse resp){
        try {
            InputStream inputStream = req.getInputStream();
            OutputStream outputStream =resp.getOutputStream();
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            RpcRequest invocation = (RpcRequest) ois.readObject();

            // 从注册中心根据接口，找接口的实现类
            String interFaceName = invocation.getInterfaceName();
            Class impClass = Class.forName(invocation.getImpl());

            Method method = impClass.getMethod(invocation.getMethodName(),invocation.getParamTypes());
            Object result = method.invoke(impClass.newInstance(),invocation.getParams());

            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setResponseId(invocation.getRequestId());
            rpcResponse.setData(result);
            IOUtils.write(toByteArray(rpcResponse),outputStream);

        } catch (IllegalAccessException e) {
            e.printStackTrace ();
        } catch (InvocationTargetException e) {
            e.printStackTrace ();
        } catch (InstantiationException e) {
            e.printStackTrace ();
        } catch (NoSuchMethodException e) {
            e.printStackTrace ();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
}
