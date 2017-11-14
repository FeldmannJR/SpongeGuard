package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventDecay {

    @Listener
    public void onDecay(ChangeBlockEvent.Decay e) {
        for (Transaction<BlockSnapshot> block : e.getTransactions()) {
            Location<World> loc = block.getOriginal().getLocation().get();
            RegionWorldManager ma = SpongeGuard.getManager().getManager(loc.getExtent());
            if (ma.getFlag(EnumFlags.LEAFDECAY, loc) == FlagValue.DENY) {
                e.setCancelled(true);
            }
        }

    }

    @Listener(beforeModifications = true)
    public void decay(ChangeBlockEvent.Pre e) {
        if (e.getCause().containsNamed(NamedCause.DECAY)) {
            for (Location<World> loc : e.getLocations()) {
                RegionWorldManager ma = SpongeGuard.getManager().getManager(loc.getExtent());
                if (ma.getFlag(EnumFlags.LEAFDECAY, loc) == FlagValue.DENY) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
