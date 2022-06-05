package testing.hio.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可以查看netstat -nat | grep ESTABLISHED
 * ESTABLISHED的意思是建立连接。表示两台机器正在通信。
 */
public class SocketIO1 {


    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("服务端已经启动:" + server.getLocalPort());
        //记录客户端的数量
        AtomicInteger count = new AtomicInteger(0);
        while (true) {
            System.out.println("正在监听：");
            Socket client = server.accept(); //客户端也是个socket
            System.out.println("连接成功: client\t" + client.getInetAddress().getHostAddress() + "(" + client.getPort() + ")");
            count.incrementAndGet();//统计客户端的数量
            System.out.println("客户端的数量：" + count);

            new Thread();
            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        InputStream in = client.getInputStream();

                        InputStreamReader inputStreamReader = new InputStreamReader(in);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            System.out.println("客户端[" + client.getInetAddress().getHostAddress() + ":" + client.getPort() + "]输出:" + line);

                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), Charset.forName("utf-8")));
                            String content = String.format("服务端已收到：%s \n", line);
                            bufferedWriter.write(content);
                            bufferedWriter.flush();
                        }

                        System.out.println("客户端断开\t" + client.getPort());
                        client.close();
                        count.decrementAndGet();

                    } catch (Exception ex) {

                        System.out.println("接收客户端数据异常 \t" + ex.getMessage());
                        ex.printStackTrace();
                    }


                }
            }).start();


        }

    }
}
