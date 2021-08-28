package me.kodysimpson.simpapi.conversations;


import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Convenience class for describing messages that take up more than one line of chat.
 * Expected usage encompasses extending this class to define concrete message classes:
 * <pre>
 * final class InvalidInputMessage extends MultilineMessage {
 *    public InvalidInputMessage(Prompt previousPrompt, Player player) {
 *        super(previousPrompt,
 *         "That is not valid input, " + player.getName(),
 *         "Please enter input as follows:",
 *         "x... y... or z",
 *         "Returning to the previous prompt."
 *        );
 *    }
 * }
 * </pre>
 * Or by calling {@link MultilineMessage#MultilineMessage(Prompt, String...)} directly.
 *
 * @author muunitnocQ
 */
public class MultilineMessage extends MessagePrompt {

    private final Prompt end;
    private final String[] messages;

    public MultilineMessage(Prompt end, String... messages) {
        if (messages == null || messages.length == 0) {
            throw new IllegalStateException("Invalid multiline message");
        }

        this.end = end;
        this.messages = messages;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return ColorTranslator.translateColorCodes(messages[0]);
    }

    @Override
    protected Prompt getNextPrompt(@NotNull ConversationContext context) {
        if (messages.length == 1) return end;
        return new MultilineMessage(end, Arrays.copyOfRange(messages, 1, messages.length));
    }


}
