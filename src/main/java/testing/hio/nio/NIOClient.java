package testing.hio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666));

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        Scanner scanner = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            selector.select();

            Set<SelectionKey> selectedKeys = selector.selectedKeys();

            Iterator iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {

                SelectionKey key = (SelectionKey) iterator.next();

                if (key.isConnectable()) {

                    while (!socketChannel.finishConnect()) {
                        System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作..");
                    }

                    socketChannel.register(selector, SelectionKey.OP_WRITE);

                    System.out.println("server connected");

                    //...如果连接成功，就发送数据
                    String str = "hello, 木子~";
                    //Wraps a byte array into a buffer
                    ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
                    //发送数据，将 buffer 数据写入 channel
                    socketChannel.write(buffer);


                } else if (key.isWritable()) {

                    System.out.println("please input message");
                    String message = scanner.nextLine();

//
//                    ByteBuffer writebufBuffer = ByteBuffer.wrap(message.getBytes());
//
//                    socketChannel.write(writebufBuffer);
//
//                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (key.isReadable()) {

                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                    int readNum = socketChannel.read(readBuffer);

                    byte[] newBytes = new byte[readNum];
                    System.arraycopy(readBuffer.array(), 0, newBytes, 0, readNum);
                    String message = new String(newBytes);
                    System.out.println(message);
                    socketChannel.register(selector, SelectionKey.OP_WRITE);

                }

            }

            iterator.remove();


        }

    }
}
