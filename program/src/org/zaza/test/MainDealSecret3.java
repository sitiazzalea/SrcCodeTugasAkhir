package org.zaza.test;
import java.io.*;
import java.util.*;
import org.zaza.PRESS.PRESSHandler;
import org.zaza.fileHandling.FileHandler;
import org.zaza.utils.HelperTools;
import org.zaza.utils.TLV;
public class MainDealSecret3 {
    public static void main(String[] args) throws Exception {
        File secretFile = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-2345-P3t4.txt");
        int P = 3;
        int t = 4;
        int n = 7;
        FileHandler.putSharesInFiles(secretFile, P, t, n);
    }
}
