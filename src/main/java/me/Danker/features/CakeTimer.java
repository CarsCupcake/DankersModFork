package me.Danker.features;

import me.Danker.commands.MoveCommand;
import me.Danker.commands.ScaleCommand;
import me.Danker.config.ModConfig;
import me.Danker.events.RenderOverlayEvent;
import me.Danker.handlers.ConfigHandler;
import me.Danker.handlers.TextRenderer;
import me.Danker.utils.RenderUtils;
import me.Danker.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class CakeTimer {

    public static double cakeTime;
    public static final ResourceLocation CAKE_ICON = new ResourceLocation("dsm", "icons/cake.png");

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = StringUtils.stripControlCodes(event.message.getUnformattedText());

        if (!Utils.inSkyblock) return;
        if (message.contains(":")) return;

        if (message.contains("Yum! You gain +") && message.contains(" for 48 hours!")) {
            cakeTime = System.currentTimeMillis() / 1000 + 172800; // Add 48 hours
            ConfigHandler.writeDoubleConfig("misc", "cakeTime", cakeTime);
        }
    }

    @SubscribeEvent
    public void renderPlayerInfo(RenderOverlayEvent event) {
        if (ModConfig.cakeTimer && Utils.inSkyblock) {
            Minecraft mc = Minecraft.getMinecraft();
            double scale = ScaleCommand.cakeTimerScale;
            double scaleReset = Math.pow(scale, -1);
            GL11.glScaled(scale, scale, scale);

            double timeNow = System.currentTimeMillis() / 1000;
            mc.getTextureManager().bindTexture(CAKE_ICON);
            RenderUtils.drawModalRectWithCustomSizedTexture(MoveCommand.cakeTimerXY[0] / scale, MoveCommand.cakeTimerXY[1] / scale, 0, 0, 16, 16, 16, 16);

            GL11.glScaled(scaleReset, scaleReset, scaleReset);
            String cakeText;
            if (cakeTime - timeNow < 0) {
                cakeText = EnumChatFormatting.RED + "NONE";
            } else {
                cakeText = ModConfig.getColour(ModConfig.cakeColour) + Utils.getTimeBetween(timeNow, cakeTime);
            }

            new TextRenderer(mc, cakeText, MoveCommand.cakeTimerXY[0] + 20 * scale, MoveCommand.cakeTimerXY[1] + 5 * scale, scale);
        }
    }

}
