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
        sb.append(numShare);
        sb.append(".");
        sb.append(getExtensionFile(file));
        // String path = sb.toString();
            // System.out.println(path);          
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static void putSharesInFiles(File file, int P, int t, int n) {
        byte[] fromFile = {};
        try {
            FileInputStream fis = new FileInputStream(file);
            fromFile = fis.readAllBytes();
            fis.close();
            System.out.println("FileInputStream Operation Success");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error message in FileInputStream: " + e.getMessage());
        }

        List<List<TLV>> parts = PRESSHandler.dealSecret(fromFile, P, t, n);

        try {
            for (int i = 0; i < parts.size(); i++) {
                List<TLV> part = parts.get(i);
                String path = dealSecretResultNumerator(file, i+1);
                FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(part);
                out.flush();
                out.close();

                System.out.println("OPERATION SUCCESS in FileOutputStream");
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error message in file output stream: " + e.getMessage());
        }

    }

    private static List<List<TLV>> collectedParts(File[] givenFiles) {
        List<List<TLV>> result = new ArrayList<List<TLV>>();

        try {
            for (int i = 0; i < givenFiles.length; i++) {
                FileInputStream fis = new FileInputStream(givenFiles[i]);
                ObjectInputStream in = new ObjectInputStream(fis);
                List<TLV> deserializedTLV = (List<TLV>)in.readObject();
                result.add(deserializedTLV);
                in.close();
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error in collecting part: " + e.getMessage());
        }

        return result;
    }

    public static void main(String[] args) {
        final int chunkNumber = 2;
        final int t = 3; //unsur SSS: threshold
        final int n = 7; // unsur SSS: jumlah share
        File secretFile = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\fileHandling\\zaza.txt");
        byte[] fromFile = {};
        try {
            FileInputStream secretFis = new FileInputStream(secretFile);
            fromFile = secretFis.readAllBytes();
            secretFis.close();
            System.out.println("Read file and transform it to byte array operation SUCCESS");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Problem in Read file and transform it to byte array operation: " + e.getMessage());
        }
 
        // putSharesInFiles(secretFile, chunkNumber, t, n);
    

        File input1 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\fileHandling\\zaza2.txt");
        File input2 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\fileHandling\\zaza7.txt");
        File input3 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\fileHandling\\zaza4.txt");
        File input4 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\fileHandling\\zaza1.txt");
        File[] inputs = {input1, input2, input3, input4};
        List<List<TLV>> parts2 = collectedParts(inputs);

        byte[] resultReconstructData = PRESSHandler.reconstructData(parts2);
        // HelperTools.printUnsignedByteArray(resultReconstructData);

        if (Arrays.equals(fromFile, resultReconstructData)) {
            System.out.println("Hasil file rahasia SAMA dengan hasil file rekonstruksi");
        }
        else {
            System.out.println("Hasil file rahasia TIDAK SAMA dengan hasil file rekonstruksi");
        }
    }
}
