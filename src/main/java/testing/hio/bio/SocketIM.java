package testing.hio.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Socket多线程实现服务端与多个客户端通信以及客户端之间的通信
 *
 * 可以查看netstat -nat | grep ESTABLISHED
 * ESTABLISHED的意思是建立连接。表示两台机器正在通信。
 */
public class SocketIM {


    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("服务端已经启动:" + server.getLocalPort());
        //记录客户端的数量
        AtomicInteger count = new AtomicInteger(0);
        Map<String, Socket> onlineClients = new ConcurrentHashMap<>();

        while (true) {
            System.out.println("正在监听：");
            Socket client = server.accept(); //客户端也是个socket
            System.out.println("连接成功: client\t" + client.getInetAddress().getHostAddress() + "(" + client.getPort() + ")");

            String clientAddress = String.format("%s:%s", client.getInetAddress().getHostAddress(), client.getPort());
            count.incrementAndGet();//统计客户端的数量
            System.out.println("客户端的数量：" + count);
            onlineClients.put(clientAddress, client);

            System.out.println("打印在线客户端:" + String.join(",", onlineClients.keySet()));


            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        String clientAddress = String.format("%s:%s", client.getInetAddress().getHostAddress(), client.getPort());
                        InputStream in = client.getInputStream();

                        InputStreamReader inputStreamReader = new InputStreamReader(in);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            System.out.println("客户端[" + client.getInetAddress().getHostAddress() + ":" + client.getPort() + "]输出:" + line);

                            /**
                             * 给所有客户端发广播消息
                             */
                            if (line.split(" ")[0].equalsIgnoreCase("allto")) {
                                String content = line.split(" ")[1];
                                String sendContent = clientAddress + ": " + content;

                                List<String> receives = new ArrayList<>();

                                for (Map.Entry<String, Socket> online : onlineClients.entrySet()) {

                                    /**
                                     * 去掉自己
                                     */
                                    if (!clientAddress.equalsIgnoreCase(online.getKey())) {
                                        OutputStream ops = online.getValue().getOutputStream();
                                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(ops, Charset.forName("utf-8")));
                                        bufferedWriter.write(sendContent);
                                        bufferedWriter.flush();
                                        receives.add(online.getKey());
                                    }
                                }


                                String ackContent = "ack[" + String.join(",", receives) + "]" + ": " + content;
                                OutputStream self = client.getOutputStream();
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(self, Charset.forName("utf-8")));
                                bufferedWriter.write(ackContent);
                                bufferedWriter.flush();

                            } else if (line.split(" ")[0].equalsIgnoreCase("pTo")) {
                                /**
                                 * 私聊： pto clientAddress 你好
                                 */
                                String to = line.split(" ")[1];
                                String content = line.split(" ")[2];
                                String sendContent = clientAddress + ": " + content;
                                List<String> receives = new ArrayList<>();

                                Socket online = onlineClients.get(to);
                                if (online != null) {
                                    OutputStream ops = online.getOutputStream();
                                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(ops, Charset.forName("utf-8")));
                                    bufferedWriter.write(sendContent);
                                    bufferedWriter.flush();
                                    receives.add(to);
                                }

                                String ackContent = "ack[" + String.join(",", receives) + "]" + ": " + content;
                                OutputStream self = client.getOutputStream();
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(self, Charset.forName("utf-8")));
                                bufferedWriter.write(ackContent);
                                bufferedWriter.flush();

                            }
                        }
                        System.out.println("客户端断开\t" + clientAddress);
                        onlineClients.remove(clientAddress);
                        count.decrementAndGet();
                        client.close();


                    } catch (Exception ex) {

                        System.out.println("接收客户端数据异常 \t" + ex.getMessage());
                        ex.printStackTrace();
                    }


                }
            }).start();


        }

    }
}
