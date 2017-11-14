/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.sponge.guard.comandos;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionManager;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.library.utils.Txt;
import org.spongepowered.api.entity.living.player.Player;

/**
 *
 * @author Carlos
 */
public abstract class SubCmdRegion {

    String cmd;

    public SubCmdRegion(String cmd) {
        this.cmd = cmd;

    }

    public abstract String getArgs();

    public abstract String getHelp();

    public void showUsage(Player p) {
        p.sendMessage(Txt.f("§f[§4!!!§f] §cUso correto: §e/region " + cmd + " " + getArgs()));
    }

    public abstract void execute(Player p, String[] args);

    public RegionManager getManager() {
        return SpongeGuard.getManager();
    }

    public RegionWorldManager getWorld(Player p) {
        return getManager().getManager(p.getWorld());
    }
}
