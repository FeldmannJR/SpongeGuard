/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.sponge.guard.manager;

import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import br.com.instamc.sponge.guard.region.Region;
import br.com.instamc.sponge.guard.utils.Utils;
import com.flowpowered.math.vector.Vector3i;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
public class ConfigManager {

    String mundo;

    public ConfigManager(String mundo) {
        this.mundo = mundo;

    }
    private ArrayList<Region> cache = null;

    public ArrayList<Region> getAllRegions() {

        if (cache != null) {
            return cache;
        }
        cache = new ArrayList<Region>();
        File dir = new File(getFolder());

        if (dir.exists()) {
            File[] files = dir.listFiles();

            for (File file : files) {
                System.out.println(file.toPath().toString());
                if (file.exists()) {
                    Region r = load(file.getName().replace(".json", ""));
                    if (r != null) {

                        cache.add(r);
                    }
                }
            }

        }

        return cache;
    }

    public String getFolder() {
        return "config/spongeguard/regions/" + mundo + "";
    }

    public void delete(Region r) {
        File file = new File(getFolder() + "/" + r.getName() + ".json");
        if (file.exists()) {
            file.delete();
        }

        for (Region ra : new ArrayList<Region>(cache)) {
            if (r.getName().equalsIgnoreCase(ra.getName())) {
                cache.remove(ra);
            }
        }
    }

    public void save(Region r) {
        String name = r.getName();

        File file = new File(getFolder() + "/" + name + ".json");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ConfigurationLoader<ConfigurationNode> loader = GsonConfigurationLoader.builder().setFile(file).build();

        ConfigurationNode root;

        try {
            root = loader.load();
        } catch (IOException e) {
            root = loader.createEmptyNode(ConfigurationOptions.defaults());
        }

        ConfigurationNode region = root.getNode(name);

        HashMap<String, Integer> pos1 = new HashMap<String, Integer>();
        pos1.put("x", r.getPos1().getX());
        pos1.put("y", r.getPos1().getY());
        pos1.put("z", r.getPos1().getZ());
        region.getNode("pos1").setValue(pos1);

        HashMap<String, Integer> pos2 = new HashMap<String, Integer>();
        pos2.put("x", r.getPos2().getX());
        pos2.put("y", r.getPos2().getY());
        pos2.put("z", r.getPos2().getZ());
        region.getNode("pos2").setValue(pos2);

        region.getNode("world").setValue(r.getWorld());
        region.getNode("priority").setValue(r.getPriority());

        HashMap<EnumFlags, String> flags = new HashMap<EnumFlags, String>();
        for (EnumFlags flag : EnumFlags.values()) {
            flags.put(flag, r.getFlag(flag).name());
        }

        ArrayList<String> owners = new ArrayList<String>();
        for (int i = 0; i < r.getOwners().size(); i++) {
            owners.add(r.getOwners().get(i).toString());
        }

        region.getNode("flags").setValue(flags);

        ArrayList<String> commands = new ArrayList<String>();
        for (int i = 0; i < r.getCommands().size(); i++) {
            commands.add(r.getCommands().get(i).toString());
        }

        region.getNode("commands").setValue(commands);

        try {
            loader.save(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Region ra : new ArrayList<Region>(cache)) {
            if (ra.getName().equalsIgnoreCase(r.getName())) {
                cache.remove(ra);
            }
        }
        cache.add(r);
    }

    public Region load(String name) {
        File file = new File(getFolder() + "/" + name + ".json");
        if (!file.exists()) {
            return null;
        }

        ConfigurationLoader<ConfigurationNode> loader = GsonConfigurationLoader.builder().setFile(file).build();

        ConfigurationNode root;

        try {
            root = loader.load();
        } catch (IOException e) {
            root = loader.createEmptyNode(ConfigurationOptions.defaults());
        }

        ConfigurationNode region = root.getNode(name);

        Vector3i pos1 = new Vector3i(region.getNode("pos1").getNode("x").getInt(),
                region.getNode("pos1").getNode("y").getInt(), region.getNode("pos1").getNode("z").getInt());

        Vector3i pos2 = new Vector3i(region.getNode("pos2").getNode("x").getInt(),
                region.getNode("pos2").getNode("y").getInt(), region.getNode("pos2").getNode("z").getInt());

        String w = region.getNode("world").getString();

        Region r = new Region(name, pos1, pos2, null, w);

        r.setPriority(region.getNode("priority").getInt());

        for (EnumFlags fl : EnumFlags.values()) {
            FlagValue val = FlagValue.NONE;
            try {
                if (region.getNode("flags").getNode(fl.name()) != null && region.getNode("flags").getNode(fl.name()).getString() != null&& !region.getNode("flags").getNode(fl.name()).getString().isEmpty()) {

                    val = FlagValue.valueOf(region.getNode("flags").getNode(fl.name()).getString());
                }else{
                    if(name.endsWith("__global__")){
                        val = fl.def?FlagValue.ALLOW:FlagValue.DENY;
                    }
                }
            } catch (IllegalArgumentException ex) {

            }
            r.setFlag(fl, val);
        }
        region.getNode("flags");

        ArrayList<String> commands = new ArrayList<String>();
        for (int i = 0; i < region.getNode("commands").getChildrenList().size(); i++) {
            String s = region.getNode("commands").getChildrenList().get(i).getString();
            commands.add(s);
        }

        r.setCommands(commands);

        return r;
    }

}
