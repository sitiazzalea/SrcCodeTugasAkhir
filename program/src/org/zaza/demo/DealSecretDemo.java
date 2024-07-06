package org.zaza.demo;

import java.io.File;
import java.util.Scanner;
import org.zaza.fileHandling.FileHandler;

public class DealSecretDemo {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Masukkan file rahasia: ");
        String secretFilePath = input.nextLine();
        File secret = new File(secretFilePath);
        System.out.println("Masukkan pecahan rahasia (P) yang diinginkan: ");
        int P = input.nextInt();
        System.out.println("Masukkan ambang batas (t) yang diinginkan: ");
        int t = input.nextInt();
        System.out.println("Masukkan jumlah share (n) yang diinginkan: ");
        int n = input.nextInt();

        FileHandler.putSharesInFiles(secret, P, t, n);
    }
}
