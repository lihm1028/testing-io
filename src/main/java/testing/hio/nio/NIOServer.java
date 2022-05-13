package testing.hio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 可以查看
 * netstat -nat | grep ESTABLISHED
 * * ESTABLISHED的意思是建立连接。表示两台机器正在通信。
 * https://www.cnblogs.com/nyhhd/p/14527636.html
 * https://www.likecs.com/show-205226665.html
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {


        /**
         * 1.创建一个ServerSocketChannel 通道===》ServerSocket
         */
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 绑定一个端口6666在服务器短监听
        serverSocket.bind(new InetSocketAddress(6666));

        // 设置为IO非阻塞，默认是阻塞的
        serverSocketChannel.configureBlocking(false);

        // 打开一个选择器Selector
        Selector selector = Selector.open();

        /**
         * 把serverSocketChannel 注册到select，关心的事件是接收连接事件
         *
         * SelectionKey中定义的4种事件
         *
         * SelectionKey.OP_ACCEPT —— 接收连接进行事件，表示服务器监听到了客户连接，那么服务器可以接收这个连接了
         * SelectionKey.OP_CONNECT —— 连接就绪事件，表示客户与服务器的连接已经建立成功
         * SelectionKey.OP_READ  —— 读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了（通道目前有数据，可以进行读操作了）
         * SelectionKey.OP_WRITE —— 写就绪事件，表示已经可以向通道写数据了（通道目前可以用于写操作）
         *
         */
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("注册后的selectKey数量:" + selector.keys().size());
        while (!Thread.currentThread().isInterrupted()) {

            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys 数量 = " + selectionKeys.size());
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();

                if (!key.isValid()) {
                    continue;
                }
                //根据key 对应的通道发生的事件做相应处理,
                if (key.isAcceptable()) {//如果是 OP_ACCEPT, 有新的客户端连接

                    //⑥ 该客户端生成一个 SocketChannel
                    ServerSocketChannel socketChannel =(ServerSocketChannel) key.channel();

                    SocketChannel clientChanel = socketChannel.accept();

                    System.out.println("客户端连接成功 生成了一个 socketChannel " + clientChanel.hashCode());
                    clientChanel.configureBlocking(false);
                    //将socketChannel 注册到selector, 关注事件为 OP_READ， 同时给socketChannel
                    //关联一个Buffer
                    clientChanel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后 ，注册的selectionKeys 数量=" + selector.keys().size());
                }
                if (key.isReadable()) {  //发生 OP_READ

                    //通过key 反向获取到对应channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int len = channel.read(buffer);
                    buffer.flip();

                    byte[] bytes = new byte[len];
                    buffer.get(bytes, 0, len);
                    System.out.println("form 客户端: " + new String(bytes, StandardCharsets.UTF_8));

                }
                if (key.isWritable()) {


                }

                //手动从集合中移动当前的selectionKey, 防止重复操作
                keyIterator.remove();


            }


        }


    }


}
