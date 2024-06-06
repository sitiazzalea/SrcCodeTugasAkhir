package org.zaza.test;
import java.io.*;

import org.zaza.fileHandling.FileHandler;
import org.zaza.utils.HelperTools;

public class MainReconstructData1 {
    public static void main(String[] args) throws Exception {
        File secret = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\zaza.txt");

        File input1 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\zaza_4.PRESS");
        File input2 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\zaza_7.PRESS");
        File input3 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\zaza_1.PRESS");
        File input4 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\zaza_3.PRESS");

        File[] inputs = {input1, input2, input3, input4};

        File hasilReconstruct = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\HasilRekonstruksi_zaza.txt");

        FileHandler.writeFileFromShares(inputs, hasilReconstruct);

        HelperTools.compareTwoFiles(secret, hasilReconstruct);
    }
}
