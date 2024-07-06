package org.zaza.test;

import java.util.List;


import org.zaza.PRESS.PRESSHandler;
import org.zaza.utils.*;;

public class MainCoba {
    public static void main(String[] args) {
        String secret = "institut teknologi sepuluh nopember";
        byte[] secretInByte = secret.getBytes();
        List<List<TLV>> shares = PRESSHandler.dealSecret(secretInByte, 2, 3, 5);
    }
}
