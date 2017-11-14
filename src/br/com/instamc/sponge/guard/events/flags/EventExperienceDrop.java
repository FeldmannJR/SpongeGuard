package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import br.com.instamc.sponge.guard.region.Region;

public class EventExperienceDrop {

    @Listener
    public void onExperienceDrop(SpawnEntityEvent event) {
        if (!event.getEntities().isEmpty()) {
            Entity e = event.getEntities().get(0);
            if (e.getType() == EntityTypes.EXPERIENCE_ORB) {

                RegionWorldManager ma = SpongeGuard.getManager().getManager(e.getLocation().getExtent());
                if (ma.getFlag(EnumFlags.EXPDROP, e.getLocation()) == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
