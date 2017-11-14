package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.manager.RegionWorldManager;
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
import br.com.instamc.sponge.library.utils.Txt;
import org.spongepowered.api.event.cause.entity.teleport.TeleportCause;
import org.spongepowered.api.event.cause.entity.teleport.TeleportType;
import org.spongepowered.api.event.cause.entity.teleport.TeleportTypes;
import org.spongepowered.api.event.entity.MoveEntityEvent;

public class EventSpawnEnderPearl extends GuardEvent {

    @Listener
    public void onEnderPearl(SpawnEntityEvent event, @First EntitySpawnCause e) {
        if (e.getType() == SpawnTypes.PLACEMENT) {

            if (e.getEntity().getType() == EntityTypes.PLAYER && event.getEntities().get(0).getType() == EntityTypes.ENDER_PEARL) {
                Player p = (Player) e.getEntity();
                RegionWorldManager ma = getManager(p.getWorld());
                if (ma.getFlag(EnumFlags.ENDERPEARL, p.getLocation()) == FlagValue.DENY) {
                    event.setCancelled(true);
                    p.sendMessage(Txt.f("§cVocê não pode usar enderpearl aqui!"));
                }
            }
        }

    }
    @Listener
    public void teleport(MoveEntityEvent.Teleport ev){
     	TeleportType tcause = TeleportTypes.UNKNOWN;
    	if (ev.getCause().containsType(TeleportCause.class)){
    		tcause = ev.getCause().first(TeleportCause.class).get().getTeleportType();
    	}
       //TO DO 
    }
}
