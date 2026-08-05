package com.niixlabs.dynamichud.client;

import com.niixlabs.dynamichud.DynHUD;
import com.niixlabs.dynamichud.DynHUDConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = DynHUD.MODID, value = Dist.CLIENT)
public class HudEventHandler {
    private static int inactivityTicks = 0;
    private static int lastSlot = -1;
    private static float lastHealth = -1;
    private static float currentProgress = 1.0f;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;

        boolean interacted = false;

        if (DynHUDConfig.TRIGGER_ITEM_CHANGE.get()) {
            if (player.getInventory().getSelectedSlot() != lastSlot) {
                lastSlot = player.getInventory().getSelectedSlot();
                interacted = true;
            }
        } else {
            lastSlot = player.getInventory().getSelectedSlot();
        }

        if (DynHUDConfig.TRIGGER_DAMAGE.get() && player.getHealth() < lastHealth) {
            interacted = true;
        }
        lastHealth = player.getHealth();

        if (DynHUDConfig.TRIGGER_NOT_FULL_HEALTH.get() && player.getHealth() < player.getMaxHealth()) {
            interacted = true;
        }

        if (DynHUDConfig.TRIGGER_COMBAT.get() && (player.swingTime > 0 || player.hurtTime > 0)) {
            interacted = true;
        }

        if (DynHUDConfig.TRIGGER_ITEM_USE.get() && player.isUsingItem()) {
            interacted = true;
        }

        if (DynHUDConfig.TRIGGER_SNEAK.get() && mc.options.keyShift.isDown()) {
            interacted = true;
        }

        if (DynHUDConfig.TRIGGER_SCREEN.get() && mc.screen != null) {
            interacted = true;
        }

        if (interacted) {
            inactivityTicks = 0;
        } else {
            inactivityTicks++;
        }

        if (inactivityTicks > DynHUDConfig.HIDE_DELAY.get()) {
            float outSpeed = DynHUDConfig.SLIDE_OUT_SPEED.get().floatValue();
            currentProgress = Math.max(0.0f, currentProgress - outSpeed);
        } else {
            float inSpeed = DynHUDConfig.SLIDE_IN_SPEED.get().floatValue();
            currentProgress = Math.min(1.0f, currentProgress + inSpeed);
        }
    }

    @SubscribeEvent
    public static void onPreRenderLayer(RenderGuiLayerEvent.Pre event) {
        Identifier layer = event.getName();

        if (isDynamicLayer(layer)) {
            if (currentProgress <= 0.0f) {
                event.setCanceled(true);
            } else if (currentProgress < 1.0f) {
                float offsetMax = DynHUDConfig.SLIDE_OFFSET.get().floatValue();
                float currentOffset = Mth.lerp(currentProgress, offsetMax, 0.0f);

                event.getGuiGraphics().pose().pushMatrix();
                event.getGuiGraphics().pose().translate(0, currentOffset);
            }
        }
    }

    @SubscribeEvent
    public static void onPostRenderLayer(RenderGuiLayerEvent.Post event) {
        Identifier layer = event.getName();

        if (isDynamicLayer(layer)) {
            if (currentProgress < 1.0f && currentProgress > 0.0f) {
                event.getGuiGraphics().pose().popMatrix();
            }
        }
    }

    private static boolean isDynamicLayer(Identifier layer) {
        if (layer.equals(VanillaGuiLayers.HOTBAR)) return DynHUDConfig.AFFECT_HOTBAR.get();
        if (layer.equals(VanillaGuiLayers.PLAYER_HEALTH)) return DynHUDConfig.AFFECT_HEALTH.get();
        if (layer.equals(VanillaGuiLayers.FOOD_LEVEL)) return DynHUDConfig.AFFECT_FOOD.get();
        if (layer.equals(VanillaGuiLayers.ARMOR_LEVEL)) return DynHUDConfig.AFFECT_ARMOR.get();
        if (layer.equals(VanillaGuiLayers.AIR_LEVEL)) return DynHUDConfig.AFFECT_AIR.get();
        if (layer.equals(VanillaGuiLayers.CONTEXTUAL_INFO_BAR_BACKGROUND)) return DynHUDConfig.AFFECT_INFO_BAR_BG.get();
        if (layer.equals(VanillaGuiLayers.CONTEXTUAL_INFO_BAR)) return DynHUDConfig.AFFECT_INFO_BAR.get();
        if (layer.equals(VanillaGuiLayers.EXPERIENCE_LEVEL)) return DynHUDConfig.AFFECT_XP_LEVEL.get();
        if (layer.equals(VanillaGuiLayers.VEHICLE_HEALTH)) return DynHUDConfig.AFFECT_VEHICLE_HEALTH.get();

        return false;
    }
}