package br.com.instamc.sponge.guard.manager;

import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.world.World;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Carlos
 */
public class RegionManager {

    private HashMap<String, RegionWorldManager> managers = new HashMap<String, RegionWorldManager>();

    public RegionManager() {

    }

    public RegionWorldManager getManager(String w) {
        if (w == null) {
            return null;
        }
        if (!managers.containsKey(w)) {
             RegionWorldManager ma = new RegionWorldManager(w);
            managers.put(w, ma);
            ma.getGlobalRegion();
        }
        
        return managers.get(w);

    }
    public List<RegionWorldManager> getAllManagers(){
        ArrayList<RegionWorldManager> man = new ArrayList();
        man.addAll(managers.values());
        return man;
        
    }

    public RegionWorldManager getManager(World w) {
        if (w == null) {
            return null;
        }
        return getManager(w.getName());

    }

    @Listener
    public void load(LoadWorldEvent ev) {
        if (managers.containsKey(ev.getTargetWorld().getName())) {
            return;
        }
        RegionWorldManager ma = new RegionWorldManager(ev.getTargetWorld().getName());
        managers.put(ev.getTargetWorld().getName(), ma);
        ma.getGlobalRegion();
    }

    public void loadWorlds() {
        for (World w : Sponge.getServer().getWorlds()) {
            if (managers.containsKey(w.getName())) {
                continue;
            }
            RegionWorldManager ma = new RegionWorldManager(w.getName());
            managers.put(w.getName(), ma);
            ma.getGlobalRegion();

        }

    }

}
