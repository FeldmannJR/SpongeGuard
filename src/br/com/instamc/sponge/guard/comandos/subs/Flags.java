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
public class Flags extends SubCmdRegion {

    public Flags() {
        super("flags");
    }

    @Override
    public String getArgs() {
        return "";
    }

    @Override
    public String getHelp() {
        return "View the flag list";
    }

    @Override
    public void execute(Player p, String[] args) {

        Text.Builder main = Txt.f("§eFlags: ").toBuilder();
        boolean first = true;
        for (EnumFlags flage : EnumFlags.values()) {
            if (!first) {
                main.append(Txt.f("§c, "));
            }
            first = false;
            Text toappend = Txt.f("§e" + flage.getCommandName()).toBuilder().onHover(TextActions.showText(Txt.f("§f" + flage.getHelp()))).build();
            main.append(toappend);

        }

        p.sendMessage(main.build());
    }

}
