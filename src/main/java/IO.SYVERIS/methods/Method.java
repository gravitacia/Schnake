package IO.SYVERIS.methods;

import IO.SYVERIS.utils.ProxyLoader;
import io.netty.channel.Channel;
import java.util.function.BiConsumer;

public interface Method extends BiConsumer<Channel, ProxyLoader.Proxy> {
}
