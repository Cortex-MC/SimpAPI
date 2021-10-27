package me.kodysimpson.simpapi.conversations;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Collections;
import java.util.Map;

/**
 * Helper class for construction of conversations.
 * Wraps {@link ConversationFactory} to make building conversations with players
 * significantly less tedious and boiler-plate prone.
 *
 * @author muunitnocQ
 * @see ConversationOptions
 */
public final class ConversationStarter {

    /**
     * Constructs a conversation with the first prompt for a given player under the authority of the provided plugin,
     * with optionally attached data.
     *
     * @param plugin      The plugin owning this conversation instance
     * @param player      Who the conversation is for
     * @param firstPrompt The prompt to start off on
     * @param options     Optional fine-grained configuration of the conversation. Refer to {@link ConversationOptions}
     * @param initialData Optional session data to start off with. Refer to {@link ConversationContext#getAllSessionData()}
     * @return The conversation, not yet started. See {@link Conversation#begin()}
     */
    public static @NotNull Conversation getForPlayer(
            @NotNull Plugin plugin,
            @NotNull Player player,
            @NotNull Prompt firstPrompt,
            @Nullable ConversationOptions options,
            @Nullable Map<Object, Object> initialData
    ) {
        if (options == null) options = ConversationOptions.DEFAULT_OPTIONS;

        ConversationFactory factory = new ConversationFactory(plugin)
                .withFirstPrompt(firstPrompt)
                .withLocalEcho(options.localEcho)
                .withPrefix(options.prefix)
                .addConversationAbandonedListener(new PrefixedAbandonedListener(options.rawPrefix))
                .withConversationCanceller(new ArrayMatchCanceller(options.escapeWordsCaseSensitive, options.escapeWords))
                .withInitialSessionData(initialData == null ? Collections.emptyMap() : initialData)
                .withTimeout(options.timeOut);
        Conversation convo = factory.buildConversation(player);

        new ServerReloadListener(plugin, convo);

        return convo;
    }

    //I really want records, koby
    //java 8 makes me go grrr

    /**
     * Represents the configurable options in a conversation.
     *
     * @see ConversationOptions#ConversationOptions(String, boolean, boolean, int, boolean, String...)
     */
    public final static class ConversationOptions {

        //Applied if the options for ConversationStarter#getForPlayer is null
        private static final ConversationOptions DEFAULT_OPTIONS = new ConversationOptions(
                null,
                true,
                false,
                60,
                false,
                "cancel", "exit", "quit", "escape", "done"
        );

        /**
         * Shown before every line of output in the conversation.
         * Supports Minecraft vanilla colors and hex colors in the format &#RRGGBB.
         *
         * @see ColorTranslator#translateColorCodes(String)
         */
        public final ConversationPrefix prefix;
        public final boolean localEcho;
        public final boolean modal;
        public final int timeOut;
        public final boolean escapeWordsCaseSensitive;
        public final String[] escapeWords;
        private final String rawPrefix;

        /**
         * Describes all options a conversation can have.
         *
         * @param prefix                   Shown before every line of output in the conversation
         *                                 Defaults to "" if null is passed
         * @param localEcho                Should the player's input be displayed back to them?
         * @param modal                    Determines if the player can see other players chatting during the conversation
         * @param timeOut                  An automatic time-out in which the conversation will cancel upon receiving no input from the player
         *                                 Clamps to 0 as a minimum.
         * @param escapeWordsCaseSensitive Should escape words be case sensitive? See next parameter for details
         * @param escapeWords              Words that, when entered as input, will force the conversation to be abandoned.
         */
        public ConversationOptions(
                @Nullable String prefix,
                boolean localEcho,
                boolean modal,
                @Range(from = 0, to = Integer.MAX_VALUE) int timeOut,
                boolean escapeWordsCaseSensitive,
                String... escapeWords
        ) {
            if (prefix == null) {
                this.rawPrefix = "";
                this.prefix = new NullConversationPrefix();
            } else {
                this.rawPrefix = ColorTranslator.translateColorCodes(prefix);
                this.prefix = (context) -> rawPrefix;
            }

            this.localEcho = localEcho;
            this.modal = modal;
            this.timeOut = timeOut;
            this.escapeWordsCaseSensitive = escapeWordsCaseSensitive;

            if (escapeWords == null || escapeWords.length < 1) {
                this.escapeWords = new String[]{
                        "cancel", "exit", "quit", "escape", "done"
                };
            } else this.escapeWords = escapeWords;
        }
    }
}
