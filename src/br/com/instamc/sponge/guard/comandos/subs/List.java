/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.sponge.guard.comandos.subs;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.comandos.SubCmdRegion;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
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
public class List extends SubCmdRegion {

    public List() {
        super("list");
    }

    @Override
    public String getArgs() {
        return "";
    }

    @Override
    public String getHelp() {
        return "List regions in current world";
    }

    @Override
    public void execute(Player p, String[] args) {

        RegionWorldManager m = getWorld(p);
        Text.Builder main = Txt.f("§9Region List: ").toBuilder();
        boolean first = true;
        for (Region r : m.getAllRegions()) {

            if (!first) {
                main.append(Txt.f("§f, "));
            }
            first = false;
            Text toappend = Txt.f("§e" + r.getName()).toBuilder().onHover(TextActions.showText(Txt.f("§fClick here to more information."))).onClick(TextActions.runCommand("/rg info " + r.getName())).build();
            main.append(toappend);

        }
        p.sendMessage(main.build());

    }

}
