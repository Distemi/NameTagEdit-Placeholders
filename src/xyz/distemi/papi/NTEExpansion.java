package xyz.distemi.papi;

import com.nametagedit.plugin.NametagEdit;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NTEExpansion extends PlaceholderExpansion {

    private NTEData api;

    @Override
    public boolean register() {
        if (!canRegister()) {
            System.out.println("Plugin NameTagEdit required for Placeholder Expansion NametagEdit");
            return false;
        }
        api = new NTEData(NametagEdit.getApi());

        return super.register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nametagedit";
    }

    @Override
    public @NotNull String getAuthor() {
        return "distemi";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "NametagEdit";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        String p = player.getName();
        String res = "";

        if (params.equalsIgnoreCase("prefix")) {
            res = api.getUser(p).prefix;
        }

        if (params.equalsIgnoreCase("suffix")) {
            res = api.getUser(p).suffix;
        }

        return ChatColor.translateAlternateColorCodes('&', res);

    }
}
