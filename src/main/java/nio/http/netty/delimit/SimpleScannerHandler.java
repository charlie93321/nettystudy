package nio.http.netty.delimit;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * @AUTHOR: 小于
 * @DATE: [2019/2/24  23:23]
 * @DESC:
 */
public class SimpleScannerHandler {
    public void handle(ChannelFuture channelFuture, EventLoopGroup group) throws UnsupportedEncodingException {
         Scanner scanner=new Scanner(System.in);
         String line=null;
         while((line=scanner.nextLine())!=null){
             if("exit".equals(line)) {
                 channelFuture.channel().close();
                 group.shutdownGracefully();
                 break;
             }
             channelFuture.channel().writeAndFlush
                     (Unpooled.copiedBuffer((line).getBytes("utf-8")));

         }
         System.out.println("客户端结束...");
    }
}
