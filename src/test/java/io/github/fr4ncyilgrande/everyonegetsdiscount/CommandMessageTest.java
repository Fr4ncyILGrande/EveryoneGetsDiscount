package io.github.fr4ncyilgrande.everyonegetsdiscount;

import org.junit.jupiter.api.Test;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandMessageTest {

    @Test
    void rendersModernAndSpigotFallbackFormats() {
        CommandMessage message = CommandMessage.message()
                .darkAqua("[")
                .aquaBold("EGD")
                .yellow(" /egd radius <blocks>");

        assertEquals(
                "<reset><dark_aqua>[<reset><aqua><bold>EGD<reset><yellow> /egd radius \\<blocks>",
                message.toMiniMessage()
        );
        assertEquals(
                "\u00A7r\u00A73[\u00A7r\u00A7b\u00A7lEGD\u00A7r\u00A7e /egd radius <blocks>",
                message.toLegacyText()
        );
    }

    @Test
    void escapesMiniMessageControlCharactersInDynamicText() {
        assertEquals(
                "<reset><red>bad \\<red>value\\\\path",
                CommandMessage.message().red("bad <red>value\\path").toMiniMessage()
        );
    }

    @Test
    void selectsRichMessageApiWhenPaperProvidesIt() {
        AtomicReference<String> calledMethod = new AtomicReference<>();
        AtomicReference<String> sentMessage = new AtomicReference<>();
        CommandSender sender = (CommandSender) Proxy.newProxyInstance(
                CommandSender.class.getClassLoader(),
                new Class<?>[]{CommandSender.class},
                (proxy, method, arguments) -> {
                    calledMethod.set(method.getName());
                    sentMessage.set((String) arguments[0]);
                    return null;
                }
        );

        CommandMessage.message().green("Modern output").sendTo(sender);

        assertEquals("sendRichMessage", calledMethod.get());
        assertEquals("<reset><green>Modern output", sentMessage.get());
    }
}
