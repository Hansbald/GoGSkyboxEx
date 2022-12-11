package vazkii.skybox;

import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;

public class ModEventHandler
{
    public static int ticksInGame;
    public static float partialTicks;
    public static float delta;
    public static float total;
    
    private static void calcDelta() {
        final float oldTotal = ModEventHandler.total;
        ModEventHandler.total = ModEventHandler.ticksInGame + ModEventHandler.partialTicks;
        ModEventHandler.delta = ModEventHandler.total - oldTotal;
    }
    
    @SubscribeEvent
    public static void renderTick(final TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ModEventHandler.partialTicks = event.renderTickTime;
        }
    }
    
    @SubscribeEvent
    public static void clientTickEnd(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            final Minecraft mc = Minecraft.getMinecraft();
            final GuiScreen gui = mc.currentScreen;
            if (gui == null || !gui.doesGuiPauseGame()) {
                ++ModEventHandler.ticksInGame;
                ModEventHandler.partialTicks = 0.0f;
            }
            calcDelta();
        }
    }

    static {
        ModEventHandler.ticksInGame = 0;
        ModEventHandler.partialTicks = 0.0f;
        ModEventHandler.delta = 0.0f;
        ModEventHandler.total = 0.0f;
    }
}
