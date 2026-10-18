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
            sendError(sender, "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender, label);
            return true;
        }

        String subCommand = args[0].toLowerCase(Locale.ROOT);
        switch (subCommand) {
            case "help" -> sendHelp(sender, label);
            case "radius" -> handleRadius(sender, label, args);
            case "reload" -> handleReload(sender, label, args);
            default -> {
                sendError(sender, "Unknown subcommand: " + args[0] + ".");
                sendHelp(sender, label);
            }
        }
        return true;
    }

    private void handleRadius(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            sendInfo(sender, "Current sharing mode: ", formatRadius(plugin.getShareRadius()));
            CommandMessage.message()
                    .gray("Use ")
                    .yellow(command(label, "radius -1"))
                    .gray(" for all online players, or ")
                    .yellow(command(label, "radius <blocks>"))
                    .gray(" for distance mode.")
                    .sendTo(sender);
            return;
        }

        if (args.length != 2) {
            sendError(sender, "Too many arguments for the radius command.");
            sendUsage(sender, command(label, "radius <blocks|-1>"));
            return;
        }

        double radius;
        try {
            radius = Double.parseDouble(args[1]);
        } catch (NumberFormatException exception) {
            sendError(sender, "Invalid radius: " + args[1] + ". Use -1, 0, or a positive number.");
            return;
        }

        if (!ShareRadiusPolicy.isValid(radius)) {
            sendError(sender, "Invalid radius: " + args[1]
                    + ". Use -1 for all online players, or a finite number of 0 or greater.");
            return;
        }

        plugin.setShareRadius(radius);
        sendSuccess(sender, "Sharing mode updated: ", formatRadius(radius));
    }

    private void handleReload(CommandSender sender, String label, String[] args) {
        if (args.length != 1) {
            sendError(sender, "The reload command does not accept additional arguments.");
            sendUsage(sender, command(label, "reload"));
            return;
        }

        plugin.reloadConfig();
        plugin.reloadShareRadius();
        sendSuccess(sender, "Configuration reloaded. Current mode: ", formatRadius(plugin.getShareRadius()));
    }

    private void sendHelp(CommandSender sender, String label) {
        CommandMessage.message()
                .darkAqua("---------------- ")
                .aquaBold("EveryoneGetsDiscount")
                .darkAqua(" ----------------")
                .sendTo(sender);
        sendInfo(sender, "Current sharing mode: ", formatRadius(plugin.getShareRadius()));
        sendHelpEntry(sender, command(label, "radius"), "Show the current sharing mode");
        sendHelpEntry(sender, command(label, "radius -1"), "Share with all online players");
        sendHelpEntry(sender, command(label, "radius <blocks>"), "Limit sharing to players within range");
        sendHelpEntry(sender, command(label, "reload"), "Reload config.yml");
        CommandMessage.message().darkGray("Permission: ").gray(PERMISSION).sendTo(sender);
    }

    static String formatRadius(double radius) {
        if (radius < 0.0D) {
            return "all online players (-1)";
        }

        if (radius == Math.rint(radius)) {
            return "players within " + Math.round(radius) + " blocks";
        }

        return String.format(Locale.ROOT, "players within %.2f blocks", radius);
    }

    private static String command(String label, String arguments) {
        return "/" + label + " " + arguments;
    }

    private static void sendHelpEntry(CommandSender sender, String command, String description) {
        CommandMessage.message()
                .yellow(command)
                .darkGray(" - ")
                .gray(description)
                .sendTo(sender);
    }

    private static void sendUsage(CommandSender sender, String command) {
        CommandMessage.message().gray("Usage: ").yellow(command).sendTo(sender);
    }

    private static void sendInfo(CommandSender sender, String message, String value) {
        prefixed().gray(message).yellow(value).gray(".").sendTo(sender);
    }

    private static void sendSuccess(CommandSender sender, String message, String value) {
        prefixed().green(message).yellow(value).green(".").sendTo(sender);
    }

    private static void sendError(CommandSender sender, String message) {
        prefixed().red(message).sendTo(sender);
    }

    private static CommandMessage prefixed() {
        return CommandMessage.message().darkAqua("[").aqua("EGD").darkAqua("] ");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!sender.hasPermission(PERMISSION)) {
            return suggestions;
        }

        if (args.length == 1) {
            addIfStartsWith(suggestions, "help", args[0]);
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
