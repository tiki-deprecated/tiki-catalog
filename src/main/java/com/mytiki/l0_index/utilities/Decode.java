/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

package com.mytiki.l0_index.utilities;

import org.springframework.security.crypto.codec.Utf8;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Decode {

    static public List<byte[]> chars(char[] bytes) {
        List<byte[]> extractedBytes = new ArrayList<>();
        int currentSize = 0;
        for (int i = 0; i < bytes.length; i += currentSize) {
            currentSize = decodeCompactSize(copyToBytes(bytes, i));

            int val = bytes[i] & 0xFF;
            if (val <= 252) i++;
            else if (val == 253) i += 3;
            else if (val == 254) i += 5;
            else i += 9;

            byte[] currentBytes = copyToBytes(bytes, i, i+currentSize);
            extractedBytes.add(currentBytes);
        }
        return extractedBytes;
    }

    static public List<byte[]> bytes(byte[] bytes) {
        List<byte[]> extractedBytes = new ArrayList<>();
        int currentSize = 0;
        for (int i = 0; i < bytes.length; i += currentSize) {
            currentSize = decodeCompactSize(Arrays.copyOfRange(bytes, i, bytes.length));

            int val = bytes[i] & 0xFF;
            if (val <= 252) i++;
            else if (val == 253) i += 3;
            else if (val == 254) i += 5;
            else i += 9;

            byte[] currentBytes = Arrays.copyOfRange(bytes, i, i+currentSize);
            extractedBytes.add(currentBytes);
        }
        return extractedBytes;
    }

    static public BigInteger bigInt(byte[] bytes) {
        boolean negative = bytes.length > 0 && ((bytes[0] & 0x80) == 0x80);
        long result;
        if (bytes.length == 1) {
            result = bytes[0];
        } else {
            result = 0;
            for (int i = 0; i < bytes.length; i++) {
                long item = bytes[bytes.length - i - 1] & 0xFF;
                result |= item << (8 * i);
            }
        }
        return result != 0 ? negative ?
                BigInteger.valueOf(result).negate() :
                BigInteger.valueOf(result) :
                BigInteger.ZERO;
    }

    static public String utf8(byte[] bytes) {
        if (bytes.length == 1 && bytes[0] == 0) return null;
        else return Utf8.decode(bytes);
    }

    static public ZonedDateTime dateTime(byte[] bytes) {
        if (bytes.length == 1 && bytes[0] == 0) return null;
        else return Instant
                .ofEpochSecond(Decode.bigInt(bytes).longValue())
                .atZone(ZoneOffset.UTC);
    }

    private static byte[] copyToBytes(char[] src, int from){
        return copyToBytes(src, from, src.length);
    }

    private static byte[] copyToBytes(char[] src, int from, int to){
        ByteBuffer out = ByteBuffer.allocate(to - from);
        for(int i=from; i<to; i++) out.put((byte) (src[i] & 0xff));
        return out.array();
    }

    private static int decodeCompactSize(byte[] compactSize) {
        int size =  compactSize[0] & 0xFF;

        byte[] bytes;
        if (size <= 252) return size;
        else if (size == 253) bytes = Arrays.copyOfRange(compactSize, 1, 3);
        else if (size == 254) bytes = Arrays.copyOfRange(compactSize, 1, 5);
        else bytes = Arrays.copyOfRange(compactSize, 1, 9);

        int value = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            value = value << 8;
            value = value | (bytes[i] & 0xFF);
        }
        return value;
    }
}
