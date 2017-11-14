package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.monster.Enderman;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import br.com.instamc.sponge.guard.region.Region;
import br.com.instamc.sponge.library.utils.Txt;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventBlockPlace {

    @Listener
    public void onBlockPlaceByEntity(ChangeBlockEvent.Place event) {
          if(event.getCause().root() instanceof CommandBlock){
                return;
            }
        for (Transaction<BlockSnapshot> t : event.getTransactions()) {
            if (!t.getOriginal().getLocation().isPresent()) {
                continue;
            }
            Location<World> loc = t.getOriginal().getLocation().get();
            RegionWorldManager ma = SpongeGuard.getManager().getManager(loc.getExtent());
            if (event.getCause().root() instanceof Enderman) {
                if (ma.getFlag(EnumFlags.ENDERMANGRIEF, loc) == FlagValue.DENY) {
                    event.setCancelled(true);
                    return;
                }
            } else if (!(event.getCause().root() instanceof Player)) {
                if (ma.getFlag(EnumFlags.BUILD, loc) == FlagValue.DENY) {
                    event.setCancelled(true);
                    return;
                }
            }

        }

    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event, @First Player player) {
        if (EventBlockBreak.build(player, EventBlockBreak.getLocs(event.getTransactions()), true)) {
            event.setCancelled(true);

        }
    }

    

}
