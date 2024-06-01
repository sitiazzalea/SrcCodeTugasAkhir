package org.zaza.test;

import org.zaza.PRESS.PRESSHandler;
import org.zaza.utils.HelperTools;
import org.zaza.utils.TLV;

import java.util.*;
import java.math.*;
import java.lang.Byte;

public class TestHitungan {
    public static void main(String[] args) {
        // BigInteger[] chunks; 
        // byte[] binarySecret = {(byte)0x7a, (byte)0x61, (byte)0x7a, (byte)0x61}; //t1
        // byte[] binarySecret = {(byte) 0xff, (byte) 0x00, (byte) 0x19, (byte)0x1a}; //t2
        // byte[] binarySecret = {(byte) 0xff, (byte) 0x00, (byte) 0x19, (byte) 0x01, (byte) 0x32}; //t3
        // final int chunkNumber = 3;
        // final int t = 5; //unsur SSS: threshold
        // final int n = 7; // unsur SSS: jumlah share
        // chunks = PRESSHandler.split(binarySecret, chunkNumber);
        // BigInteger largestChunk = PRESSHandler.findLargestChunk(chunks);
        // BigInteger prime = PRESSHandler.nextPrime(largestChunk);
        // System.out.println("Chunks:");
        // for (BigInteger b : chunks) {
        //     System.out.print(b + " ");
        // }
        // System.out.println();
        // // System.out.println("Largest prime among chunks: " + prime.toString());
        // // BigInteger[] hasilDealPhase = PRESSHandler.dealPhase(chunks, prime, chunkNumber, t, n);
        // // System.out.println("Hasil DealPhase:");
        // // for (BigInteger h : hasilDealPhase) {
        // //     System.out.print(h + " ");
        // // }
        // // System.out.println();

        // // 14197 17077 14389 15386 1492 20301 28912 //t1
        // // BigInteger[] xs = {new BigInteger("3"), BigInteger.ONE, BigInteger.TWO, new BigInteger("4"), new BigInteger("5"), new BigInteger("6"), };
        // // BigInteger[] ys = {new BigInteger("14389"), new BigInteger("14197"), new BigInteger("17077"), new BigInteger("15386"), new BigInteger("1492"), new BigInteger("20301")};
        // // 2541 8766 481 6652 924 4093 7252 //t3
        // BigInteger[] xs = {BigInteger.ONE, BigInteger.TWO, new BigInteger("3"), new BigInteger("4"), new BigInteger("5"), new BigInteger("6"), new BigInteger("7")};
        // BigInteger[] ys = {new BigInteger("2541"), new BigInteger("8766"), new BigInteger("481"), new BigInteger("6652"), new BigInteger("924"), new BigInteger("4093"), new BigInteger("7252")};
        // BigInteger[] hasilReconstructionPhase = PRESSHandler.reconstructPhase(xs, ys, prime, chunkNumber, t, n);
        // System.out.println("Hasil Reconstruct Data:");
        // for (BigInteger h : hasilReconstructionPhase) {
        //     System.out.print(h + " ");
        // }
        // System.out.println();

        // BigInteger a = new BigInteger("-256");
        // byte[] aInBytes = a.toByteArray();
        // System.out.println("-256 in bytes:");
        // for (byte b : aInBytes) {
        //     System.out.print(b + " ");
        // }
        // System.out.println();

        // BigInteger b = new BigInteger("12553");
        // byte[] bInBytes = b.toByteArray();
        // System.out.println("12553 in bytes");
        // for (byte c : bInBytes) {
        //     System.out.print(c + " ");
        // }
        // System.out.println();

        // byte d = (byte)0xff;
        // int unsignedD = Byte.toUnsignedInt(d);
        // System.out.println("Unsigned 0xff: " + unsignedD);
        // byte[] unsignedDInBytes = HelperTools.intToBytes(unsignedD);
        // System.out.println("Unsigned 255 in bytes:");
        // for (byte c : unsignedDInBytes) {
        //     System.out.print(c + " ");
        // }

        // byte[] arr = {(byte)0x00, (byte)0xff, (byte)0xff};
        byte[] arr = {(byte)0xff, (byte)0xff};
        // byte[] arr = {(byte)0x00, (byte)0x7a, (byte)0x7b};
        // byte[] arr = {(byte)0x7a, (byte)0x7b};
        // byte[] unsignedArr = ;
        // int[] arr = {0x7a, 0x7b};
        BigInteger a = new BigInteger(HelperTools.prependWithZero(arr));
        System.out.println(a.toString());

        byte[] fromBigInt = a.toByteArray();
        HelperTools.printUnsignedByteArray(fromBigInt);
    }
}
