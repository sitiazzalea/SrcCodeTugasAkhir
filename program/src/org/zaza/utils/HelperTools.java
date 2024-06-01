package org.zaza.utils;

import java.nio.ByteBuffer;

public class HelperTools {
    public static byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }

    public static int bytesToint(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip 
        return buffer.getInt();
    }

    public static void printUnsignedByteArray(byte[] arr) {
        System.out.print("In decimal: ");
        for (byte b : arr) {
            System.out.printf("%4d ",Byte.toUnsignedInt(b));
        }
        System.out.println();
        System.out.print("In Hexa: ");
        for (byte b : arr) {
            System.out.printf("%02x ",Byte.toUnsignedInt(b));
        }
        System.out.println();
    }

    public static byte[] prependWithZero(byte[] arr) {
        byte[] unsignedByteArray = new byte[arr.length + 1];
        System.arraycopy(arr, 0, unsignedByteArray, 1, arr.length);
        return unsignedByteArray;
    }

}
