package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import br.com.instamc.sponge.guard.region.Region;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;

public class EventItemDrop extends GuardEvent {
    
    @Listener
    public void onItemDrop(SpawnEntityEvent event, @First EntitySpawnCause spawnCause) {
        if (spawnCause.getType() == SpawnTypes.DROPPED_ITEM) {
            if (spawnCause.getEntity() instanceof Player) {
                Player p = (Player) spawnCause.getEntity();
                FlagValue f = getManager(p.getWorld()).getFlag(EnumFlags.ITEMDROP, p.getLocation());
                if (f == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            }
        }
        
    }
    
}
