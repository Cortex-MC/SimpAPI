package me.kodysimpson.simpapi.conversations;

import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

//Addresses a bug with conversations where, upon server reload, active conversations enter a "zombie" state.
//In this state, the conversation is *still* alive, but the player cannot interact with it normally.
//This class detects when the conversation's owning plugin is disabled (/reload, plugin managers, etc) and abandons
//the associated conversation.

/**
 * @author muunitnocQ
 */
final class ServerReloadListener implements Listener {

    final ServerReloadCanceller canceller;
    private final Class<? extends Plugin> pluginClazz;

    ServerReloadListener(Plugin plugin, Conversation convo) {
        this.pluginClazz = plugin.getClass();
        this.canceller = new ServerReloadCanceller();

        convo.getCancellers().add(canceller);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onReload(PluginDisableEvent event) {
        if (event.getPlugin().getClass() != pluginClazz) return;
        canceller.forceAbandon();
    }

    //Dummy implementation save for reloadCancel
    static final class ServerReloadCanceller implements ConversationCanceller {
        private Conversation conversation;

        @Override
        public void setConversation(@NotNull Conversation conversation) {
            this.conversation = conversation;
        }

        @Override
        public boolean cancelBasedOnInput(@NotNull ConversationContext context, @NotNull String input) {
            return false;
        }

        @Override
        public ConversationCanceller clone() {
            return this;
        }

        //A "backdoor" into the ConversationCanceller API so the listener can notify this instance when to abandon
        //the conversation.
        void forceAbandon() {
            conversation.abandon(new ConversationAbandonedEvent(conversation, this));
        }

    }
}
