package org.zaza.test;
import java.io.*;

import org.zaza.fileHandling.FileHandler;
import org.zaza.utils.HelperTools;

public class MainReconstructData1 {
    public static void main(String[] args) throws Exception {
        File secret = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-931.txt");

        File input1 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-931_1.PRESS");
        File input2 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-931_5.PRESS");
        File input3 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-931_6.PRESS");
        File input4 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-931_7.PRESS");
        File input5 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-931_3.PRESS");
        File input6 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-931_2.PRESS");

        File[] inputs = {input1, input2, input3, input4, input5, input6};

        File hasilReconstruct = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\Reconstructed-lorem-931.txt");

        FileHandler.writeFileFromShares(inputs, hasilReconstruct);

        HelperTools.compareTwoFiles(secret, hasilReconstruct);
    }
}
