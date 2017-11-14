/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.instamc.sponge.guard.comandos;

import br.com.instamc.sponge.guard.comandos.subs.Command;
import br.com.instamc.sponge.guard.comandos.subs.Define;
import br.com.instamc.sponge.guard.comandos.subs.Delete;
import br.com.instamc.sponge.guard.comandos.subs.Flag;
import br.com.instamc.sponge.guard.comandos.subs.Flags;
import br.com.instamc.sponge.guard.comandos.subs.Info;
import br.com.instamc.sponge.guard.comandos.subs.List;
import br.com.instamc.sponge.guard.comandos.subs.Redefine;
import br.com.instamc.sponge.guard.comandos.subs.Rename;
import br.com.instamc.sponge.guard.comandos.subs.SetPriority;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.utils.Txt;
import java.util.HashMap;
import java.util.Optional;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 *
 * @author Carlos
 */
public class CommandRegion implements CommandExecutor {

    HashMap<String, SubCmdRegion> subs = new HashMap<>();

    public static String cmd = "region";

    public CommandRegion() {

        add(new Define());
        add(new Info());
        add(new List());
        add(new Flag());
        add(new Command());
        add(new SetPriority());
        add(new Delete());
        add(new Flags());
        add(new List());
        add(new Redefine());
        add(new Rename());
        add(new SetPriority());

    }

    public void add(SubCmdRegion el) {
        subs.put(el.cmd.toLowerCase(), el);
    }

    Text separator = Txt.f("§7§l§m=======================================");

    public void showHelp(Player p) {
        // STAFF

        p.sendMessage(separator);

        for (SubCmdRegion el : subs.values()) {
            p.sendMessage(Txt.f("§f[§a§l!!!§f] §e/" + cmd + " " + el.cmd + " " + el.getArgs() + " §8- §7" + el.getHelp()));

        }

        p.sendMessage(separator);

    }

    public void onCommand(CommandSource cs, String[] args) {
        Player p = (Player) cs;
        
        if (args.length == 0) {
            showHelp(p);
            return;
        }

        String subcmd = args[0].toLowerCase();
        if (subs.containsKey(subcmd)) {
            SubCmdRegion el = subs.get(subcmd);

            String[] newargs = new String[args.length - 1];
            for (int x = 1; x < args.length; x++) {
                newargs[x - 1] = args[x];
            }
            el.execute(p, newargs);

            return;
        }

        showHelp(p);

    }

    private static String[] getArgs(CommandContext ccArgs) {
        Optional<String> arg = ccArgs.getOne("args");
        String[] args = new String[0];
        if (arg.isPresent()) {
            args = arg.get().split(" ");
        }
        return args;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            if (src.hasPermission("instamc.region.use")) {
                onCommand(src, getArgs(args));
            }
        }
        return CommandResult.empty();
    }
}
