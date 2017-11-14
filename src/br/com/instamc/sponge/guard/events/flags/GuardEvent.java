/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.SpongeGuard;
import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import org.spongepowered.api.world.World;

/**
 *
 * @author Carlos
 */
public class GuardEvent {
    
    public RegionWorldManager getManager(World w){
        return SpongeGuard.getManager().getManager(w);
    }
    
}
