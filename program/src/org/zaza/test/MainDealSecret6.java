package org.zaza.test;
import java.io.*;
import java.util.*;
import org.zaza.PRESS.PRESSHandler;
import org.zaza.fileHandling.FileHandler;
import org.zaza.utils.HelperTools;
import org.zaza.utils.TLV;
public class MainDealSecret6 {
    public static void main(String[] args) throws Exception {
        File secretFile = new File("C:\\Users\\Azzalea\\Documents\\JavaProject\\TugasAkhir\\program\\src\\org\\zaza\\test\\text\\lorem-5668-P5t6.txt");
        int P = 5;
        int t = 6;
        int n = 7;
        FileHandler.putSharesInFiles(secretFile, P, t, n);
    }
}
