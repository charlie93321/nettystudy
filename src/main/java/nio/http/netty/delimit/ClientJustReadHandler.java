package nio.http.netty.delimit;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @AUTHOR: 小于
 * @DATE: [2019/2/24  23:17]
 * @DESC:
 */
public class ClientJustReadHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
           System.out.println("客户端接收到数据==>|"+s);
    }
}
