package com.blunix.areaprotect.models;

import com.blunix.areaprotect.AreaProtect;

public class ConfigManager {
    private static final AreaProtect PLUGIN = AreaProtect.getInstance();

    public static double getRequiredPlayTime() {
        return PLUGIN.getConfig().getDouble("required-playtime");
    }
}
