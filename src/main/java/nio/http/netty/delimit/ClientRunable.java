package nio.http.netty.delimit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import scala.util.control.Exception;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @AUTHOR: 小于
 * @DATE: [2019/2/24  22:57]
 * @DESC:
 */
public class ClientRunable implements Runnable {

    private Bootstrap bootstrap=null;
    private EventLoopGroup group=null;
    private int port;
    private SimpleScannerHandler handler=new SimpleScannerHandler();

    public ClientRunable(int port){
        init();
        this.port=port;
    }

    private void init() {
        bootstrap=new Bootstrap();
        group=new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);

    }

    @Override
    public void run() {
         bootstrap.handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel sc) throws  UnsupportedEncodingException {
                 sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,
                         Unpooled.copiedBuffer("$_$".getBytes("utf-8"))))
                 .addLast(new StringDecoder(Charset.forName("utf-8")))
                 .addLast(new ClientJustReadHandler());
             }
         });
        ChannelFuture channelFuture=null;
        try {
             channelFuture=bootstrap.connect("localhost",this.port).sync();
              System.out.println("客户端启动。。。");
            handler.handle(channelFuture,group);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {
            if(null!=channelFuture) {
                try {
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(null!=group)group.shutdownGracefully();
        }

    }



      public static void main(String[] args){
                 ClientRunable client=new ClientRunable(9999);

                 new Thread(client).start();

      }

}
