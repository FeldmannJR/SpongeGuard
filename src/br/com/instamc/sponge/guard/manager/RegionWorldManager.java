/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.sponge.guard.manager;

import br.com.instamc.sponge.guard.manager.ConfigManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import br.com.instamc.sponge.guard.region.Region;
import br.com.instamc.sponge.guard.utils.Utils;
import com.flowpowered.math.vector.Vector3i;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 *
 * @author Carlos
 */
public class RegionWorldManager {

    String mundo;
    private ConfigManager config;

    public RegionWorldManager(String mundo) {
        this.mundo = mundo;
        config = new ConfigManager(mundo);
        System.out.println(config.getFolder());

        config.getAllRegions();

    }
    /*
     */

    public boolean byPass(Player p) {
        if (p.hasPermission("instamc.region.bypass")) {
            return true;
        }
        return false;
    }

    public ArrayList<Region> getRegionsAt(Location<World> location) {
        return getRegionsAt(location, false);
    }

    public ArrayList<Region> getRegionsAt(Location<World> location, boolean includeglobal) {
        ArrayList<Region> reg = new ArrayList<>();
        for (Region r : getAllRegions()) {
            if (r.isGlobal() && !includeglobal) {
                continue;
            }
            if (r.isInRegion(location) || r.isGlobal()) {
                reg.add(r);
            }
        }
        Collections.sort(reg, new Comparator<Region>() {

            @Override
            public int compare(Region o1, Region o2) {
                if (o1.isGlobal()) {
                    return 1;
                }
                if (o2.isGlobal()) {
                    return -1;
                }

                if (o1.getPriority() > o2.getPriority()) {
                    return -1;
                }
                if (o2.getPriority() > o1.getPriority()) {
                    return 1;
                }

                return 0;
            }
        });
        return reg;

    }

    public Region getGlobalRegion() {
        if (getRegionByName("__global__") == null) {
            Region r = new Region("__global__", new Vector3i(0, -1, 0), new Vector3i(0, -1, 0), DimensionTypes.OVERWORLD, mundo);
            for (EnumFlags flag : EnumFlags.values()) {
                r.setFlag(flag, flag.def ? FlagValue.ALLOW : FlagValue.DENY);
            }
            r.save();
        }
        return getRegionByName("__global__");

    }

    public FlagValue getFlag(EnumFlags flag, Location<World> location) {

        boolean b = true;
        if (flag == EnumFlags.PLACE || flag == EnumFlags.BREAK) {
            b = false;
        }
        for (Region r : getRegionsAt(location, b)) {
            if (r.getFlag(flag) != FlagValue.NONE) {
                return r.getFlag(flag);
            }
        }
        if (b == false) {
            return FlagValue.NONE;
        }
        return flag.def ? FlagValue.ALLOW : FlagValue.DENY;
    }

    public Region getRegionByName(String name) {
        for (Region r : getAllRegions()) {
            if (r.getName().equalsIgnoreCase(name)) {
                return r;
            }
        }
        return null;
    }

    public boolean hasRegion(String nome) {
        return getRegionByName(nome) != null;
    }

    public Region getHighestPriority(Location<World> loc) {
        return getHighestPriority(loc, false);
    }

    public Region getHighestPriority(Location<World> loc, boolean includeglobal) {
        ArrayList<Region> re = getRegionsAt(loc, includeglobal);
        if (re.isEmpty()) {
            return null;
        }
        return re.get(0);
    }

    public List<Region> getAllRegions() {
        return config.getAllRegions();
    }

    public void deleteRegion(Region region) {
        config.delete(region);

    }

    public void saveRegion(Region re) {
        config.save(re);
    }

    

}
