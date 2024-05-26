package org.zaza.utils;


import java.util.Arrays;
import java.io.Serializable;
/**
 *
 * @author Zaza
 */
public class TLV implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public static final byte TYPE_THRESHOLD = 1;
    public static final byte TYPE_CHUNK_NUMBER = 2;
    public static final byte TYPE_PRIME = 3;
    public static final byte TYPE_SECRET_LENGTH = 4;
    public static final byte TYPE_X_COORDINATE = 5;
    public static final byte TYPE_Y_SHARE = 6;

    
    private byte type;
    private int length;
    private byte[] value = null;
    
    public TLV(byte type, int length, byte[] value) {
        this.type = type;
        this.length = length;
        this.value = Arrays.copyOf(value, length);
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public byte[] getValueAsBinary() {
        return this.value;
    }
    
}
