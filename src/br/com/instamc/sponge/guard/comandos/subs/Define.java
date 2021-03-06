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
import com.flowpowered.math.vector.Vector3i;
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
public class Define extends SubCmdRegion {
    
    public Define() {
        super("define");
    }
    
    @Override
    public String getArgs() {
        return "<name>";
    }
    
    @Override
    public String getHelp() {
        return "Create a region with a definited name";
    }
    
    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 1) {
            showUsage(p);
            return;
        }
        String nome = args[0];
        RegionWorldManager m = getWorld(p);
        if (m.hasRegion(nome)) {
            p.sendMessage(Txt.f("§cAlredy exists a region with this name."));
            return;
        }
        SpongeWorldEdit we = SpongeGuard.getWorldEdit();
        try {
            com.sk89q.worldedit.regions.Region sele = we.getSession(p).getSelection(we.getWorld(p.getWorld()));
            Vector3i pos1 = new Vector3i( sele.getMinimumPoint().getBlockX(), sele.getMinimumPoint().getBlockY(), sele.getMinimumPoint().getBlockZ());
            Vector3i pos2 = new Vector3i( sele.getMaximumPoint().getBlockX(), sele.getMaximumPoint().getBlockY(), sele.getMaximumPoint().getBlockZ());
            
            Region r = new Region(nome, pos1, pos2, p.getWorld().getDimension().getType(), p.getWorld().getName());
            r.save();
            p.sendMessage(Txt.f("§aRegião " + nome + "§a criada!"));
        } catch (IncompleteRegionException ex) {
            p.sendMessage(Txt.f("§cSelecione uma região com o world edit!"));
        }
        ;
        
    }
    
}
