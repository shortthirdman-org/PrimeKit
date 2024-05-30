package com.shortthirdman.sharedlibs.common;

public enum FileTypes {

    DOCX("docx"),
    DOC("doc"),
    PDF("pdf"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    CSV("csv"),
    JSON("json"),
    TXT("txt"),
    RTF("rtf");

    private final String text;

    FileTypes(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static FileTypes fromString(String parameterName) {
        if (parameterName != null) {
            for (FileTypes objType: FileTypes.values()) {
                if (parameterName.equals(objType.text)) {
                    return objType;
                }
            }
        }
        return null;
    }
}
