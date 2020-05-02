package nettyDemo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable {


    private static int port = 4343; //端口号


    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer ();

        new Thread ( socketServer ).start ();
    }


    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket ( port );
            while (true) {
                final Socket socket = serverSocket.accept ();
                Thread sHandlerThread = new Thread ( new Runnable () {
                    @Override
                    public void run() {
                        PrintWriter printWriter = null;
                        try {
                            printWriter = new PrintWriter ( socket.getOutputStream () );
                            printWriter.println ( "hello word! " );
                            printWriter.flush ();
                        } catch (IOException e) {
                            e.printStackTrace ();
                        }
                    }
                } );
                sHandlerThread.start ();
            }
        } catch (IOException e) {
            e.printStackTrace ();
        } catch (Exception e) {
            e.printStackTrace ();
        }


    }
}
