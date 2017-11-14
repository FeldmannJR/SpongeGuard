package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import java.util.ArrayList;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventBlockExplosion {

    @Listener
    public void onBlockExplode(ExplosionEvent.Detonate event) {
        Entity src = null;
        if (event.getCause().root() instanceof Entity) {
            src = (Entity) event.getCause().root();
        }
        Location<World> loc = event.getExplosion().getLocation();
        RegionWorldManager ma = SpongeGuard.getManager().getManager(loc.getExtent());
        FlagValue tnt = ma.getFlag(EnumFlags.TNT, loc);
        FlagValue creeper = ma.getFlag(EnumFlags.CREEPER_EXPLOSIONS, loc);
        FlagValue other = ma.getFlag(EnumFlags.OTHER_EXPLOSIONS, loc);

        if (src != null && src.getType() == EntityTypes.CREEPER) {
            if (creeper == FlagValue.DENY) {
                event.setCancelled(true);
            }
        } else if (src != null && (src.getType() == EntityTypes.TNT_MINECART || src.getType() == EntityTypes.PRIMED_TNT)) {
            if (tnt == FlagValue.DENY) {
                event.setCancelled(true);
            }
        } else {
            if (other == FlagValue.DENY) {
                event.setCancelled(true);
            }
        }

        ArrayList<Location<World>> locations = new ArrayList<Location<World>>();
        for (Location<World> l : event.getAffectedLocations()) {
            if (event.getTargetWorld().getBlock(l.getBlockPosition()).getType() != BlockTypes.AIR) {
                locations.add(l);
            }
        }

        for (Location<World> l : locations) {
            tnt = ma.getFlag(EnumFlags.TNT, l);
            creeper = ma.getFlag(EnumFlags.CREEPER_EXPLOSIONS, l);
            other = ma.getFlag(EnumFlags.OTHER_EXPLOSIONS, l);

            if (src != null && src.getType() == EntityTypes.CREEPER) {
                if (creeper == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            } else if (src != null && (src.getType() == EntityTypes.TNT_MINECART || src.getType() == EntityTypes.PRIMED_TNT)) {
                if (tnt == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            } else {
                if (other == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            }

        }

    }
}
