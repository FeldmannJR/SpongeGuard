package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.spawn.BlockSpawnCause;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import br.com.instamc.sponge.guard.region.Region;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventSpawnItem extends GuardEvent {
    
    @Listener
    public void onDecay(ChangeBlockEvent.Decay e) {
        for (Transaction<BlockSnapshot> b : e.getTransactions()) {
            Location<World> l = b.getOriginal().getLocation().get();
            RegionWorldManager ma = getManager(l.getExtent());
            FlagValue decay = ma.getFlag(EnumFlags.LEAFDECAY, l);
            if (decay == FlagValue.DENY) {
                e.setCancelled(true);
                b.setValid(false);
            }
        }
    }
    
}
