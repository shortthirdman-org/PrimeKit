package com.shortthirdman.primekit.essentials.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum CryptoModes {

    ECB("ECB", "Electronic Code Book Mode"),
    CBC("CBC", "Cipher Block Chain Mode"),
    CCM("CCM", "Counter/CBC Mode"),
    CFB("CFB","Cipher Feedback Mode"),
    OFB("OFB","Output Feedback Mode"),
    CTR("CTR","Counter Mode"),
    GCM("GCM","Galois/Counter Mode"),
    KW("KW","Key Wrap Mode"),
    KWP("KWP","Key Wrap Padding Mode"),
    PCBC("PCBC","Propagating Cipher Block Chaining");

    private final String modeName;
    private final String qualifiedName;

    CryptoModes(String mode, String name) {
        this.modeName = mode;
        this.qualifiedName = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(modeName).append("=").append(qualifiedName);
        return sb.toString();
    }

    public static CryptoModes fromMode(final String mode) {
        if (mode != null) {
            Optional<CryptoModes> obj = Arrays.stream(CryptoModes.values()).filter(cm -> cm.modeName.equals(mode)).findAny();
            return   obj.orElse(null);
        }

        return null;
    }
}
