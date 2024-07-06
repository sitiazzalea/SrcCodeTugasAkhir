package org.zaza.demo;

import java.util.Scanner;
import java.io.File;
import java.util.Scanner;
import org.zaza.fileHandling.FileHandler;

public class ReconstructDataDemo {
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.println("Jumlah file share yang akan dimasukkan: ");
        int filesNumber = input.nextInt();
        File[] secretFiles = new File[filesNumber];
        for (int i = 0; i < secretFiles.length; i++) {
            System.out.println("Masukkan file share ke- " + (i+1));
            String path = input.next();
            System.out.println("\n");            
            secretFiles[i] = new File(path);
        }

        System.out.println("Masukkan nama file hasil rekonstruksi: ");
        String pathReconstructedSecret = input.next();
        File reconstructedSecret = new File(pathReconstructedSecret);

        FileHandler.writeFileFromShares(secretFiles, reconstructedSecret);
    }
}
