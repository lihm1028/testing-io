package testing.hio.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketIOServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 8080));


        while (!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                handle(socket);
            }).start();

        }

    }


    static void handle(Socket socket) {

        try {

            byte[] bytes = new byte[1024];
            int len;
            while (true) {
                if ((len = socket.getInputStream().read(bytes)) != -1) {
                    String content = new String(bytes, 0, len);
                    System.out.println("收到[客户端]" + socket.getRemoteSocketAddress() + "信息：" + content);

                    // 在重新写会客户端
                    content = "[ack]" + content;
                    socket.getOutputStream().write(content.getBytes());
                    socket.getOutputStream().flush();
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
