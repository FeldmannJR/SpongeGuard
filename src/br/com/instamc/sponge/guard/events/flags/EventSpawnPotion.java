package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

public class EventSpawnPotion extends GuardEvent {

    @Listener
    public void onEnderPearl(SpawnEntityEvent event, @First EntitySpawnCause e) {

        if (e.getEntity().getType() == EntityTypes.PLAYER) {
            Player p = (Player) e.getEntity();
            for (Entity spawned : event.getEntities()) {
                if (spawned.getType() == EntityTypes.AREA_EFFECT_CLOUD || spawned.getType() == EntityTypes.SPLASH_POTION || spawned.getType() == EntityTypes.TIPPED_ARROW || spawned.getType() == EntityTypes.SPECTRAL_ARROW) {
                    if (getManager(spawned.getWorld()).getFlag(EnumFlags.POTIONSPLASH, spawned.getLocation()) == FlagValue.DENY) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

        }

    }
}
