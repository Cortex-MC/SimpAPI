package me.kodysimpson.simpapi.conversations;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.InactivityConversationCanceller;
import org.bukkit.entity.Player;

import java.util.Objects;

//A nice conversation abandoned listener that prefixes messages with the conversation's prefix
//Detects a few different conversation cancellers and writes a smarter message versus "conversation abandoned"

/**
 * @author muunitnocQ
 */
final class PrefixedAbandonedListener implements ConversationAbandonedListener {

    private final String prefix;

    PrefixedAbandonedListener(String prefix) {
        this.prefix = prefix;
    }

    private static String tl(String msg) {
        return ColorTranslator.translateColorCodes(msg);
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
        if (abandonedEvent.gracefulExit()) return;

        Player player = (Player) abandonedEvent.getContext().getForWhom();
        ConversationCanceller canceller = Objects.requireNonNull(abandonedEvent.getCanceller(), "[m.k.s.c.ArrayMatchCanceller] Canceller is null but gracefulExit is false");

        //JEP 406 can't land soon enough

        //The player entered one of the escape words.
        if (canceller instanceof ArrayMatchCanceller) {
            player.sendMessage(tl(prefix + "&9Good bye!"));
            return;
        }

        //Server is reloading, abandon the conversation
        if (canceller instanceof ServerReloadListener.ServerReloadCanceller) {
            player.sendMessage(tl(prefix + "&9Server reloaded, quitting."));
            return;
        }

        //Conversation timed out
        if (canceller instanceof InactivityConversationCanceller) {
            player.sendMessage(tl(prefix + "&9Session timed out."));
            return;
        }

        //Unknown canceller
        player.sendMessage(tl(prefix + "&9Conversation cancelled by &#FF00FFcosmic energy&9."));
    }

}
