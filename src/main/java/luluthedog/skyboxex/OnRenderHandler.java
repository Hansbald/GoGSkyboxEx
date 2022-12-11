package luluthedog.skyboxex;


import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import vazkii.skybox.SkyblockSkyRenderer;
import java.util.HashSet;



public class OnRenderHandler {
    public static HashSet<Integer> dimWhitelistSet = new HashSet<Integer>();
    @SubscribeEvent
    public static void onRender(final RenderWorldLastEvent event) {
        final World world = (World) Minecraft.getMinecraft().world;
        if (dimWhitelistSet.contains(world.provider.getDimension()) && !(world.provider.getSkyRenderer() instanceof SkyblockSkyRenderer)) {
            GoGSkyboxEx.logger.log(Level.INFO, String.format("Setting GoG Renderer for Dimension %d", world.provider.getDimension()));
            world.provider.setSkyRenderer((IRenderHandler)new SkyblockSkyRenderer());
        }
    }

    @Config(modid = GoGSkyboxEx.MOD_ID)
    public static class SkyboxConfig
    {
        @Config.Comment({
                "Add Dimension that you want the GoG Skybox to be active in"
        })
        @Config.Name("Dimension Whitelist")
        public static Integer[] dimWhitelistArray = {0};
    }
}
