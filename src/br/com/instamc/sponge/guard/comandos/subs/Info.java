/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.sponge.guard.comandos.subs;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.comandos.SubCmdRegion;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import br.com.instamc.sponge.guard.region.Region;
import br.com.instamc.sponge.library.utils.Txt;
import com.sk89q.worldedit.IncompleteRegionException;

import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.session.SessionOwner;
import com.sk89q.worldedit.sponge.SpongeWorldEdit;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 *
 * @author Carlos
 */
public class Info extends SubCmdRegion {

    public Info() {
        super("info");
    }

    @Override
    public String getArgs() {
        return "[name]";
    }

    @Override
    public String getHelp() {
        return "List regions in current world";
    }

    @Override
    public void execute(Player p, String[] args) {

        RegionWorldManager m = getWorld(p);
        Region r = m.getHighestPriority(p.getLocation(), true);
        if (args.length == 1) {
            String nome = args[0];
            if (m.hasRegion(nome)) {
                r = m.getRegionByName(nome);
            } else {
                p.sendMessage(Txt.f("§cThere are no regions with this name."));
                return;
            }
        }
        
        showInfo(p, r);

    }

    public void showInfo(Player p, Region r) {
        p.sendMessage(Txt.f("§e=-=-=-=-= §f" + r.getName() + " §e=-=-=-=-="));
        p.sendMessage(Txt.f("§eBounds: §f " + r.getPos1().getX() + ", " + r.getPos1().getY() + ", " + r.getPos1().getZ() + " -> " + r.getPos2().getX() + ", " + r.getPos2().getY() + ", " + r.getPos2().getZ()));
        p.sendMessage(Txt.f("§ePriority: §f" + r.getPriority()));
        String bloqued = "";
        for (String s : r.getCommands()) {
            if (!bloqued.isEmpty()) {
                bloqued += ", ";
            }
            bloqued += s;
        }
        p.sendMessage(Txt.f("§eBlocked Cmds: §f" + bloqued));
        Text.Builder flags = Txt.f("§eFlags: ").toBuilder();
        boolean first = true;
        for (EnumFlags flage : EnumFlags.values()) {
            if (r.getFlag(flage) == FlagValue.NONE) {
                continue;
            }
            if (!first) {
                flags.append(Txt.f("§f, "));
            }
            String color = "§c";
            if (r.getFlag(flage) == FlagValue.ALLOW) {
                color = "§a";
            }
            first = false;

            Text toappend = Txt.f(color + flage.getCommandName()).toBuilder().onHover(TextActions.showText(Txt.f("§f" + flage.getHelp()))).onClick(TextActions.suggestCommand("/rg flag "+r.getName()+" "+flage.getCommandName()+" <ALLOW/DENY/NONE>")).build();
            flags.append(toappend);

        }
        p.sendMessage(flags.build());
    }

}
