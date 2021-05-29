package com.blunix.areaprotect.util;

import com.google.common.base.Charsets;

import java.util.UUID;

public class UUIDUtil {

    public static UUID getOfflineUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
    }
}
