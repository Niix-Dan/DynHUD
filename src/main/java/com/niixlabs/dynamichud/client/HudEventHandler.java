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
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = DynHUD.MODID, value = Dist.CLIENT)
public class HudEventHandler {

    private static final class ElementState {
        float progress = 1.0f;
        int inactivityTicks = 0;
    }

    private static final Map<HudElement, ElementState> STATES = new EnumMap<>(HudElement.class);
    private static final Map<Identifier, HudElement> LAYER_LOOKUP = new HashMap<>();

    static {
        for (HudElement element : HudElement.values()) {
            STATES.put(element, new ElementState());
            LAYER_LOOKUP.put(element.layerId, element);
        }
    }

    private static int lastSlot = -1;
    private static float lastHealth = -1;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;

        Map<HudTrigger, Boolean> fired = new EnumMap<>(HudTrigger.class);

        boolean itemChanged = player.getInventory().getSelectedSlot() != lastSlot;
        lastSlot = player.getInventory().getSelectedSlot();
        fired.put(HudTrigger.ITEM_CHANGE, itemChanged);

        boolean damaged = player.getHealth() < lastHealth;
        lastHealth = player.getHealth();
        fired.put(HudTrigger.DAMAGE, damaged);

        fired.put(HudTrigger.NOT_FULL_HEALTH, player.getHealth() < player.getMaxHealth());
        fired.put(HudTrigger.COMBAT, player.swingTime > 0 || player.hurtTime > 0);
        fired.put(HudTrigger.ITEM_USE, player.isUsingItem());
        fired.put(HudTrigger.SNEAK, mc.options.keyShift.isDown());
        fired.put(HudTrigger.SCREEN, mc.screen != null);

        for (HudElement element : HudElement.values()) {
            ElementState state = STATES.get(element);
            Map<HudTrigger, ModConfigSpec.BooleanValue> triggerConfig = DynHUDConfig.TRIGGERS.get(element);

            boolean interacted = false;
            for (HudTrigger trigger : HudTrigger.values()) {
                if (triggerConfig.get(trigger).get() && Boolean.TRUE.equals(fired.get(trigger))) {
                    interacted = true;
                    break;
                }
            }

            if (interacted) {
                state.inactivityTicks = 0;
            } else {
                state.inactivityTicks++;
            }

            int hideDelay = DynHUDConfig.HIDE_DELAY.get(element).get();

            if (state.inactivityTicks > hideDelay) {
                float outSpeed = DynHUDConfig.SLIDE_OUT_SPEED.get(element).get().floatValue();
                state.progress = Math.max(0.0f, state.progress - outSpeed);
            } else {
                float inSpeed = DynHUDConfig.SLIDE_IN_SPEED.get(element).get().floatValue();
                state.progress = Math.min(1.0f, state.progress + inSpeed);
            }
        }
    }

    @SubscribeEvent
    public static void onPreRenderLayer(RenderGuiLayerEvent.Pre event) {
        HudElement element = LAYER_LOOKUP.get(event.getName());
        if (element == null || !DynHUDConfig.ENABLED.get(element).get()) return;

        ElementState state = STATES.get(element);

        if (state.progress <= 0.0f) {
            event.setCanceled(true);
        } else if (state.progress < 1.0f) {
            float offsetMax = DynHUDConfig.SLIDE_OFFSET.get(element).get().floatValue();
            float currentOffset = Mth.lerp(state.progress, offsetMax, 0.0f);

            event.getGuiGraphics().pose().pushMatrix();
            event.getGuiGraphics().pose().translate(0, currentOffset);
        }
    }

    @SubscribeEvent
    public static void onPostRenderLayer(RenderGuiLayerEvent.Post event) {
        HudElement element = LAYER_LOOKUP.get(event.getName());
        if (element == null || !DynHUDConfig.ENABLED.get(element).get()) return;

        ElementState state = STATES.get(element);

        if (state.progress < 1.0f && state.progress > 0.0f) {
            event.getGuiGraphics().pose().popMatrix();
        }
    }
}