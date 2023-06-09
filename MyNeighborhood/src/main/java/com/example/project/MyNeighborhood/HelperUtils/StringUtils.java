package com.example.project.MyNeighborhood.HelperUtils;

import java.util.UUID;

public class StringUtils {
    public static UUID convertStringToUUID(final String value){
        return UUID.fromString(value);
    }
}
