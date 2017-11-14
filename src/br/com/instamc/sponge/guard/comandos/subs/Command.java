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
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 *
 * @author Carlos
 */
public class Command extends SubCmdRegion {

    public Command() {
        super("command");
    }

    @Override
    public String getArgs() {
        return "<region> <command>";
    }

    @Override
    public String getHelp() {
        return "Block or allow a command";
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 2) {
            showUsage(p);
            return;
        }
        String nome = args[0];
        String comando = args[1];
        RegionWorldManager m = getWorld(p);
        if (!m.hasRegion(nome)) {
            p.sendMessage(Txt.f("§cThere are no region with this name."));
            return;
        }
        Region r = m.getRegionByName(nome);
        if (r.getCommands().contains(comando.toLowerCase())) {
            r.getCommands().remove(comando.toLowerCase());
            p.sendMessage(Txt.f("§aCommand allowed in the region!"));
        } else {
            r.getCommands().add(comando.toLowerCase());
            p.sendMessage(Txt.f("§cCommand blocked in the region!"));

        }
        r.save();
    }

}
