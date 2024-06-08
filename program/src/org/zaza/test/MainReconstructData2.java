package org.zaza.test;
import java.io.*;

import org.zaza.fileHandling.FileHandler;
import org.zaza.utils.HelperTools;

public class MainReconstructData2 {
    public static void main(String[] args) throws Exception {
        File secret = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\usedTestCase\\smiley-3565.png");

        File input1 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\usedTestCase\\smiley-3565-P3t4_1.PRESS");
        File input2 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\usedTestCase\\smiley-3565-P3t4_5.PRESS");
        File input3 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\usedTestCase\\smiley-3565-P3t4_6.PRESS");
        File input4 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\usedTestCase\\smiley-3565-P3t4_2.PRESS");
        File input5 = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\usedTestCase\\smiley-3565-P3t4_4.PRESS");

        File[] inputs = {input1, input2, input3, input4, input5};

        File hasilReconstruct = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\usedTestCase\\Hasil Rekonstruksi-smiley-3565-P3t4.png");

        FileHandler.writeFileFromShares(inputs, hasilReconstruct);

        HelperTools.compareTwoFiles(secret, hasilReconstruct);
    }
}
