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
public class Flag extends SubCmdRegion {
    
    public Flag() {
        super("flag");
    }
    
    @Override
    public String getArgs() {
        return "<region> <flag> <value>";
    }
    
    @Override
    public String getHelp() {
        return "Set a flag value to a region";
    }
    
    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 3) {
            showUsage(p);
            return;
        }
        String nome = args[0];
        String flagarg = args[1];
        String valuearg = args[2];
        
        RegionWorldManager m = getWorld(p);
        if (!m.hasRegion(nome)) {
            p.sendMessage(Txt.f("§cThere are no region with this name."));
            return;
        }
        Region r = m.getRegionByName(nome);
        EnumFlags flag = null;
        for (EnumFlags flage : EnumFlags.values()) {
            String en = flage.name();
            if (en.equalsIgnoreCase(flagarg) || flage.getCommandName().equalsIgnoreCase(flagarg)) {
                flag = flage;
            }
        }
        if (flag == null) {
            Text.Builder main = Text.of().toBuilder();
            boolean first = true;
            for (EnumFlags flage : EnumFlags.values()) {
                if (!first) {
                    main.append(Txt.f("§c, "));
                }
                first = false;
                Text toappend = Txt.f("§7" + flage.getCommandName()).toBuilder().onHover(TextActions.showText(Txt.f("§f" + flage.getHelp()))).build();
                main.append(toappend);
                
            }
            p.sendMessage(Txt.f("§cInvalid flag!"));
            p.sendMessage(main.build());
            return;
        }
        FlagValue value = null;
        if (valuearg.equalsIgnoreCase("true") || valuearg.equalsIgnoreCase("allow")) {
            value = FlagValue.ALLOW;
        }
        if (valuearg.equalsIgnoreCase("false") || valuearg.equalsIgnoreCase("deny")) {
            value = FlagValue.DENY;
        }
        if (valuearg.equalsIgnoreCase("null") || valuearg.equalsIgnoreCase("none")) {
            value = FlagValue.NONE;
        }
        if (value == null) {
            p.sendMessage(Txt.f("§cInvalid value, only accepts allow/none/deny !"));
            return;
        }
        r.setFlag(flag, value);
        r.save();
        p.sendMessage(Txt.f("§cFlag " + flag.getCommandName() + " setted to " + value.name().toLowerCase() + "!"));
    }
    
}
