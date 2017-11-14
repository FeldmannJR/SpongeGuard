package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import br.com.instamc.sponge.guard.region.Region;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.animal.Animal;

public class EventSpawnEntity extends GuardEvent {

    @Listener
    public void onEntitySpawn(SpawnEntityEvent event) {
        if (!event.getEntities().isEmpty()) {
            for (Entity e : event.getEntities()) {
                RegionWorldManager ma = getManager(e.getWorld());
                if ((e instanceof Monster)) {
                    if (ma.getFlag(EnumFlags.MOBS, e.getLocation()) == FlagValue.DENY) {
                        event.setCancelled(true);
                    }
                }
                if ((e instanceof Animal)) {
                    if (ma.getFlag(EnumFlags.ANIMALS, e.getLocation()) == FlagValue.DENY) {
                        event.setCancelled(true);
                    }
                }
            }

        }
    }
}
