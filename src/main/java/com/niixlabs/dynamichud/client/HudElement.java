package com.niixlabs.dynamichud.client;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public enum HudElement {
    HOTBAR("hotbar", VanillaGuiLayers.HOTBAR),
    HEALTH("health", VanillaGuiLayers.PLAYER_HEALTH),
    FOOD("food", VanillaGuiLayers.FOOD_LEVEL),
    ARMOR("armor", VanillaGuiLayers.ARMOR_LEVEL),
    AIR("air", VanillaGuiLayers.AIR_LEVEL),
    INFO_BAR_BACKGROUND("infoBarBackground", VanillaGuiLayers.CONTEXTUAL_INFO_BAR_BACKGROUND),
    INFO_BAR("infoBar", VanillaGuiLayers.CONTEXTUAL_INFO_BAR),
    XP_LEVEL("xpLevel", VanillaGuiLayers.EXPERIENCE_LEVEL),
    VEHICLE_HEALTH("vehicleHealth", VanillaGuiLayers.VEHICLE_HEALTH);

    public final String key;
    public final Identifier layerId;

    HudElement(String key, Identifier layerId) {
        this.key = key;
        this.layerId = layerId;
    }
}