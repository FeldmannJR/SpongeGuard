package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import br.com.instamc.sponge.library.utils.Cooldown;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.complex.EnderDragon;
import org.spongepowered.api.entity.living.complex.EnderDragonPart;
import org.spongepowered.api.entity.living.monster.Enderman;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;

import br.com.instamc.sponge.library.utils.Txt;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.ExperienceOrb;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.entity.spawn.SpawnCause;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventBlockBreak {

    @Listener
    public void onBlockBreakByEntity(ChangeBlockEvent.Break event) {
         if(event.getCause().root() instanceof CommandBlock){
                return;
            }
        for (Transaction<BlockSnapshot> t : event.getTransactions()) {
            Optional<Location<World>> locatio = t.getOriginal().getLocation();
            if (!locatio.isPresent()) {
                continue;
            }
            Location<World> loc = locatio.get();
            RegionWorldManager ma = SpongeGuard.getManager().getManager(loc.getExtent());
           
            if (event.getCause().root() instanceof Enderman) {
                FlagValue fla = ma.getFlag(EnumFlags.ENDERMANGRIEF, loc);
                if (fla == FlagValue.DENY) {
                    event.setCancelled(true);
                    return;
                }
            } else if (event.getCause().root() instanceof EnderDragon || event.getCause().root() instanceof EnderDragonPart) {
                FlagValue fla = ma.getFlag(EnumFlags.ENDERDRAGON_BLOCK_DAMAGE, loc);
                if (fla == FlagValue.DENY) {
                    event.setCancelled(true);
                    return;
                }
            } else if (!(event.getCause().root() instanceof Player)) {
                FlagValue fla = ma.getFlag(EnumFlags.BUILD, loc);
                if (fla == FlagValue.DENY) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @Listener
    public void move(MoveEntityEvent ev, @First Player p) {
        if (Cooldown.isCooldown(p, "botoubloco")) {

            Vector3d from = ev.getFromTransform().getPosition();
            Vector3d to = ev.getFromTransform().getPosition();
            if (to.getY() < from.getY() && from.getX() == to.getX() && from.getZ() == to.getZ()) {
                return;
            }

            ev.setCancelled(true);

        }

    }

    public static List<Location<World>> getLocs(List<Transaction<BlockSnapshot>> blocks) {
        List<Location<World>> locs = new ArrayList();
        for (Transaction<BlockSnapshot> b : blocks) {
            Optional<Location<World>> location = b.getOriginal().getLocation();
            if (location.isPresent()) {
                locs.add(location.get());
            }
        }
        return locs;

    }

    public static boolean build(Player p, List<Location<World>> blocks, boolean place) {
        return build(p, blocks, place, true);
    }

    public static boolean build(Player p, List<Location<World>> blocks, boolean place, boolean msg) {
        for (Location<World> loc : blocks) {

            RegionWorldManager ma = SpongeGuard.getManager().getManager(loc.getExtent());
            FlagValue fla = ma.getFlag(EnumFlags.BUILD, loc);
            FlagValue verifica = ma.getFlag(place ? EnumFlags.PLACE : EnumFlags.BREAK, loc);
            boolean block = false;

            if (fla == FlagValue.DENY) {
                if (verifica != FlagValue.ALLOW) {
                    block = true;
                }

            } else {
                if (verifica == FlagValue.DENY) {
                    block = true;
                }
            }
            if (block && !ma.byPass(p)) {
                if (place) {
                    double x = p.getLocation().getX();
                    double y = p.getLocation().getBlockY();
                    double z = p.getLocation().getZ();
                    if (p.getWorld().getBlock((int) x, (int) (y), (int) z).getType() == BlockTypes.AIR) {
                        while (p.getWorld().getBlock((int) x, (int) (y - 1.0), (int) z).getType() == BlockTypes.AIR) {
                            y--;
                        }
                    }
                    p.setLocation(p.getWorld().getLocation(x, y, z));
                    p.setHeadRotation(new Vector3d(-90, -90, 0));
                    Cooldown.addCoolDown(p, "botoubloco", 800);
                } else {
                    Iterator<ExperienceOrb> iterator = exps.iterator();
                    while (iterator.hasNext()) {
                        ExperienceOrb next = iterator.next();
                        double x = next.getLocation().getPosition().getX() - 0.5;
                        double y = next.getLocation().getPosition().getY() - 0.5;
                        double z = next.getLocation().getPosition().getZ() - 0.5;
                        if (x == loc.getPosition().getX() && z == loc.getPosition().getZ() && y == loc.getPosition().getY()) {
                            next.remove();
                            iterator.remove();

                        }
                    }
                }

                p.sendMessage(Txt.f("§cVocê não pode construir aqui!"));
                return true;
            }

        }
        return false;
    }

    @Listener
    public void onEntitySpawn(SpawnEntityEvent event, @First SpawnCause spawnCause) {
        for (Entity e : event.getEntities()) {
            if (e instanceof ExperienceOrb) {
                ExperienceOrb ex = (ExperienceOrb) e;
                exps.add(ex);
            }
        }

    }
    private static HashSet<ExperienceOrb> exps = new HashSet();

    @Listener(beforeModifications = true,order = Order.EARLY)
    public void onBlockBreak(ChangeBlockEvent.Pre event, @First Player player) {
        if (build(player, event.getLocations(), false, false)) {
            event.setCancelled(true);

        }

    }

    @Listener(beforeModifications = true,order = Order.EARLY)
    public void onBlockBreak(ChangeBlockEvent.Break event, @First Player player) {
        if (build(player, getLocs(event.getTransactions()), false)) {
            event.setCancelled(true);

        }

    }

    @Listener
    public void onInteract(InteractEntityEvent.Primary event, @First Player player) {
        RegionWorldManager ma = SpongeGuard.getManager().getManager(event.getTargetEntity().getWorld());

        if (event.getTargetEntity().getType() == EntityTypes.BOAT
                || event.getTargetEntity().getType() == EntityTypes.RIDEABLE_MINECART) {
            FlagValue va = ma.getFlag(EnumFlags.VEHICLEDESTROY, event.getTargetEntity().getLocation());
            if (va == FlagValue.DENY && !ma.byPass(player)) {
                event.setCancelled(true);
            }

        } else {
            FlagValue va = ma.getFlag(EnumFlags.BUILD, event.getTargetEntity().getLocation());
            if (va == FlagValue.ALLOW && !ma.byPass(player)) {
                event.setCancelled(true);
            }
        }

    }
}
