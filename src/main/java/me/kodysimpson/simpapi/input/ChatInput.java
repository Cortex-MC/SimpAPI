package me.kodysimpson.simpapi.input;

import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChatInput {

    /**
     * Request input from a player with custom validation
     * @param plugin Your plugin instance
     * @param player The player to request input from
     * @param prompt The message to show the player
     * @param validator Custom validation function that returns true if input is valid
     * @param onInput Callback function to handle the validated input
     * @param onCancel Optional callback for when input is cancelled
     */
    public static void requestInput(Plugin plugin, Player player, String prompt,
                                    Predicate<String> validator, Consumer<String> onInput, Runnable onCancel) {

        ConversationFactory factory = new ConversationFactory(plugin)
                .withModality(true)
                .withLocalEcho(false)
                .withPrefix(context -> "§6§l[Input] §r")
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public String getPromptText(ConversationContext context) {
                        return prompt;
                    }

                    @Override
                    public Prompt acceptInput(ConversationContext context, String input) {
                        if (input.equalsIgnoreCase("cancel")) {
                            context.getForWhom().sendRawMessage("§cInput cancelled.");
                            if (onCancel != null) {
                                plugin.getServer().getScheduler().runTask(plugin, onCancel);
                            }
                            return END_OF_CONVERSATION;
                        }

                        if (validator.test(input)) {
                            plugin.getServer().getScheduler().runTask(plugin, () -> onInput.accept(input));
                            return END_OF_CONVERSATION;
                        }
                        return this;
                    }
                })
                .withEscapeSequence("cancel")
                .thatExcludesNonPlayersWithMessage("Only players can provide input!")
                .addConversationAbandonedListener(event -> {
                    if (!event.gracefulExit() && onCancel != null) {
                        plugin.getServer().getScheduler().runTask(plugin, onCancel);
                    }
                });

        factory.buildConversation(player).begin();
    }

    /**
     * Request simple text input from a player
     * @param plugin Your plugin instance
     * @param player The player to request input from
     * @param prompt The message to show the player
     * @param onInput Callback function to handle the input
     */
    public static void requestInput(Plugin plugin, Player player, String prompt, Consumer<String> onInput) {
        requestInput(plugin, player, prompt, input -> true, onInput, null);
    }

    /**
     * Request numeric input from a player
     * @param plugin Your plugin instance
     * @param player The player to request input from
     * @param prompt The message to show the player
     * @param onInput Callback function to handle the validated numeric input
     */
    public static void requestNumericInput(Plugin plugin, Player player, String prompt, Consumer<Double> onInput) {
        requestInput(plugin, player, prompt,
                input -> {
                    try {
                        Double.parseDouble(input);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                input -> onInput.accept(Double.parseDouble(input)),
                null
        );
    }
}