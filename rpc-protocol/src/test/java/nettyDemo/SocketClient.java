package nettyDemo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    private static int port = 4343; //端口号

    public static void main(String[] args) {

        while (true) {
            try {
                Socket socket = new Socket ( InetAddress.getLocalHost (), port );
                BufferedReader bufferedReader = new BufferedReader ( new InputStreamReader ( socket.getInputStream () ) );
               // bufferedReader.lines ().forEach ( s -> System.out.println ( "接受消息" + s ) );
            } catch (UnknownHostException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }

    }
}
