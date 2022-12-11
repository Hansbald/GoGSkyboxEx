package vazkii.skybox;

import net.minecraft.client.renderer.*;
import net.minecraftforge.fml.relauncher.*;
import java.lang.invoke.*;
import java.lang.reflect.*;

public final class ModMethodHandles
{
    public static MethodHandle starGLCallList_getter;
    public static MethodHandle starVBO_getter;
    public static MethodHandle glSkyList_getter;
    public static MethodHandle skyVBO_getter;
    public static final String[] STAR_GL_CALL_LIST;
    public static final String[] STAR_VBO;
    public static final String[] GL_SKY_LIST;
    public static final String[] SKY_VBO;
    
    static {
        STAR_GL_CALL_LIST = new String[] { "starGLCallList", "starGLCallList", "p" };
        STAR_VBO = new String[] { "starVBO", "starVBO", "t" };
        GL_SKY_LIST = new String[] { "glSkyList", "glSkyList", "q" };
        SKY_VBO = new String[] { "skyVBO", "skyVBO", "u" };
        try {
            Field f = ReflectionHelper.findField((Class)RenderGlobal.class, ModMethodHandles.STAR_GL_CALL_LIST);
            f.setAccessible(true);
            ModMethodHandles.starGLCallList_getter = MethodHandles.publicLookup().unreflectGetter(f);
            f = ReflectionHelper.findField((Class)RenderGlobal.class, ModMethodHandles.STAR_VBO);
            f.setAccessible(true);
            ModMethodHandles.starVBO_getter = MethodHandles.publicLookup().unreflectGetter(f);
            f = ReflectionHelper.findField((Class)RenderGlobal.class, ModMethodHandles.GL_SKY_LIST);
            f.setAccessible(true);
            ModMethodHandles.glSkyList_getter = MethodHandles.publicLookup().unreflectGetter(f);
            f = ReflectionHelper.findField((Class)RenderGlobal.class, ModMethodHandles.SKY_VBO);
            f.setAccessible(true);
            ModMethodHandles.skyVBO_getter = MethodHandles.publicLookup().unreflectGetter(f);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("Failiure in getting class data for the Garden of Glass Skybox", e);
        }
    }
}
