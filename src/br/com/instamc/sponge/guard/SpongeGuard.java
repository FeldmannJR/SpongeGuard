package br.com.instamc.sponge.guard;

import br.com.instamc.sponge.guard.comandos.CommandRegion;
import br.com.instamc.sponge.guard.manager.RegionManager;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import br.com.instamc.sponge.guard.config.ConfigurationManager;

import br.com.instamc.sponge.guard.events.flags.EventBlockBreak;
import br.com.instamc.sponge.guard.events.flags.EventBlockExplosion;
import br.com.instamc.sponge.guard.events.flags.EventBlockPlace;
import br.com.instamc.sponge.guard.events.flags.EventBlockUse;

import br.com.instamc.sponge.guard.events.flags.EventCommandSend;
import br.com.instamc.sponge.guard.events.flags.EventDecay;
import br.com.instamc.sponge.guard.events.flags.EventExperienceDrop;
import br.com.instamc.sponge.guard.events.flags.EventFlow;
import br.com.instamc.sponge.guard.events.flags.EventHunger;
import br.com.instamc.sponge.guard.events.flags.EventItemDrop;
import br.com.instamc.sponge.guard.events.flags.EventItemUse;
import br.com.instamc.sponge.guard.events.flags.EventPvp;

import br.com.instamc.sponge.guard.events.flags.EventSpawnEnderPearl;
import br.com.instamc.sponge.guard.events.flags.EventSpawnEntity;
import br.com.instamc.sponge.guard.events.flags.EventSpawnItem;
import br.com.instamc.sponge.guard.events.flags.EventSpawnPotion;
import br.com.instamc.sponge.guard.events.flags.EventSpawnVehicle;
import br.com.instamc.sponge.guard.region.Region;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.sponge.SpongeWorldEdit;

import java.util.ArrayList;
import java.util.List;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.block.BlockType;
import static org.spongepowered.api.command.args.GenericArguments.optional;
import org.spongepowered.api.plugin.Dependency;


@Plugin(id = "spongeguard", name = "SpongeGuard-ITMC", version = "1.0", description = "Regions do InstaMC em Sponge.", dependencies = {
    @Dependency(id = "worldedit")})
public class SpongeGuard {

    public HashMap<Player, Region> pendings = new HashMap<Player, Region>();
    public static List<String> idsbloquedos = new ArrayList<>();

    private static RegionManager manager;
    public static SpongeGuard instance;

    private static List<BlockType> chests = new ArrayList();
    private static List<BlockType> doors = new ArrayList();
    @Inject
    Game game;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File configFile;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    private Logger logger;

    public static RegionManager getManager() {
        return manager;
    }

    public File getConfigFile() {
        return configFile;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
        return configManager;
    }

    public Logger getLogger() {
        return logger;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        instance = this;
        logger.info("Carregando SpongeGuard...");
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        manager = new RegionManager();
        manager.loadWorlds();
        Sponge.getCommandManager().register(this,
                CommandSpec.builder()
                .description(Text.of("Comando de regiões!"))
                .executor(new CommandRegion())
                .arguments(optional(GenericArguments.remainingJoinedStrings(Text.of("args"))))
                .build(), "region", "rg");
        game.getEventManager().registerListeners(this, manager);
        game.getEventManager().registerListeners(this, new EventBlockPlace());
        game.getEventManager().registerListeners(this, new EventBlockBreak());
        game.getEventManager().registerListeners(this, new EventPvp());
        game.getEventManager().registerListeners(this, new EventBlockExplosion());
        game.getEventManager().registerListeners(this, new EventItemDrop());
        game.getEventManager().registerListeners(this, new EventExperienceDrop());
        game.getEventManager().registerListeners(this, new EventItemUse());
        game.getEventManager().registerListeners(this, new EventSpawnEnderPearl());

        game.getEventManager().registerListeners(this, new EventBlockUse());
        game.getEventManager().registerListeners(this, new EventSpawnVehicle());

        game.getEventManager().registerListeners(this, new EventFlow());
        game.getEventManager().registerListeners(this, new EventDecay());
        game.getEventManager().registerListeners(this, new EventSpawnItem());
        game.getEventManager().registerListeners(this, new EventCommandSend());
        game.getEventManager().registerListeners(this, new EventSpawnPotion());
        game.getEventManager().registerListeners(this, new EventSpawnEntity());

        Task.builder().execute(new EventHunger())
                .interval(1, TimeUnit.SECONDS)
                .name("Hunger Timer Task").submit(instance);

        logger.info("SpongeGuard Carregado!");
    }

    public static SpongeWorldEdit getWorldEdit() {
        return SpongeWorldEdit.inst();
    }
}
