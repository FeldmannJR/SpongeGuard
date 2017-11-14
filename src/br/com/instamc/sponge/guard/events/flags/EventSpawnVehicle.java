package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import br.com.instamc.sponge.guard.region.Region;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.vehicle.Boat;
import org.spongepowered.api.entity.vehicle.minecart.Minecart;

public class EventSpawnVehicle extends GuardEvent {

    @Listener
    public void onEnderPearl(SpawnEntityEvent event, @First EntitySpawnCause e) {
        if (e.getType() == SpawnTypes.PLACEMENT) {
            for (Entity spawned : event.getEntities()) {
                if (spawned instanceof Minecart || spawned instanceof Boat) {
                    if (getManager(spawned.getWorld()).getFlag(EnumFlags.VEHICLEPLACE, spawned.getLocation()) == FlagValue.DENY) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

    }
}
