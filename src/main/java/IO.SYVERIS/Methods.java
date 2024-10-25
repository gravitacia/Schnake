package IO.SYVERIS;

import IO.SYVERIS.methods.BOTNET;
import IO.SYVERIS.methods.BigHandshake;
import IO.SYVERIS.methods.BigPacket;
import IO.SYVERIS.methods.BotJoiner;
import IO.SYVERIS.methods.CHARONBOT;
import IO.SYVERIS.methods.ColorCrasher;
import IO.SYVERIS.methods.IPSpoofFLood;
import IO.SYVERIS.methods.InvalidNames;
import IO.SYVERIS.methods.Join;
import IO.SYVERIS.methods.Killer;
import IO.SYVERIS.methods.LegitJoin;
import IO.SYVERIS.methods.LegitNameKiller;
import IO.SYVERIS.methods.Localhost;
import IO.SYVERIS.methods.LoginPingMulticrasher;
import IO.SYVERIS.methods.LongNames;
import IO.SYVERIS.methods.Method;
import IO.SYVERIS.methods.NettyDowner;
import IO.SYVERIS.methods.Network;
import IO.SYVERIS.methods.NullPing;
import IO.SYVERIS.methods.Packet;
import IO.SYVERIS.methods.Ping;
import IO.SYVERIS.methods.QueryFlood;
import IO.SYVERIS.methods.RAM;
import IO.SYVERIS.methods.RandomBytes;
import IO.SYVERIS.methods.ServerFucker;
import IO.SYVERIS.methods.Spam;
import IO.SYVERIS.methods.SpamJoin;
import IO.SYVERIS.methods.TCPBYPASS;
import IO.SYVERIS.methods.TCPHIT;
import IO.SYVERIS.methods.UltimateSmasher;
import IO.SYVERIS.methods.YooniksCry;
import IO.SYVERIS.methods.nAntiBotCry;
import IO.SYVERIS.methods.queue;
import IO.SYVERIS.utils.RandomUtils;
import java.util.HashMap;

public class Methods {
   public static final HashMap<String, Method> METHODS = new HashMap();

   public static Method getByID(int i) {
      return (Method)METHODS.getOrDefault(i, (c, p) -> {
         c.close();
         System.err.println("Invalid method id: " + i);
      });
   }

   private static void registerMethod(String name, Method m) {
      if (METHODS.containsKey(name)) {
         throw new IllegalStateException("Method with id " + name + " is already existing.");
      } else {
         METHODS.put(name, m);
      }
   }

   public static void setupMethods() {
      YooniksCry.bert = RandomUtils.randomUTF16String1(600000);
      registerMethod("join", new Join());
      registerMethod("legitjoin", new LegitJoin());
      registerMethod("localhost", new Localhost());
      registerMethod("invalidnames", new InvalidNames());
      registerMethod("longnames", new LongNames());
      registerMethod("botjoiner", new BotJoiner());
      registerMethod("power", new LegitNameKiller());
      registerMethod("spoof", new IPSpoofFLood());
      registerMethod("ping", new Ping());
      registerMethod("spam", new Spam());
      registerMethod("killer", new Killer());
      registerMethod("nullping", new NullPing());
      registerMethod("charonbot", new CHARONBOT());
      registerMethod("multikiller", new LoginPingMulticrasher());
      registerMethod("packet", new Packet());
      registerMethod("handshake", new BigHandshake());
      registerMethod("bighandshake", new NullPing());
      registerMethod("query", new QueryFlood());
      registerMethod("bigpacket", new BigPacket());
      registerMethod("network", new Network());
      registerMethod("randombytes", new RandomBytes());
      registerMethod("spamjoin", new SpamJoin());
      registerMethod("nettydowner", new NettyDowner());
      registerMethod("ram", new RAM());
      registerMethod("yoonikscry", new YooniksCry());
      registerMethod("colorcrasher", new ColorCrasher());
      registerMethod("tcphit", new TCPHIT());
      registerMethod("queue", new queue());
      registerMethod("botnet", new BOTNET());
      registerMethod("tcpbypass", new TCPBYPASS());
      registerMethod("ultimatesmasher", new UltimateSmasher());
      registerMethod("sf", new ServerFucker());
      registerMethod("nabcry", new nAntiBotCry());
   }

   public static Method getMethod(String methodID) {
      return (Method)METHODS.get(methodID);
   }
}
