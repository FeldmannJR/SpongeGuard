package br.com.instamc.sponge.guard.events.flags;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

import br.com.instamc.sponge.guard.region.Region;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;

public class EventCommandSend extends GuardEvent {

    @Listener
    public void onCommandSend(SendCommandEvent event, @First Player p) {
        String cmd = event.getCommand().toLowerCase();
        Region r = getManager(p.getWorld()).getHighestPriority(p.getLocation(), true);
        if (r != null) {
            if (r.getCommands().contains(cmd)) {
                if (!getManager(p.getWorld()).byPass(p)) {

                    p.sendMessage(Text.of(TextColors.RED, "Você não pode usar o comando " + cmd + " aqui!"));
                    event.setCancelled(true);
                }
            }
        }

    }
}
