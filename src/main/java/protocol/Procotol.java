package protocol;

import framework.RpcRequest;

import java.net.URL;

public interface Procotol {

    void start(URL url);

    Object send(URL url, RpcRequest invocation);
}
