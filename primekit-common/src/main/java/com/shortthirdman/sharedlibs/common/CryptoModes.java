package com.shortthirdman.sharedlibs.common;

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

    private final String mode;

    private final String name;

    CryptoModes(String mode, String name) {
        this.mode = mode;
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mode).append("=").append(name);
        return sb.toString();
    }
}
