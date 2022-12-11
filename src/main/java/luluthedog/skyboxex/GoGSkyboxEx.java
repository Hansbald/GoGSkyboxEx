//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Lulu\Downloads\mcp_stable-39-1.12"!

//Decompiled by Procyon!

package luluthedog.skyboxex;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.skybox.ModEventHandler;
import java.util.Collections;


@Mod(modid = GoGSkyboxEx.MOD_ID, name = GoGSkyboxEx.MOD_NAME, version = GoGSkyboxEx.VERSION, clientSideOnly = true)
public class GoGSkyboxEx
{
    public static final String MOD_ID = "gogskybox";
    public static final String MOD_NAME = "Garden of Glass Skybox Extended";
    public static final String VERSION = "1.1";
    public static final Logger logger = LogManager.getLogger(GoGSkyboxEx.MOD_ID);
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        logger.log(Level.INFO, "Converting Integer Array into HashSet for O(1) .contains");
        Collections.addAll(OnRenderHandler.dimWhitelistSet, OnRenderHandler.SkyboxConfig.dimWhitelistArray);
        logger.log(Level.DEBUG, "Registering EventHandlers");
        MinecraftForge.EVENT_BUS.register((Object) ModEventHandler.class);
        MinecraftForge.EVENT_BUS.register((Object) OnRenderHandler.class);
        logger.log(Level.INFO, "Initialization finished!");
    }
}
