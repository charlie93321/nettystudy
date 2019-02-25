package nio.http.netty.delimit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;

/**
 * @AUTHOR: 小于
 * @DATE: [2019/2/24  22:21]
 * @DESC:
 */
public class DelimitNettyServer{
    private EventLoopGroup acceptGroup=null;
    private EventLoopGroup clientGroup=null;
    private ServerBootstrap bootstrap=null;
    public DelimitNettyServer(){
           init();
    }

    private void init() {

        acceptGroup=new NioEventLoopGroup();
        clientGroup=new NioEventLoopGroup();

        bootstrap=new ServerBootstrap();

        bootstrap.group(acceptGroup,clientGroup);

        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.option(ChannelOption.SO_BACKLOG,1024);

        bootstrap.childOption(ChannelOption.SO_SNDBUF,1024*16)
        .childOption(ChannelOption.SO_RCVBUF,16*1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true);

    }

    public ChannelFuture doAccept(int port , final ChannelHandler... handlers) throws InterruptedException {
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                 socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,
                         Unpooled.copiedBuffer("$_$".getBytes("utf-8"))))
                 .addLast(new StringDecoder(Charset.forName("UTF-8")))
                 .addLast(new EchoSimpleHandler())
                 ;
            }
        });
        return  bootstrap.bind(port).sync();
    }



      public static void main(String[] args){
          DelimitNettyServer server =null;
          ChannelFuture futrue =null;
          try {
              server = new DelimitNettyServer();

              futrue = server.doAccept(9999);
         }catch (InterruptedException e) {
             e.printStackTrace();
         }finally {

              if(null!=futrue){
                  try {
                      futrue.channel().closeFuture().sync();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }

              if(null!=server){
                  server.clientGroup.shutdownGracefully();
                  server.acceptGroup.shutdownGracefully();
              }
          }
      }

}
