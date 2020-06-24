package main.java.com.company.utils;

import java.util.Random;

public final class ByteArray {
    private byte[] data;
    private int offset;
    private int length;
    private int pos;

    private ByteArray() {
    }

    public ByteArray(int size) {
        data = new byte[size];
        offset = 0;
        length = size;
        pos = 0;
    }

    public ByteArray(byte[] data, int offset, int length) {
        this.data = data;
        this.offset = offset;
        this.length = length;
        this.pos = 0;
    }

    public ByteArray(byte[] data) {
        this.data = data;
        offset = 0;
        length = data.length;
        pos = 0;
    }

    public byte[] getData() {
        return data;
    }

    public int getLength() {
        return length;
    }

    public int getOffset() {
        return offset;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        if ( pos >= 0 && pos < length )
            this.pos = pos;
    }

    public void resetPos() {
        pos = offset;
    }

    public static long readLong(byte[] data, int offset) {
        long result = 0;
        for (int i = offset; i < Long.BYTES + offset; i++) {
            result <<= Long.BYTES;
            result |= (data[i] & 0xFF);
        }
        return result;
    }

    public long readLong() {
        long result = readLong(data, pos);
        pos += 8;
        return result;
    }

    public static int readInt(byte[] data, int offset) {
        int ch1 = data[offset] & 0xff;
        int ch2 = data[offset + 1] & 0xff;
        int ch3 = data[offset + 2] & 0xff;
        int ch4 = data[offset + 3] & 0xff;
        return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
    }

    public int readInt() {
        int result = readInt(data, pos);
        pos += 4;
        return result;
    }

    public static short readShort(byte[] data, int offset) {
        int ch1 = data[offset] & 0xff;
        int ch2 = data[offset + 1] & 0xff;
        return (short)((ch1 << 8) + (ch2 << 0));
    }

    public short readShort() {
        short result = readShort(data, pos);
        pos += 2;
        return result;
    }

    public static char readChar(byte[] data, int offset) {
        int ch1 = data[offset] & 0xff;
        int ch2 = data[offset + 1] & 0xff;
        return (char)((ch1 << 8) + (ch2 << 0));
    }

    public char readChar() {
        char result = readChar(data, pos);
        pos += 2;
        return result;
    }

    public static byte readByte(byte[] data, int offset) {
        return data[offset];
    }

    public byte readByte() {
        return data[pos++];
    }

    public static void readBytes(byte[] data, int offset, byte[] buffer, int pos, int length) {
        System.arraycopy(data, offset, buffer, pos, length);
    }

    public ByteArray readBytes(byte[] buffer, int offset, int length) {
        System.arraycopy(data, pos, buffer, offset, length);
        pos += length;
        return this;
    }

    public static void readBytes(byte[] data, int offset, byte[] buffer) {
        readBytes(data, offset, buffer, 0, buffer.length);
    }

    public ByteArray readBytes(byte[] buffer) {
        return readBytes(buffer, 0, buffer.length);
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

    public static void writeShort(byte[] data, int offset, short value) {
        data[offset] = (byte)((value >>> 8) & 0xFF);
        data[offset + 1] = (byte)((value >>> 0) & 0xFF);
    }

    public ByteArray writeShort(short value) {
        writeShort(data, pos, value);
        pos += 2;
        return this;
    }

    public static void writeChar(byte[] data, int offset, char value) {
        data[offset] = (byte)((value >>> 8) & 0xFF);
        data[offset + 1] = (byte)((value >>> 0) & 0xFF);
    }

    public ByteArray writeChar(char value) {
        writeChar(data, pos, value);
        pos += 2;
        return this;
    }

    public static void writeByte(byte[] data, int offset, byte value) {
        data[offset] = value;
    }

    public ByteArray writeByte(byte value) {
        data[pos++] = value;
        return this;
    }

    public static void writeBytes(byte[] data, int offset, byte[] buffer, int pos, int length) {
        System.arraycopy(buffer, pos, data, offset, length);
    }

    public ByteArray writeBytes(byte[] buffer, int offset, int length) {
        System.arraycopy(buffer, 0, data, offset, length);
        pos += length;
        return this;
    }

    public static void writeBytes(byte[] data, int offset, byte[] buffer) {
        writeBytes(data, offset, buffer, 0, buffer.length);
    }

    public ByteArray writeBytes(byte[] buffer) {
        return writeBytes(buffer, 0, buffer.length);
    }

    public static byte[] generateRandomBytesArray(int size) {
        byte[] randomBytes = new byte[size];
        new Random().nextBytes(randomBytes);
        return randomBytes;
    }
}