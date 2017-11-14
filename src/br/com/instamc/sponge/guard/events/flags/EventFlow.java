package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import java.util.Optional;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.property.block.MatterProperty.Matter;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import br.com.instamc.sponge.guard.region.Region;

public class EventFlow {

    @Listener
    public void onFlow(ChangeBlockEvent.Pre event) {

        BlockSnapshot snapshot = event.getTargetWorld().createSnapshot(event.getLocations().get(0).getBlockX(), event.getLocations().get(0).getBlockY(), event.getLocations().get(0).getBlockZ());
        Location<World> loc = event.getLocations().get(event.getLocations().size() - 1);
        RegionWorldManager ma = SpongeGuard.getManager().getManager(loc.getExtent());
        FlagValue water = ma.getFlag(EnumFlags.WATERFLOW, loc);
        FlagValue lava = ma.getFlag(EnumFlags.LAVAFLOW, loc);
        Optional<MatterProperty> matter = snapshot.getState().getProperty(MatterProperty.class);

        if (matter.isPresent() && matter.get().getValue() == Matter.LIQUID) {
            if (snapshot.getState().getType() == BlockTypes.LAVA
                    || snapshot.getState().getType() == BlockTypes.FLOWING_LAVA) {
                if (lava == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            } else {
                if (water == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
