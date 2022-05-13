package testing.hio.bio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 8080));


        String content = String.format("我是:%s:%s,hello server", socket.getLocalAddress(), socket.getLocalPort());
        socket.getOutputStream().write(content.getBytes());
        socket.getOutputStream().flush();
        System.out.println("waiting for msg back..");

//        byte[] bytes = new byte[1024];
//        int len=socket.getInputStream().read(bytes);
//        System.out.println("server:"+new String(bytes, 0, len));



        byte[] bytes = new byte[1024];
        int len;
        while (true) {
            if ((len = socket.getInputStream().read(bytes)) != -1) {
                System.out.println("server:"+new String(bytes, 0, len));
            }
        }


    }

}
