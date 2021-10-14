package xyz.distemi.papi;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.nametagedit.plugin.api.INametagApi;
import com.nametagedit.plugin.api.data.GroupData;
import com.nametagedit.plugin.api.data.Nametag;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class NTEData {

    private static LoadingCache<String, NTEUserData> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(4, TimeUnit.SECONDS)
            .maximumSize(2000)
            .concurrencyLevel(5).build(
                    new CacheLoader<String, NTEUserData>() {
                        @Override
                        public NTEUserData load(String s) {
                            return null;
                        }
                    }
            );
    private final NTEUserData empty = new NTEUserData("", "");
    private final INametagApi api;

    public NTEData(INametagApi api) {
        this.api = api;
    }

    private void check(String player) {
        if (cache.asMap().containsKey(player)) {
            return;
        }
        Player p = Bukkit.getPlayer(player);
        Nametag tag = api.getNametag(p);
        NTEUserData d = new NTEUserData(tag.getPrefix(), tag.getSuffix());
        if (!d.prefix.isEmpty() && !d.suffix.isEmpty() ) {
            for ( GroupData data : api.getGroupData() ) {
                if ( p.hasPermission(data.getPermission()) ) {
                    d = new NTEUserData(data.getPrefix(), data.getSuffix());
                    break;
                }
            }
        }
        cache.put(player, d);
    }

    public NTEUserData getUser(String player) {
        check(player);
        try {
            return cache.get(player);
        } catch (Exception ignored) {
            return empty;
        }
    }

    public static class NTEUserData {
        public String prefix;
        public String suffix;
        public NTEUserData(String prefix, String suffix) {
            this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            this.suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        }
    }
}
