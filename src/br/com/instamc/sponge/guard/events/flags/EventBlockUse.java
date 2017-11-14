package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import br.com.instamc.sponge.guard.region.Region;
import java.util.Arrays;
import java.util.List;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventBlockUse {

    List<BlockType> usableblocks = Arrays.asList(
            BlockTypes.FURNACE,
            BlockTypes.ENCHANTING_TABLE,
            BlockTypes.CRAFTING_TABLE,
            BlockTypes.ENDER_CHEST,
            BlockTypes.BREWING_STAND,
            BlockTypes.DISPENSER,
            BlockTypes.JUKEBOX,
            BlockTypes.ANVIL,
            BlockTypes.HOPPER,
            BlockTypes.DROPPER,
            BlockTypes.BEACON,
            BlockTypes.BED,
            BlockTypes.LEVER,
            BlockTypes.STONE_BUTTON,
            BlockTypes.WOODEN_BUTTON,
            BlockTypes.WOODEN_PRESSURE_PLATE,
            BlockTypes.STONE_PRESSURE_PLATE,
            BlockTypes.HEAVY_WEIGHTED_PRESSURE_PLATE,
            BlockTypes.LIGHT_WEIGHTED_PRESSURE_PLATE,
            BlockTypes.POWERED_REPEATER,
            BlockTypes.UNPOWERED_REPEATER,
            BlockTypes.POWERED_COMPARATOR,
            BlockTypes.UNPOWERED_COMPARATOR
    );
    List<BlockType> doorblocks = Arrays.asList(BlockTypes.WOODEN_DOOR,
            BlockTypes.ACACIA_DOOR,
            BlockTypes.BIRCH_DOOR,
            BlockTypes.DARK_OAK_DOOR,
            BlockTypes.JUNGLE_DOOR,
            BlockTypes.SPRUCE_DOOR,
            BlockTypes.TRAPDOOR,
            BlockTypes.FENCE_GATE,
            BlockTypes.ACACIA_FENCE_GATE,
            BlockTypes.BIRCH_FENCE_GATE,
            BlockTypes.DARK_OAK_FENCE_GATE,
            BlockTypes.JUNGLE_FENCE_GATE,
            BlockTypes.SPRUCE_FENCE_GATE
    );

    @Listener
    public void onChestUse(InteractBlockEvent.Secondary event, @First Player player) {
        BlockSnapshot block = event.getTargetBlock();
        if(!block.getLocation().isPresent()){
            return;
        }
        Location<World> loc = event.getTargetBlock().getLocation().get();
        RegionWorldManager ma = SpongeGuard.getManager().getManager(loc.getExtent());
        if (ma.byPass(player)) {
            return;
        }
        if (block.getState().getType().equals(BlockTypes.CHEST) || block.getState().getType().equals(BlockTypes.TRAPPED_CHEST)) {
            if (ma.getFlag(EnumFlags.CHESTS, loc) == FlagValue.DENY) {
                event.setCancelled(true);
            }

        } else if (block.getState().getType().equals(BlockTypes.ENDER_CHEST)) {
            if (ma.getFlag(EnumFlags.ENDERCHEST, loc) == FlagValue.DENY) {
                event.setCancelled(true);
            }
        } else if (usableblocks.contains(block.getState().getType())) {
            if (ma.getFlag(EnumFlags.ENDERCHEST, loc) == FlagValue.DENY) {
                event.setCancelled(true);
            }
        } else if (doorblocks.contains(block.getState().getType())) {
            if (ma.getFlag(EnumFlags.DOORS, loc) == FlagValue.DENY) {
                event.setCancelled(true);
            }
        }

    }

}
