package mnm.mods.tabbychat.core;

import java.awt.Rectangle;
import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedFilterEvent;
import mnm.mods.tabbychat.gui.ChatBox;
import mnm.mods.tabbychat.settings.ChatBoxSettings;
import mnm.mods.tabbychat.util.ChatChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;

import org.lwjgl.input.Mouse;

public class GuiNewChatTC extends GuiNewChat {

    private static GuiNewChatTC instance;
    private ChatBox chatbox;

    private TabbyChat tc = TabbyChat.getInstance();

    private GuiNewChatTC(Minecraft minecraft) {
        super(minecraft);
        Rectangle rect = new Rectangle();

        ChatBoxSettings settings = TabbyChat.getInstance().chatSettings;
        rect.x = settings.xPos.getValue();
        rect.y = settings.yPos.getValue();
        rect.width = settings.width.getValue();
        rect.height = settings.height.getValue();

        chatbox = new ChatBox(rect);
    }

    public static GuiNewChatTC getInstance() {
        if (instance == null)
            instance = new GuiNewChatTC(Minecraft.getMinecraft());
        return instance;
    }

    @Override
    public void refreshChat() {
        // TODO Auto-generated method stub
        chatbox.updateComponent();
    }

    @Override
    public void drawChat(int i) {
        int mouseX = Mouse.getEventX();
        int mouseY = -Mouse.getEventY() - 1;
        chatbox.drawComponent(mouseX, mouseY);
    }

    @Override
    public void printChatMessageWithOptionalDeletion(IChatComponent chat, int id) {
        // chat listeners
        ChatRecievedEvent chatevent = new ChatRecievedEvent(chat, id);
        chatevent.channels.add(ChatChannel.DEFAULT_CHANNEL);
        tc.getEventManager().onChatRecieved(chatevent);
        // chat filters
        ChatRecievedFilterEvent chatfilter = new ChatRecievedFilterEvent(chat, id);
        tc.getEventManager().onChatReicevedFilter(chatfilter);
        chat = chatfilter.chat;
        id = chatfilter.id;
        if (chat != null && !chat.getUnformattedText().isEmpty()) {
            for (Channel channel : chatevent.channels) {
                channel.addMessage(chat, id);
            }
        }
    }

    @Override
    public void resetScroll() {
        chatbox.getChatArea().resetScroll();
        super.resetScroll();
    }

    @Override
    public void clearChatMessages() {
        chatbox.clearMessages();
        super.clearChatMessages();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getSentMessages() {
        return super.getSentMessages();
    }

    public ChatBox getChatbox() {
        return chatbox;
    }

    @Override
    public IChatComponent getChatComponent(int clickX, int clickY) {
        return chatbox.getChatArea().getChatComponent(clickX, clickY);
    }

    @Override
    public int getChatWidth() {
        return chatbox.getWidth();
    }
}
