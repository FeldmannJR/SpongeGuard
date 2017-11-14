package br.com.instamc.sponge.guard.region;

import br.com.instamc.sponge.guard.SpongeGuard;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import br.com.instamc.sponge.guard.utils.Utils;
import com.flowpowered.math.vector.Vector3i;
import java.util.List;
import java.util.Optional;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class Region {

    private Vector3i pos1;
    private Vector3i pos2;
    private DimensionType dimension;
    private String world;
    private String name;
    private int priority;
    private List<UUID> owners;
    private GameMode gamemode;

    private List<String> commands;
    private HashMap<EnumFlags, FlagValue> flags = new HashMap<>();

    public Region(String name, Vector3i p1, Vector3i p2, DimensionType d, String w) {
        this.pos1 = p1;
        this.pos2 = p2;
        this.dimension = d;
        this.world = w;
        this.name = name.toLowerCase();
        priority = 0;
        owners = new ArrayList<UUID>();
        commands = new ArrayList<String>();
        gamemode = GameModes.NOT_SET;

        initFlags();
    }

    public boolean isGlobal() {
        return name.equals("__global__");
    }

    public void setGameMode(String value) {
        if (value.equalsIgnoreCase("adventure")) {
            this.gamemode = GameModes.ADVENTURE;
        } else if (value.equalsIgnoreCase("creative")) {
            this.gamemode = GameModes.CREATIVE;
        } else if (value.equalsIgnoreCase("spectator")) {
            this.gamemode = GameModes.SPECTATOR;
        } else if (value.equalsIgnoreCase("survival")) {
            this.gamemode = GameModes.SURVIVAL;
        } else {
            this.gamemode = GameModes.NOT_SET;
        }
    }

    public GameMode getGameMode() {
        return this.gamemode;
    }

    public void setWorld(String s) {
        this.world = s;
    }

    public void save() {
        SpongeGuard.getManager().getManager(world).saveRegion(this);

    }

    public World getWorldObject() {
        Optional<World> w = Sponge.getServer().getWorld(world);
        return w.orElse(null);
    }

    public String getWorld() {
        return this.world;
    }

    public Vector3i getPos1() {
        return pos1;
    }

    public Vector3i getPos2() {
        return pos2;
    }

    public void setPos1(Vector3i pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Vector3i pos2) {
        this.pos2 = pos2;
    }

    public void setDimension(DimensionType d) {
        this.dimension = d;
    }

    public DimensionType getDimension() {
        return this.dimension;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public void addCommand(String s) {
        if (!this.commands.contains(s)) {
            commands.add(s);
        }
    }

    public void removeCommand(String s) {
        if (this.commands.contains(s)) {
            commands.remove(s);
        }
    }

    public void setCommands(List<String> s) {
        this.commands = s;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public void addOwner(UUID p) {
        if (!this.owners.contains(p)) {
            owners.add(p);
        }
    }

    public void removeOwner(UUID p) {
        if (this.owners.contains(p)) {
            this.owners.remove(p);
        }
    }

    public void setOwners(List<UUID> p) {
        this.owners = p;
    }

    public List<UUID> getOwners() {
        return this.owners;
    }

    public boolean isOwner(UUID p) {
        return this.owners.contains(p);
    }

    public void setPriority(int p) {
        this.priority = p;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setFlag(EnumFlags flag, FlagValue value) {
        flags.put(flag, value);
    }

    public FlagValue getFlag(EnumFlags flag) {
        if (flags.containsKey(flag)) {
            return flags.get(flag);
        }
        return FlagValue.NONE;
    }

    public void initFlags() {
        for (EnumFlags flag : EnumFlags.values()) {
            setFlag(flag, FlagValue.NONE);
        }
    }
    public List<Player> getPlayers(){
        ArrayList<Player> pl = new ArrayList();
        for(Player p : Sponge.getServer().getOnlinePlayers()){
            if(isInRegion(p.getLocation())){
                pl.add(p);
            }
        }
        return pl;
        
    }

    public boolean isInRegion(Location<World> l) {

        Vector3i posr1 = getPos1();
        Vector3i posr2 = getPos2();

        int x1 = Math.min(posr1.getX(), posr2.getX());
        int y1 = Math.min(posr1.getY(), posr2.getY());
        int z1 = Math.min(posr1.getZ(), posr2.getZ());
        int x2 = Math.max(posr1.getX(), posr2.getX());
        int y2 = Math.max(posr1.getY(), posr2.getY());
        int z2 = Math.max(posr1.getZ(), posr2.getZ());

        if (getWorld().equalsIgnoreCase(l.getExtent().getName())) {
            if (isGlobal()) {
                return true;
            }
            if (((l.getBlockX() >= x1 && l.getBlockX() <= x2)
                    && (l.getBlockY() >= y1 && l.getBlockY() <= y2)
                    && (l.getBlockZ() >= z1 && l.getBlockZ() <= z2))) {
                return true;
            }
        }
        return false;
    }

    public void delete() {
        SpongeGuard.getManager().getManager(world).deleteRegion(this);

    }

}
