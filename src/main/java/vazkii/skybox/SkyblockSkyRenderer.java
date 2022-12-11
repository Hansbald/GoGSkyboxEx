package vazkii.skybox;

import net.minecraftforge.client.*;
import net.minecraft.util.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.*;
import java.util.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.*;

public class SkyblockSkyRenderer extends IRenderHandler
{
    private static final ResourceLocation textureSkybox;
    private static final ResourceLocation textureRainbow;
    private static final ResourceLocation MOON_PHASES_TEXTURES;
    private static final ResourceLocation SUN_TEXTURES;
    private static final ResourceLocation[] planetTextures;
    
    public void render(final float partialTicks, final WorldClient world, final Minecraft mc) {
        int glSkyList;
        VertexBuffer skyVBO;
        try {
            glSkyList = (int) ModMethodHandles.glSkyList_getter.invokeExact(mc.renderGlobal);
            skyVBO = (VertexBuffer) ModMethodHandles.skyVBO_getter.invokeExact(mc.renderGlobal);
        }
        catch (Throwable t) {
            return;
        }
        GlStateManager.disableTexture2D();
        final Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
        float f = (float)vec3d.x;
        float f2 = (float)vec3d.y;
        float f3 = (float)vec3d.z;
        float insideVoid = 0.0f;
        if (mc.player.posY <= -2.0) {
            insideVoid = (float)Math.min(1.0, -(mc.player.posY + 2.0) / 30.0);
        }
        f = Math.max(0.0f, f - insideVoid);
        f2 = Math.max(0.0f, f2 - insideVoid);
        f3 = Math.max(0.0f, f3 - insideVoid);
        GlStateManager.color(f, f2, f3);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.depthMask(false);
        GlStateManager.enableFog();
        GlStateManager.color(f, f2, f3);
        if (OpenGlHelper.useVbo()) {
            skyVBO.bindBuffer();
            GlStateManager.glEnableClientState(32884);
            GlStateManager.glVertexPointer(3, 5126, 12, 0);
            skyVBO.drawArrays(7);
            skyVBO.unbindBuffer();
            GlStateManager.glDisableClientState(32884);
        }
        else {
            GlStateManager.callList(glSkyList);
        }
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        final float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
        if (afloat != null) {
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate((MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0f) ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            final float f4 = afloat[0];
            final float f5 = afloat[1];
            final float f6 = afloat[2];
            vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            vertexbuffer.pos(0.0, 100.0, 0.0).color(f4, f5, f6, afloat[3] * (1.0f - insideVoid)).endVertex();
            for (int l = 0; l <= 16; ++l) {
                final float f7 = l * 6.2831855f / 16.0f;
                final float f8 = MathHelper.sin(f7);
                final float f9 = MathHelper.cos(f7);
                vertexbuffer.pos((double)(f8 * 120.0f), (double)(f9 * 120.0f), (double)(-f9 * 40.0f * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0f).endVertex();
            }
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        float f10 = 1.0f - world.getRainStrength(partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, f10);
        GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
        float effCelAng;
        final float celAng = effCelAng = world.getCelestialAngle(partialTicks);
        if (celAng > 0.5) {
            effCelAng = 0.5f - (celAng - 0.5f);
        }
        float f11 = 20.0f;
        final float lowA = Math.max(0.0f, effCelAng - 0.3f) * f10;
        float a = Math.max(0.1f, lowA);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, a * 4.0f * (1.0f - insideVoid));
        GlStateManager.rotate(90.0f, 0.5f, 0.5f, 0.0f);
        for (int p = 0; p < SkyblockSkyRenderer.planetTextures.length; ++p) {
            mc.renderEngine.bindTexture(SkyblockSkyRenderer.planetTextures[p]);
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
            tessellator.getBuffer().pos((double)(-f11), 100.0, (double)(-f11)).tex(0.0, 0.0).endVertex();
            tessellator.getBuffer().pos((double)f11, 100.0, (double)(-f11)).tex(1.0, 0.0).endVertex();
            tessellator.getBuffer().pos((double)f11, 100.0, (double)f11).tex(1.0, 1.0).endVertex();
            tessellator.getBuffer().pos((double)(-f11), 100.0, (double)f11).tex(0.0, 1.0).endVertex();
            tessellator.draw();
            switch (p) {
                case 0: {
                    GlStateManager.rotate(70.0f, 1.0f, 0.0f, 0.0f);
                    f11 = 12.0f;
                    break;
                }
                case 1: {
                    GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
                    f11 = 15.0f;
                    break;
                }
                case 2: {
                    GlStateManager.rotate(80.0f, 1.0f, 0.0f, 1.0f);
                    f11 = 25.0f;
                    break;
                }
                case 3: {
                    GlStateManager.rotate(100.0f, 0.0f, 0.0f, 1.0f);
                    f11 = 10.0f;
                    break;
                }
                case 4: {
                    GlStateManager.rotate(-60.0f, 1.0f, 0.0f, 0.5f);
                    f11 = 40.0f;
                    break;
                }
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        mc.renderEngine.bindTexture(SkyblockSkyRenderer.textureSkybox);
        f11 = 20.0f;
        a = lowA;
        GlStateManager.pushMatrix();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        GlStateManager.rotate(220.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, a);
        final int angles = 90;
        final float y = 2.0f;
        final float y2 = 0.0f;
        final float uPer = 0.0027777778f;
        final float anglePer = 360.0f / angles;
        double fuzzPer = 31.41592653589793 / angles;
        float rotSpeed = 1.0f;
        final float rotSpeedMod = 0.4f;
        for (int p2 = 0; p2 < 3; ++p2) {
            final float baseAngle = rotSpeed * rotSpeedMod * (ModEventHandler.ticksInGame + ModEventHandler.partialTicks);
            GlStateManager.rotate((ModEventHandler.ticksInGame + ModEventHandler.partialTicks) * 0.25f * rotSpeed * rotSpeedMod, 0.0f, 1.0f, 0.0f);
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
            for (int i = 0; i < angles; ++i) {
                int j = i;
                if (i % 2 == 0) {
                    --j;
                }
                final float ang = j * anglePer + baseAngle;
                final double xp = Math.cos(ang * 3.141592653589793 / 180.0) * f11;
                final double zp = Math.sin(ang * 3.141592653589793 / 180.0) * f11;
                final double yo = Math.sin(fuzzPer * j) * 1.0;
                final float ut = ang * uPer;
                if (i % 2 == 0) {
                    tessellator.getBuffer().pos(xp, yo + y2 + y, zp).tex((double)ut, 1.0).endVertex();
                    tessellator.getBuffer().pos(xp, yo + y2, zp).tex((double)ut, 0.0).endVertex();
                }
                else {
                    tessellator.getBuffer().pos(xp, yo + y2, zp).tex((double)ut, 0.0).endVertex();
                    tessellator.getBuffer().pos(xp, yo + y2 + y, zp).tex((double)ut, 1.0).endVertex();
                }
            }
            tessellator.draw();
            switch (p2) {
                case 0: {
                    GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.color(1.0f, 0.4f, 0.4f, a);
                    fuzzPer = 43.982297150257104 / angles;
                    rotSpeed = 0.2f;
                    break;
                }
                case 1: {
                    GlStateManager.rotate(50.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.color(0.4f, 1.0f, 0.7f, a);
                    fuzzPer = 18.84955592153876 / angles;
                    rotSpeed = 2.0f;
                    break;
                }
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        mc.renderEngine.bindTexture(SkyblockSkyRenderer.textureRainbow);
        f11 = 10.0f;
        float effCelAng2 = celAng;
        if (effCelAng2 > 0.25f) {
            effCelAng2 = 1.0f - effCelAng2;
        }
        effCelAng2 = 0.25f - Math.min(0.25f, effCelAng2);
        final long time = world.getWorldTime() + 1000L;
        final int day = (int)(time / 24000L);
        final Random rand = new Random(day * 255);
        final float angle1 = rand.nextFloat() * 360.0f;
        final float angle2 = rand.nextFloat() * 360.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f, effCelAng2 * (1.0f - insideVoid));
        GlStateManager.rotate(angle1, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(angle2, 0.0f, 0.0f, 1.0f);
        tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int k = 0; k < angles; ++k) {
            int m = k;
            if (k % 2 == 0) {
                --m;
            }
            final float ang2 = m * anglePer;
            final double xp2 = Math.cos(ang2 * 3.141592653589793 / 180.0) * f11;
            final double zp2 = Math.sin(ang2 * 3.141592653589793 / 180.0) * f11;
            final double yo2 = 0.0;
            final float ut2 = ang2 * uPer;
            if (k % 2 == 0) {
                tessellator.getBuffer().pos(xp2, yo2 + y2 + y, zp2).tex((double)ut2, 1.0).endVertex();
                tessellator.getBuffer().pos(xp2, yo2 + y2, zp2).tex((double)ut2, 0.0).endVertex();
            }
            else {
                tessellator.getBuffer().pos(xp2, yo2 + y2, zp2).tex((double)ut2, 0.0).endVertex();
                tessellator.getBuffer().pos(xp2, yo2 + y2 + y, zp2).tex((double)ut2, 1.0).endVertex();
            }
        }
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f - insideVoid);
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f);
        f11 = 60.0f;
        mc.renderEngine.bindTexture(SkyblockSkyRenderer.SUN_TEXTURES);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(-f11), 100.0, (double)(-f11)).tex(0.0, 0.0).endVertex();
        vertexbuffer.pos((double)f11, 100.0, (double)(-f11)).tex(1.0, 0.0).endVertex();
        vertexbuffer.pos((double)f11, 100.0, (double)f11).tex(1.0, 1.0).endVertex();
        vertexbuffer.pos((double)(-f11), 100.0, (double)f11).tex(0.0, 1.0).endVertex();
        tessellator.draw();
        f11 = 60.0f;
        mc.renderEngine.bindTexture(SkyblockSkyRenderer.MOON_PHASES_TEXTURES);
        int k = world.getMoonPhase();
        final int k2 = k % 4;
        final int i2 = k / 4 % 2;
        final float f12 = (k2 + 0) / 4.0f;
        final float f13 = (i2 + 0) / 2.0f;
        final float f14 = (k2 + 1) / 4.0f;
        final float f15 = (i2 + 1) / 2.0f;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(-f11), -100.0, (double)f11).tex((double)f14, (double)f15).endVertex();
        vertexbuffer.pos((double)f11, -100.0, (double)f11).tex((double)f12, (double)f15).endVertex();
        vertexbuffer.pos((double)f11, -100.0, (double)(-f11)).tex((double)f12, (double)f13).endVertex();
        vertexbuffer.pos((double)(-f11), -100.0, (double)(-f11)).tex((double)f14, (double)f13).endVertex();
        tessellator.draw();
        GlStateManager.disableTexture2D();
        f10 *= Math.max(0.1f, effCelAng * 2.0f);
        this.renderStars(mc, f10, partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
    }
    
    private void renderStars(final Minecraft mc, final float alpha, final float partialTicks) {
        int starGLCallList;
        VertexBuffer starVBO;
        try {
            starGLCallList = (int) ModMethodHandles.starGLCallList_getter.invokeExact(mc.renderGlobal);
            starVBO = (VertexBuffer) ModMethodHandles.starVBO_getter.invokeExact(mc.renderGlobal);
        }
        catch (Throwable t2) {
            return;
        }
        final float t = (ModEventHandler.ticksInGame + partialTicks + 2000.0f) * 0.005f;
        GlStateManager.pushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t * 3.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t, 0.0f, 1.0f, 0.0f);
        GlStateManager.color(0.5f, 1.0f, 1.0f, alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t * 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.color(1.0f, 0.75f, 0.75f, alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t * 3.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.25f * alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t, 0.0f, 0.0f, 1.0f);
        GlStateManager.color(0.5f, 1.0f, 1.0f, 0.25f * alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t * 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.color(1.0f, 0.75f, 0.75f, 0.25f * alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
    
    private void drawVboOrList(final VertexBuffer vbo, final int displayList) {
        if (OpenGlHelper.useVbo()) {
            vbo.bindBuffer();
            GlStateManager.glEnableClientState(32884);
            GlStateManager.glVertexPointer(3, 5126, 12, 0);
            vbo.drawArrays(7);
            vbo.unbindBuffer();
            GlStateManager.glDisableClientState(32884);
        }
        else {
            GlStateManager.callList(displayList);
        }
    }
    
    static {
        textureSkybox = new ResourceLocation("gogskybox:textures/skybox.png");
        textureRainbow = new ResourceLocation("gogskybox:textures/rainbow.png");
        MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
        SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
        planetTextures = new ResourceLocation[] {
                new ResourceLocation("gogskybox:textures/planet0.png"),
                new ResourceLocation("gogskybox:textures/planet1.png"),
                new ResourceLocation("gogskybox:textures/planet2.png"),
                new ResourceLocation("gogskybox:textures/planet3.png"),
                new ResourceLocation("gogskybox:textures/planet4.png"),
                new ResourceLocation("gogskybox:textures/planet5.png")
        };
    }
}
