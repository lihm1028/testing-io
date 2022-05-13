package testing.hio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;


public class NettyWithServer {


    public static void main(String[] args) {
        new NettyServer(8080).serverStart();
    }


    static class NettyServer {

        int port = 8080;

        public NettyServer(int port) {
            this.port = port;
        }

        /**
         * 初始化并启动 Netty 服务端示例代码如下：
         */
        public void serverStart() {
            // 创建mainReactor
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            // 创建工作线程组
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)     // 组装NioEventLoopGroup
                    .channel(NioServerSocketChannel.class) // 设置channel类型为NIO类型
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 配置入站、出站事件handler
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new Handler());// 配置入站、出站事件channel
                        }
                    });

            try {
                // 绑定端口
                ChannelFuture f = b.bind(port).sync();

                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }

    }

    static class Handler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //super.channelRead(ctx, msg);
            System.out.println("server: channel read");
            ByteBuf buf = (ByteBuf) msg;

            System.out.println(buf.toString(CharsetUtil.UTF_8));

            ctx.writeAndFlush(msg);

            ctx.close();

            //buf.release();
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            //super.exceptionCaught(ctx, cause);
            cause.printStackTrace();
            ctx.close();
        }
    }
}
