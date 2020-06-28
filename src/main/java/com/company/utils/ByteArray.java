package main.java.com.company.utils;

import java.util.Random;

/**
 * Helper class for parsing and retrieving different data types from byte arrays.
 *
 * @author Ivana Salmanić
 */

public final class ByteArray {
    private byte[] data;
    private int length;
    private int pos;

    public ByteArray(int size) {
        data = new byte[size];
        length = size;
        pos = 0;
    }

    public byte[] getData() {
        return data;
    }

    public int getLength() {
        return length;
    }

    public int getPos() {
        return pos;
    }

    public static long readLong(byte[] data, int offset) {
        long result = 0;
        for (int i = offset; i < Long.BYTES + offset; i++) {
            result <<= Long.BYTES;
            result |= (data[i] & 0xFF);
        }
        return result;
    }

    public static int readInt(byte[] data, int offset) {
        int ch1 = data[offset] & 0xff;
        int ch2 = data[offset + 1] & 0xff;
        int ch3 = data[offset + 2] & 0xff;
        int ch4 = data[offset + 3] & 0xff;
        return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
    }

    public static void writeLong(byte[] data, int offset, long value) {

        for (int i = 7; i >= 0; i--) {
            data[offset + i] = (byte)(value & 0xFF);
            value >>= 8;
        }
    }

    public ByteArray writeLong(long value) {
        writeLong(data, pos, value);
        pos += 8;
        return this;
    }

    public static void writeInt(byte[] data, int offset, int value) {
        data[offset] = (byte)((value >>> 24) & 0xFF);
        data[offset + 1] = (byte)((value >>> 16) & 0xFF);
        data[offset + 2] = (byte)((value >>> 8) & 0xFF);
        data[offset + 3] = (byte)((value >>> 0) & 0xFF);
    }

    public ByteArray writeInt(int value) {
        writeInt(data, pos, value);
        pos += 4;
        return this;
    }

    public ByteArray writeBytes(byte[] buffer, int offset, int length) {
        System.arraycopy(buffer, 0, data, offset, length);
        pos += length;
        return this;
    }

    public static byte[] generateRandomBytesArray(int size) {
        byte[] randomBytes = new byte[size];
        new Random().nextBytes(randomBytes);
        return randomBytes;
    }
}