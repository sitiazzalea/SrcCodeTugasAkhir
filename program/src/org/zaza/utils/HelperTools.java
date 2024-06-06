package org.zaza.utils;

import java.nio.ByteBuffer;
import java.io.*;
import java.util.*;

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

    public static void compareTwoFiles(File input, File output) {
        byte[] inputFile = {};
        try {
            FileInputStream fis = new FileInputStream(input);
            inputFile = fis.readAllBytes();
            fis.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("HelperTools.compareTwoFiles: Error in reading input file: " + e.getMessage());
        }

        byte[] outputFile = {};
        try {
            FileInputStream fis = new FileInputStream(output);
            outputFile = fis.readAllBytes();
            fis.close();            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("HelperTools.compareTwoFiles: Error in reading output file: " + e.getMessage());
        }

        if (Arrays.equals(inputFile, outputFile)) {
            System.out.println("File output SAMA dengan file input");
        }
        else {
            System.out.println("File output TIDAK SAMA dengan file input");
        }
    }

}
