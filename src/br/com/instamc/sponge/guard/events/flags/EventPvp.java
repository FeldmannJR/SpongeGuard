package br.com.instamc.sponge.guard.events.flags;

import br.com.instamc.sponge.guard.manager.RegionWorldManager;
import br.com.instamc.sponge.guard.region.EnumFlags;
import br.com.instamc.sponge.guard.region.FlagValue;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;

import br.com.instamc.sponge.guard.region.Region;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventPvp extends GuardEvent {

    @Listener
    public void onPvp(InteractEntityEvent.Primary event, @Root Player player) {
        if (event.getTargetEntity() instanceof Player) {
            Player p = (Player) event.getTargetEntity();
            RegionWorldManager ma = getManager(p.getWorld());
            FlagValue attacker = ma.getFlag(EnumFlags.PVP, player.getLocation());
            FlagValue attacked = ma.getFlag(EnumFlags.PVP, p.getLocation());
            if (attacked == FlagValue.DENY || attacker == FlagValue.DENY) {
                event.setCancelled(true);
            }
        }
    }

    @Listener
    public void onDamageByEntity(DamageEntityEvent event, @Root DamageSource src, @First EntityDamageSource entity) {
        if (event.getTargetEntity() instanceof Player) {
            Entity e = entity.getSource();
            Player p = (Player) event.getTargetEntity();
            RegionWorldManager ma = getManager(p.getWorld());

            if (src.getType() == DamageTypes.EXPLOSIVE) {
                if (e.getType() == EntityTypes.DRAGON_FIREBALL || e.getType() == EntityTypes.ENDER_CRYSTAL
                        || e.getType() == EntityTypes.SMALL_FIREBALL || e.getType() == EntityTypes.FIREBALL) {
                    if (ma.getFlag(EnumFlags.OTHER_EXPLOSIONS, p.getLocation()) == FlagValue.DENY) {
                        event.setCancelled(true);
                    }
                    if (e.getType() == EntityTypes.TNT_MINECART || e.getType() == EntityTypes.PRIMED_TNT) {
                        if (ma.getFlag(EnumFlags.TNT, p.getLocation()) == FlagValue.DENY) {
                            event.setCancelled(true);
                        }
                    }
                }

            } else if (src.getType() == DamageTypes.PROJECTILE || src.getType() == DamageTypes.ATTACK) {
                if (e.getType() == EntityTypes.SPECTRAL_ARROW || e.getType() == EntityTypes.TIPPED_ARROW) {
                    Arrow projectile = (Arrow) e;
                    if (projectile != null) {
                        if (projectile.getShooter() instanceof Monster) {
                            if (ma.getFlag(EnumFlags.MOBDAMAGE, p.getLocation()) == FlagValue.DENY) {
                                event.setCancelled(true);
                            }
                        } else {
                            if (ma.getFlag(EnumFlags.PVP, p.getLocation()) == FlagValue.DENY) {
                                event.setCancelled(true);
                            }
                        }

                    }
                }

            } else if (src instanceof Monster) {
                if (ma.getFlag(EnumFlags.MOBDAMAGE, p.getLocation()) == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            }

        }
    }

    @Listener
    public void onDamage(DamageEntityEvent event, @Root DamageSource src) {
        if (event.getTargetEntity() instanceof Player) {
            Player p = (Player) event.getTargetEntity();
            RegionWorldManager ma = getManager(p.getWorld());

            Location<World> l = p.getLocation();
            if (ma.getFlag(EnumFlags.DAMAGE, l) == FlagValue.DENY) {
                event.setCancelled(true);
                return;
            }

            if (src.getType() == DamageTypes.HUNGER) {
                if (ma.getFlag(EnumFlags.HUNGER, l) == FlagValue.DENY) {
                    event.setCancelled(true);
                }
            }

        }
    }
}
