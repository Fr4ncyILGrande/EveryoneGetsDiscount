package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

final class CommandMessage {

    private static final String LEGACY_RESET = "\u00A7r";

    private static volatile Method richMessageMethod = findRichMessageMethod();

    private final List<Segment> segments = new ArrayList<>();

    private CommandMessage() {
    }

    static CommandMessage message() {
        return new CommandMessage();
    }

    CommandMessage darkAqua(String text) {
        return append(text, MessageColor.DARK_AQUA, false);
    }

    CommandMessage aqua(String text) {
        return append(text, MessageColor.AQUA, false);
    }

    CommandMessage aquaBold(String text) {
        return append(text, MessageColor.AQUA, true);
    }

    CommandMessage gray(String text) {
        return append(text, MessageColor.GRAY, false);
    }

    CommandMessage darkGray(String text) {
        return append(text, MessageColor.DARK_GRAY, false);
    }

    CommandMessage yellow(String text) {
        return append(text, MessageColor.YELLOW, false);
    }

    CommandMessage green(String text) {
        return append(text, MessageColor.GREEN, false);
    }

    CommandMessage red(String text) {
        return append(text, MessageColor.RED, false);
    }

    void sendTo(CommandSender sender) {
        Method method = richMessageMethod;
        if (method != null) {
            try {
                method.invoke(sender, toMiniMessage());
                return;
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ignored) {
                richMessageMethod = null;
            }
        }

        // Supported Spigot versions do not expose Adventure on CommandSender.
        sender.sendMessage(toLegacyText());
    }

    String toMiniMessage() {
        StringBuilder message = new StringBuilder();
        for (Segment segment : segments) {
            message.append("<reset><").append(segment.color().miniMessageName()).append('>');
            if (segment.bold()) {
                message.append("<bold>");
            }
            message.append(escapeMiniMessage(segment.text()));
        }
        return message.toString();
    }

    String toLegacyText() {
        StringBuilder message = new StringBuilder();
        for (Segment segment : segments) {
            message.append(LEGACY_RESET).append('\u00A7').append(segment.color().legacyCode());
            if (segment.bold()) {
                message.append("\u00A7l");
            }
            message.append(segment.text());
        }
        return message.toString();
    }

    private CommandMessage append(String text, MessageColor color, boolean bold) {
        segments.add(new Segment(text, color, bold));
        return this;
    }

    private static Method findRichMessageMethod() {
        try {
            return CommandSender.class.getMethod("sendRichMessage", String.class);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private static String escapeMiniMessage(String text) {
        return text.replace("\\", "\\\\").replace("<", "\\<");
    }

    private record Segment(String text, MessageColor color, boolean bold) {
    }

    private enum MessageColor {
        DARK_AQUA("dark_aqua", '3'),
        AQUA("aqua", 'b'),
        GRAY("gray", '7'),
        DARK_GRAY("dark_gray", '8'),
        YELLOW("yellow", 'e'),
        GREEN("green", 'a'),
        RED("red", 'c');

        private final String miniMessageName;
        private final char legacyCode;

        MessageColor(String miniMessageName, char legacyCode) {
            this.miniMessageName = miniMessageName;
            this.legacyCode = legacyCode;
        }

        String miniMessageName() {
            return miniMessageName;
        }

        char legacyCode() {
            return legacyCode;
        }
    }
}
