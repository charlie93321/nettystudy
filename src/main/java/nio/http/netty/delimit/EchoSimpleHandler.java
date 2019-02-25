package nio.http.netty.delimit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.jupiter.api.condition.EnabledOnJre;

/**
 * @AUTHOR: 小于
 * @DATE: [2019/2/24  22:45]
 * @DESC:
 */
public class EchoSimpleHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext context, String message) throws Exception {
           System.out.println("接收到客户端数据"+message);
           context.writeAndFlush(Unpooled.copiedBuffer(
                   ("服务端=>客户端我已接收到数据["+message+"]"+"$_$").getBytes("utf-8")));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exception upon server ==> ");
        cause.printStackTrace();
        ctx.channel().close();
    }
}
