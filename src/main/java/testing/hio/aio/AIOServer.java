package testing.hio.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOServer {


    public static void main(String[] args) throws IOException {

        /**
         * 打开异步服务器套接字通道，将通道放入默认通道组管理
         */
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();

        /**
         * 将通道的套接字绑定到本地地址，并将套接字配置为侦听连接，backlog为挂起连接的最大数目
         */
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8080));


        System.out.println("AIO 已经启动");
        /**
         * 不阻塞
         * 接受连接，attachment表示要附加到I/O操作的对象，可以是null ，handler是用于使用结果的处理程序，即回调函数
         */
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel client, Object attachment) {

                // accept the next connection
                serverSocketChannel.accept(null, this);


                try {
                    System.out.println(client.getRemoteAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {

                            attachment.flip();
                            System.out.println("收到客户端：" + new String(attachment.array(), 0, result));
                            client.write(ByteBuffer.wrap("hello client".getBytes()));

                            attachment.clear();
                            /**
                             * 读取下一次数据
                             */
                            client.read(buffer, buffer, this);

                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            exc.printStackTrace();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }

        });

        while (true) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
