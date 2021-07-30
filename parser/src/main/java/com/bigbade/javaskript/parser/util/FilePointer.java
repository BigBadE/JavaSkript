package com.bigbade.javaskript.parser.util;

import com.bigbade.javaskript.parser.api.SkriptFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class FilePointer {
    private final SkriptFile file;
    private final char[] charBuffer;
    private int lineStart = 0;
    private int lineNumber = 0;
    public int bufferLocation = 0;
}
