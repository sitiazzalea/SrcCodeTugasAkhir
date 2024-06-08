package org.zaza.fileHandling;
import java.math.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

import org.zaza.PRESS.PRESSHandler;
import org.zaza.utils.HelperTools;
import org.zaza.utils.TLV;

public class FileHandler {

    private static String getExtensionFile(File file) {
        String extension = null;
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        if(index > 0) {
            extension = fileName.substring(index + 1);
        }
        return extension;
    }

    private static String getNameWithoutExtension(File file) {
        String justName = null;
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            justName = fileName.substring(0, pos);
        }
        return justName;
    }

    private static String getNewPathForDealSecretResult(File file) {
        StringBuilder sb = new StringBuilder();
        // String secretParentPath = ;
        sb.append(file.getParent());
        sb.append("\\");
        // sb.append("hasilDealSecret_");
        // sb.append(getNameWithoutExtension(file));
        // sb.append("\\");
        return sb.toString();
    }

    private static String dealSecretResultNumerator(File file, int numShare) {
        String newPathForDealSecretResult = getNewPathForDealSecretResult(file);
        
        StringBuilder sb = new StringBuilder(newPathForDealSecretResult);
        sb.append(getNameWithoutExtension(file));
        sb.append("_");
        sb.append(numShare);
        sb.append(".PRESS");
        // sb.append(getExtensionFile(file));
        // String path = sb.toString();
            // System.out.println(path);          
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static void putSharesInFiles(File file, int P, int t, int n) {
        long start = System.nanoTime();

        byte[] fromFile = {};
        try {
            FileInputStream fis = new FileInputStream(file);
            fromFile = fis.readAllBytes();
            fis.close();
            // System.out.println("FileInputStream Operation Success");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error message in FileInputStream: " + e.getMessage());
        }

        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.printf( "putSharesInFiles: Elapsed time to load file: %,d nanosecond\n",  timeElapsed);
        
        start = System.nanoTime();
        List<List<TLV>> parts = PRESSHandler.dealSecret(fromFile, P, t, n);
        finish = System.nanoTime();
        timeElapsed = finish - start;
        System.out.printf( "putSharesInFiles: Elapsed time to call dealSecret: %,d nanosecond\n",  timeElapsed);

        start = System.nanoTime();
        try {
            
            for (int i = 0; i < parts.size(); i++) {
                List<TLV> part = parts.get(i);
                String path = dealSecretResultNumerator(file, i+1);
                FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(part);
                out.flush();
                out.close();

                // System.out.println("OPERATION SUCCESS in FileOutputStream");
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error message in file output stream: " + e.getMessage());
        }
        finish = System.nanoTime();
        timeElapsed = finish - start;
        System.out.printf( "putSharesInFiles: Elapsed time to write file: %,d nanosecond\n",  timeElapsed);


    }

    private static List<List<TLV>> collectParts(File[] givenFiles) throws Exception {
        List<List<TLV>> result = new ArrayList<List<TLV>>();

        try {
            int threshold = 0;
            int chunkNumber = 0;
            BigInteger primeNumber = BigInteger.ZERO;
            for (int i = 0; i < givenFiles.length; i++) {
                FileInputStream fis = new FileInputStream(givenFiles[i]);
                ObjectInputStream in = new ObjectInputStream(fis);
                List<TLV> deserializedTLV = (List<TLV>)in.readObject();

                for (int j = 0; j < deserializedTLV.size(); j++) {
                    if (deserializedTLV.get(j).getType() == deserializedTLV.get(j).TYPE_THRESHOLD) {
                        if (i == 0) { //first share
                            threshold = HelperTools.bytesToint(deserializedTLV.get(j).getValueAsBinary());
                            if (threshold > givenFiles.length) {
                                // throw ExceptionShareBelowThreshold                           
                            }                      
                        }
                        else {
                            if (threshold != HelperTools.bytesToint(deserializedTLV.get(j).getValueAsBinary())) {
                                throw new ExceptionDifferentThreshold("Different threshold number among files detected");
                            }
                        }
                    }

                    else if (deserializedTLV.get(j).getType() == deserializedTLV.get(j).TYPE_CHUNK_NUMBER) {
                        if (i == 0) { //first share
                            chunkNumber = HelperTools.bytesToint(deserializedTLV.get(j).getValueAsBinary());
                         
                        }
                        else {
                            if (chunkNumber != HelperTools.bytesToint(deserializedTLV.get(j).getValueAsBinary())) {
                                throw  new ExceptionDifferentChunkNumber("Different chunk numbers on the files detected");
                                
                            }
                        }
                    }

                    else if (deserializedTLV.get(j).getType() == deserializedTLV.get(j).TYPE_PRIME) {
                        BigInteger tmpPrime = new BigInteger(deserializedTLV.get(j).getValueAsBinary()); 
                        if (i == 0) { //first share
                            primeNumber = tmpPrime;
                         
                        }
                        else {
                            if (!primeNumber.equals(tmpPrime)) {
                                throw new ExceptionDifferentPrimeNumber("Different prime numbers on files detected");
                            }
                        }
                    }
                }

                result.add(deserializedTLV);
                in.close();
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error in collecting part: " + e.getMessage());
        }

        return result;
    }

    private static String namingReconstructionFile (File secretFile) {
        StringBuilder forResultFileName = new StringBuilder("HasilReconstruct_");
        forResultFileName.append(getNameWithoutExtension(secretFile));
        forResultFileName.append(".");
        forResultFileName.append(getExtensionFile(secretFile));
        return forResultFileName.toString();
    }

    public static void writeFileFromShares(File[] inputs, File resultFile) throws Exception {
        long startOfThisFunction = System.nanoTime();

        long start = System.nanoTime();
        List<List<TLV>> parts2 = collectParts(inputs);
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.printf( "writeFileFromShares: Elapsed time to call collectedParts(): %,d nanosecond\n",  timeElapsed);

        start = System.nanoTime();
        byte[] resultReconstructData = PRESSHandler.reconstructData(parts2);
        finish = System.nanoTime();
        timeElapsed = finish - start;
        System.out.printf( "writeFileFromShares: Elapsed time to call reconstructData(): %,d nanosecond\n",  timeElapsed);

        // String resultFileName = namingReconstructionFile(resultFile);

        try {
            start = System.nanoTime();
            FileOutputStream fos = new FileOutputStream(resultFile);
            fos.write(resultReconstructData);
            fos.flush();
            fos.close();
            finish = System.nanoTime();
            timeElapsed = finish - start;
            System.out.printf( "writeFileFromShares: Elapsed time to write data: %,d nanosecond\n",  timeElapsed);
                // System.out.println("Write to file operation SUCCEED");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error in writing reconstruction result to file: " + e.getMessage());
        }

        long endOfThisFunction = System.nanoTime();
        long elapsedTimeInFunction = endOfThisFunction - startOfThisFunction;
        System.out.printf( "writeFileFromShares: Elapsed time in function: %,d nanosecond\n",  elapsedTimeInFunction);
    }

}
