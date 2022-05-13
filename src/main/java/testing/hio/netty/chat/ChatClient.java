package testing.hio.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        pipeline.addLast(new ChatClientHandler());

                    }
                });


        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            // 得到当前连接对应的channel
            Channel channel = channelFuture.channel();
            System.out.println("client已经连接成功:" + channel.localAddress());

            // 创建一个扫描器，客户端需要能输入信息
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                /**
                 * 将获取到的内容，写入到自己的channnel发给服务端
                 */
                channel.writeAndFlush(msg);
            }

        } finally {
            worker.shutdownGracefully();
        }


    }
}
