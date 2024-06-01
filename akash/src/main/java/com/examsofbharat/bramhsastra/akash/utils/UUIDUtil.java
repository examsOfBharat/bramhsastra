package com.examsofbharat.bramhsastra.akash.utils;

import org.hibernate.id.UUIDHexGenerator;

public class UUIDUtil {
    private static final UUIDHexGenerator uuidGenerator = new UUIDHexGenerator();

    public static String generateUUID() {
        return uuidGenerator.generate(null, null).toString().replace("-","");
    }
}
