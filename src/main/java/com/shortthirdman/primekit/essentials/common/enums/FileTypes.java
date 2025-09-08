package com.shortthirdman.primekit.essentials.common.enums;

import lombok.Getter;

@Getter
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

    private final String extension;

    FileTypes(String extension) {
        this.extension = extension;
    }

    public static FileTypes fromString(String parameterName) {
        if (parameterName != null) {
            for (FileTypes objType: FileTypes.values()) {
                if (parameterName.equals(objType.extension)) {
                    return objType;
                }
            }
        }
        return null;
    }
}
