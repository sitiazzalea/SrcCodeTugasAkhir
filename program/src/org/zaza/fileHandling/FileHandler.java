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

    public static void main(String[] args) {
        final int chunkNumber = 2;
        final int t = 3; //unsur SSS: threshold
        final int n = 7; // unsur SSS: jumlah share
        byte[] fromFile;
        try {
            File secretFile = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\fileHandling\\zaza.txt");
            // System.out.println("File name: " + secretFile.getName());
            // System.out.println("File path: " + secretFile.getPath());
            // System.out.println("File absolute path: " + secretFile.getAbsolutePath());
            // System.out.println("parent file path: " + secretFile.getParent());
            // System.out.println("File Extension: " + getExtensionFile(secretFile));
            // System.out.println("File name without extension: " + getNameWithoutExtension(secretFile));
            FileInputStream fis = new FileInputStream(secretFile);
            fromFile = fis.readAllBytes();
            fis.close();
            // HelperTools.printUnsignedByteArray(fromFile);

            List<List<TLV>> parts = PRESSHandler.dealSecret(fromFile, chunkNumber, t, n);
            // List<List<TLV>> parts2 = new ArrayList<List<TLV>>();
            // parts2.add(parts.get(1));
            // parts2.add(parts.get(4));
            // parts2.add(parts.get(0));
            // parts2.add(parts.get(6));
            // parts2.add(parts.get(3));

            // System.out.println("New path for deal secret result: " + getNewPathForDealSecretResult(secretFile));

            // String newPathForDealSecretResult = getNewPathForDealSecretResult(secretFile);

            for (int i = 0; i < parts.size(); i++) {
                List<TLV> part = parts.get(i);
                String path = dealSecretResultNumerator(secretFile, i+1);
                FileOutputStream fos = new FileOutputStream(path);
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(part);
                out.flush();
                out.close();
            }
                
            // byte[] recoveredSecretInBytes = PRESSHandler.reconstructData(parts2);
            // System.out.println("Recovered secret in bytes: ");
            // HelperTools.printUnsignedByteArray(recoveredSecretInBytes);

            // if (Arrays.equals(fromFile, recoveredSecretInBytes)) {               
            //     System.out.println("Hasil recovered secret SAMA dengan secret");            
            // }
            // else {
            //     System.out.println("Hasil recovered secret TIDAK SAMA dengan secret");            
            // }

            System.out.println("OPERATION SUCCESS");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error message: " + e.getMessage());
        }

    }
}
