package com.blunix.areaprotect.files;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.models.ProtectedArea;

public class AreasDataManager {
    private final AreaProtect plugin;

    public AreasDataManager(AreaProtect plugin) {
        this.plugin = plugin;
    }

    public void saveAllAreas() {
        for (ProtectedArea area : plugin.getProtectedAreas()) {
            saveArea(area);
        }
    }

    public void saveArea(ProtectedArea area) {

    }

    public void deleteArea(ProtectedArea area) {

    }
}
