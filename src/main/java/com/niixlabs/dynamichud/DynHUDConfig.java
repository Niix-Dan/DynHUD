package com.niixlabs.dynamichud;

import com.niixlabs.dynamichud.client.HudElement;
import com.niixlabs.dynamichud.client.HudTrigger;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.Map;

public class DynHUDConfig {
    public static final ModConfigSpec SPEC;

    public static final Map<HudElement, ModConfigSpec.BooleanValue> ENABLED = new EnumMap<>(HudElement.class);
    public static final Map<HudElement, ModConfigSpec.DoubleValue> SLIDE_IN_SPEED = new EnumMap<>(HudElement.class);
    public static final Map<HudElement, ModConfigSpec.DoubleValue> SLIDE_OUT_SPEED = new EnumMap<>(HudElement.class);
    public static final Map<HudElement, ModConfigSpec.IntValue> HIDE_DELAY = new EnumMap<>(HudElement.class);
    public static final Map<HudElement, ModConfigSpec.IntValue> SLIDE_OFFSET = new EnumMap<>(HudElement.class);
    public static final Map<HudElement, Map<HudTrigger, ModConfigSpec.BooleanValue>> TRIGGERS = new EnumMap<>(HudElement.class);

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        for (HudElement element : HudElement.values()) {
            builder.push(element.key);

            ENABLED.put(element, builder.comment("Whether DynHUD manages this element at all").define("enabled", true));

            builder.push("animation");
            SLIDE_IN_SPEED.put(element, builder.defineInRange("slideInSpeed", 0.15, 0.01, 1.0));
            SLIDE_OUT_SPEED.put(element, builder.defineInRange("slideOutSpeed", 0.05, 0.01, 1.0));
            HIDE_DELAY.put(element, builder.defineInRange("hideDelayTicks", 80, 0, 1200));
            SLIDE_OFFSET.put(element, builder.defineInRange("slideOffset", 50, 0, 200));
            builder.pop();

            builder.push("triggers");
            Map<HudTrigger, ModConfigSpec.BooleanValue> triggerMap = new EnumMap<>(HudTrigger.class);
            for (HudTrigger trigger : HudTrigger.values()) {
                triggerMap.put(trigger, builder.define(trigger.key, trigger.defaultValue));
            }
            TRIGGERS.put(element, triggerMap);
            builder.pop();

            builder.pop();
        }

        SPEC = builder.build();
    }
}