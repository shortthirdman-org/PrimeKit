package com.shortthirdman.sharedlibs.util;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StringPadderImplTest {
    private final StringPadderImpl stringPadder = new StringPadderImpl();

    @Test
    public void padLeft() {
        assertThat(stringPadder.padLeft("thegreatapi.com", 20))
                .isEqualTo("     thegreatapi.com");
    }
    @Test
    public void padLeftWithZeros() {
        assertThat(stringPadder.padLeft("thegreatapi.com", 20, '0'))
                .isEqualTo("00000thegreatapi.com");
    }
    @Test
    public void padRight() {
        assertThat(stringPadder.padRight("thegreatapi.com", 20))
                .isEqualTo("thegreatapi.com     ");
    }
    @Test
    public void padRightWithZeros() {
        assertThat(stringPadder.padRight("thegreatapi.com", 20, '0'))
                .isEqualTo("thegreatapi.com00000");
    }
    @Test
    public void padLeftWithInvalidTotalLength() {
        assertThat(stringPadder.padLeft("thegreatapi.com", 3))
                .isEqualTo("thegreatapi.com");
    }
    @Test
    public void padLeftWithZerosInvalidTotalLength() {
        assertThat(stringPadder.padLeft("thegreatapi.com", 3, '0'))
                .isEqualTo("thegreatapi.com");
    }
    @Test
    public void padRightInvalidTotalLength() {
        assertThat(stringPadder.padRight("thegreatapi.com", 3))
                .isEqualTo("thegreatapi.com");
    }
    @Test
    public void padRightWithZerosInvalidTotalLength() {
        assertThat(stringPadder.padRight("thegreatapi.com", 3, '0'))
                .isEqualTo("thegreatapi.com");
    }
}
