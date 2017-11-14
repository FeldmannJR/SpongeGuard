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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 *
 * @author Carlos
 */
public class Rename extends SubCmdRegion {

    public Rename() {
        super("rename");
    }

    @Override
    public String getArgs() {
        return "<region> <newname>";
    }

    @Override
    public String getHelp() {
        return "Set a region name";
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 2) {
            showUsage(p);
            return;
        }
        String nome = args[0];
        String newname = args[1];

        RegionWorldManager m = getWorld(p);

        if (!m.hasRegion(nome)) {
            p.sendMessage(Txt.f("§cThere is no region with this name."));
            return;
        }
        if (m.hasRegion(newname)) {
            p.sendMessage(Txt.f("§cAlready has a region with this name."));
            return;
        }
        Region r = m.getRegionByName(nome);
        r.delete();
        r.setName(newname);
        r.save();

        p.sendMessage(Txt.f("§cRegion renamed to " + newname + " !"));

    }

}
