package com.niixlabs.dynamichud.client;

public enum HudTrigger {
    ITEM_CHANGE("triggerOnItemChange", true),
    ITEM_USE("triggerOnItemUse", true),
    COMBAT("triggerOnCombat", true),
    DAMAGE("triggerOnDamage", true),
    NOT_FULL_HEALTH("triggerWhenNotFullHealth", false),
    SNEAK("triggerOnSneak", true),
    SCREEN("triggerOnScreenOpen", true);

    public final String key;
    public final boolean defaultValue;

    HudTrigger(String key, boolean defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }
}
