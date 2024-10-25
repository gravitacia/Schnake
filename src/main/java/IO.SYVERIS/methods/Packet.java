package IO.SYVERIS.methods;

import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.util.internal.ThreadLocalRandom;

public class Packet implements Method {
   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      byte[] bytes = new byte[5 + ThreadLocalRandom.current().nextInt(65534)];
      ThreadLocalRandom.current().nextBytes(bytes);
      channel.writeAndFlush(Unpooled.buffer().writeBytes(bytes));
      if (ThreadLocalRandom.current().nextBoolean()) {
         channel.config().setOption(ChannelOption.SO_LINGER, 1);
      }

      channel.close();
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
   }
}
