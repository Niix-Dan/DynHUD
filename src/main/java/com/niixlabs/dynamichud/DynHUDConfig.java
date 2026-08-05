package com.niixlabs.dynamichud;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DynHUDConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue SLIDE_IN_SPEED;
    public static final ModConfigSpec.DoubleValue SLIDE_OUT_SPEED;
    public static final ModConfigSpec.IntValue HIDE_DELAY;
    public static final ModConfigSpec.IntValue SLIDE_OFFSET;

    public static final ModConfigSpec.BooleanValue TRIGGER_ITEM_CHANGE;
    public static final ModConfigSpec.BooleanValue TRIGGER_ITEM_USE;
    public static final ModConfigSpec.BooleanValue TRIGGER_COMBAT;
    public static final ModConfigSpec.BooleanValue TRIGGER_DAMAGE;
    public static final ModConfigSpec.BooleanValue TRIGGER_NOT_FULL_HEALTH;
    public static final ModConfigSpec.BooleanValue TRIGGER_SNEAK;
    public static final ModConfigSpec.BooleanValue TRIGGER_SCREEN;

    public static final ModConfigSpec.BooleanValue AFFECT_HOTBAR;
    public static final ModConfigSpec.BooleanValue AFFECT_HEALTH;
    public static final ModConfigSpec.BooleanValue AFFECT_FOOD;
    public static final ModConfigSpec.BooleanValue AFFECT_ARMOR;
    public static final ModConfigSpec.BooleanValue AFFECT_AIR;
    public static final ModConfigSpec.BooleanValue AFFECT_INFO_BAR_BG;
    public static final ModConfigSpec.BooleanValue AFFECT_INFO_BAR;
    public static final ModConfigSpec.BooleanValue AFFECT_XP_LEVEL;
    public static final ModConfigSpec.BooleanValue AFFECT_VEHICLE_HEALTH;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("settings_animation");
        SLIDE_IN_SPEED = builder.defineInRange("slideInSpeed", 0.15, 0.01, 1.0);
        SLIDE_OUT_SPEED = builder.defineInRange("slideOutSpeed", 0.05, 0.01, 1.0);
        HIDE_DELAY = builder.defineInRange("hideDelayTicks", 80, 0, 1200);
        SLIDE_OFFSET = builder.defineInRange("slideOffset", 50, 0, 200);
        builder.pop();

        builder.push("settings_triggers");
        TRIGGER_ITEM_CHANGE = builder.define("triggerOnItemChange", true);
        TRIGGER_ITEM_USE = builder.define("triggerOnItemUse", true);
        TRIGGER_COMBAT = builder.define("triggerOnCombat", true);
        TRIGGER_DAMAGE = builder.define("triggerOnDamage", true);
        TRIGGER_NOT_FULL_HEALTH = builder.define("triggerWhenNotFullHealth", false);
        TRIGGER_SNEAK = builder.define("triggerOnSneak", true);
        TRIGGER_SCREEN = builder.define("triggerOnScreenOpen", true);
        builder.pop();

        builder.push("settings_elements");
        AFFECT_HOTBAR = builder.define("affectHotbar", true);
        AFFECT_HEALTH = builder.define("affectHealth", true);
        AFFECT_FOOD = builder.define("affectFood", true);
        AFFECT_ARMOR = builder.define("affectArmor", true);
        AFFECT_AIR = builder.define("affectAir", true);
        AFFECT_INFO_BAR_BG = builder.define("affectInfoBarBackground", true);
        AFFECT_INFO_BAR = builder.define("affectInfoBar", true);
        AFFECT_XP_LEVEL = builder.define("affectXpLevel", true);
        AFFECT_VEHICLE_HEALTH = builder.define("affectVehicleHealth", true);
        builder.pop();

        SPEC = builder.build();
    }
}