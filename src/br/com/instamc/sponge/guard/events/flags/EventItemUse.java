package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import br.com.instamc.sponge.guard.region.Region;
import java.util.Arrays;
import java.util.List;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventItemUse extends GuardEvent {

    @Listener
    public void onItemUse(InteractBlockEvent.Secondary event, @First Player player) {
        BlockSnapshot block = event.getTargetBlock();
        Location<World> loc = null;
        if (!block.getState().getType().equals(BlockTypes.AIR)) {
            loc = block.getLocation().get();
        } else {
            loc = player.getLocation();
        }

        ItemType item = null;
        if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
            item = player.getItemInHand(HandTypes.MAIN_HAND).get().getItem();
        } else if (player.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
            item = player.getItemInHand(HandTypes.OFF_HAND).get().getItem();
        }
        List<ItemType> bloqueado = Arrays.asList(
                ItemTypes.ARMOR_STAND,
                ItemTypes.FLINT_AND_STEEL,
                ItemTypes.MONSTER_EGG,
                ItemTypes.ITEM_FRAME,
                ItemTypes.PAINTING,
                ItemTypes.LAVA_BUCKET,
                ItemTypes.WATER_BUCKET,
                ItemTypes.END_CRYSTAL
        );
        if (item != null) {
            if (bloqueado.contains(item)) {
                RegionWorldManager manager = getManager(loc.getExtent());
                if(manager.getFlag(EnumFlags.BUILD, loc)==FlagValue.DENY){
                    if(!manager.byPass(player)){
                        event.setCancelled(true);
                    }
                }
            }

          
        }
        
    }

}
