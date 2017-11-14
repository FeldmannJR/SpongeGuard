package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import br.com.instamc.sponge.guard.region.Region;

public class EventHunger extends GuardEvent implements Runnable {

    @Override
    public void run() {
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            if (p.gameMode().get() == GameModes.SURVIVAL) {
                if (getManager(p.getWorld()).getFlag(EnumFlags.HUNGER, p.getLocation()) == FlagValue.DENY) {
                    p.offer(Keys.FOOD_LEVEL, 20);
                }
            }
        }
    }

}
