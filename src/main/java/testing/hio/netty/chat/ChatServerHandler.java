package testing.hio.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * GlobalEventExecutor.INSTANCE是全局事件执行器，单例饿汉式
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "上线了");



        /**
         * 将该客户端上线信息推送给其他所有在线客户端
         * 该方法会将channelGroup中的所有channel遍历，并发送消息
         */
        String online = String.format("[客户端]%s 上线了 %s", channel.remoteAddress(), sdf.format(new Date()));
        channelGroup.writeAndFlush(online);
        //将当前客户端的channel加入到channelGroup
        channelGroup.add(channel);
        System.out.println("channelGroup 数量：" + channelGroup.size());


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "下线了");

        String offline = String.format("[客户端]%s 下线了 %s", channel.remoteAddress(), sdf.format(new Date()));
        channelGroup.writeAndFlush(offline);
        //将当前channel加入到channelGroup
        channelGroup.remove(channel);
        System.out.println("channelGroup 数量：" + channelGroup.size());


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel current = ctx.channel();
        channelGroup.forEach(ch -> {

            if (ch != current) {
                ch.writeAndFlush("[ 客户端 ]" + current.remoteAddress() + " 发送了消息：" + msg);
            } else {
                ch.writeAndFlush("[ 自己 ]发送了消息：" + msg);
            }

        });
    }


}
