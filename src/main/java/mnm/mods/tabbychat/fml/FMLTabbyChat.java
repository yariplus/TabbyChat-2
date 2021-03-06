package mnm.mods.tabbychat.fml;

import java.io.File;
import java.net.SocketAddress;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.TabbyRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

@Mod(modid = TabbyRef.MOD_ID, name = TabbyRef.MOD_NAME, version = TabbyRef.MOD_VERSION)
public class FMLTabbyChat extends TabbyChat {

    private File tempDir;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        tempDir = event.getModConfigurationDirectory();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (getAPI() == null) {
            setInstance(this);
            this.setConfigFolder(tempDir);
            FMLCommonHandler.instance().bus().register(this);
            init();
        } else {
            getLogger().info("TabbyChat already initialized. Not registering FML events.");
        }
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent event) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        onRender(currentScreen);
    }

    @SubscribeEvent
    public void onJoin(ClientConnectedToServerEvent event) {
        if (event.isLocal) {
            onJoin((SocketAddress) null);
        } else {
            onJoin(event.manager.getRemoteAddress());
        }
    }
}
