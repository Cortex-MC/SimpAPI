package me.kodysimpson.simpapi.conversations;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

//A better version of o.b.c.ExactMatchConversationCanceller

/**
 * @author muunitnocQ
 */
final class ArrayMatchCanceller implements ConversationCanceller {

    private final boolean caseSensitive;
    private final List<String> escapeWords;

    ArrayMatchCanceller(boolean caseSensitive, String... escapeWords) {
        this(caseSensitive, Arrays.asList(escapeWords));
    }

    ArrayMatchCanceller(boolean caseSensitive, List<String> escapeWords) {
        this.caseSensitive = caseSensitive;
        this.escapeWords = escapeWords;
    }

    @Override
    public void setConversation(@NotNull Conversation conversation) {
    }

    @Override
    public boolean cancelBasedOnInput(@NotNull ConversationContext context, @NotNull String input) {
        return escapeWords.stream()
                .anyMatch(escapeWord -> caseSensitive ? escapeWord.equals(input) : escapeWord.equalsIgnoreCase(input));
    }

    @Override
    public ConversationCanceller clone() {
        return new ArrayMatchCanceller(caseSensitive, escapeWords);
    }
}