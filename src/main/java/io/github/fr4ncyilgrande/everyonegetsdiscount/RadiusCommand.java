package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class RadiusCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "everyonegetsdiscount.admin";

    private final EveryoneGetsDiscount plugin;

    public RadiusCommand(EveryoneGetsDiscount plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sendUsage(sender, label);
            return true;
        }

        String subCommand = args[0].toLowerCase(Locale.ROOT);
        if (subCommand.equals("radius")) {
            handleRadius(sender, label, args);
            return true;
        }

        if (subCommand.equals("reload")) {
            plugin.reloadConfig();
            plugin.reloadShareRadius();
            sender.sendMessage("EveryoneGetsDiscount config reloaded. Current radius: " + formatRadius(plugin.getShareRadius()) + ".");
            return true;
        }

        sendUsage(sender, label);
        return true;
    }

    private void handleRadius(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            sender.sendMessage("Current shared discount radius: " + formatRadius(plugin.getShareRadius()) + ".");
            sender.sendMessage("Use /" + label + " radius -1 for all online players, or /" + label + " radius <blocks>.");
            return;
        }

        if (args.length != 2) {
            sendUsage(sender, label);
            return;
        }

        double radius;
        try {
            radius = Double.parseDouble(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage("Invalid radius. Use -1, 0, or a positive number.");
            return;
        }

        if (!Double.isFinite(radius) || (radius != -1.0D && radius < 0.0D)) {
            sender.sendMessage("Invalid radius. Use -1 for all online players, or 0 / a positive number for distance mode.");
            return;
        }

        plugin.setShareRadius(radius);
        sender.sendMessage("Shared discount radius set to " + formatRadius(radius) + ".");
    }

    private static void sendUsage(CommandSender sender, String label) {
        sender.sendMessage("EveryoneGetsDiscount commands:");
        sender.sendMessage("/" + label + " radius - show the current radius");
        sender.sendMessage("/" + label + " radius -1 - share with all online players");
        sender.sendMessage("/" + label + " radius <blocks> - share only with online players within that distance");
        sender.sendMessage("/" + label + " reload - reload config.yml");
    }

    private static String formatRadius(double radius) {
        if (radius < 0.0D) {
            return "-1 (all online players)";
        }

        if (radius == Math.rint(radius)) {
            return Long.toString(Math.round(radius)) + " blocks";
        }

        return String.format(Locale.ROOT, "%.2f blocks", radius);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!sender.hasPermission(PERMISSION)) {
            return suggestions;
        }

        if (args.length == 1) {
            addIfStartsWith(suggestions, "radius", args[0]);
            addIfStartsWith(suggestions, "reload", args[0]);
            return suggestions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("radius")) {
            addIfStartsWith(suggestions, "-1", args[1]);
            addIfStartsWith(suggestions, "50", args[1]);
            addIfStartsWith(suggestions, "100", args[1]);
            addIfStartsWith(suggestions, "200", args[1]);
        }

        return suggestions;
    }

    private static void addIfStartsWith(List<String> suggestions, String value, String prefix) {
        if (value.toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT))) {
            suggestions.add(value);
        }
    }
}
