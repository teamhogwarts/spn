import java.util.Arrays;
import java.util.HashMap;

public class SPN {
    int r;   //Rundenschlüssel
    int n;   //Anzahl Bit eines Blocks
    int m;   //Anzahl Blöcke
    int s;   //Länge des Schlüssel
    int fullKey;

    public SPN(int r, int n, int m, int s, int fullKey) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.fullKey = fullKey;
    }
    public SPN(){}

    public int rounds(int x, int rounds , boolean reverse){
        int tempX;
        int[] keys = generateSpnKeys(fullKey, r, n);
        //initialer Weisschritt
        tempX = keys[0] ^ x;

        //reguläre runde
        for (int i = 0; i < rounds; i++) {
           // sbox(tempX, reverse);
        }

        //verkürzte runde


        return 0; //return Chiffretext
    }

    public int[] generateSpnKeys(int fullKey, int rounds, int SizeOfBlock) {
        int[] keys = new int[rounds + 1];
        for (int i = 0; i < 5; i++) {
            int keyValue = (int) fullKey << SizeOfBlock * i;
            keys[i] = keyValue >>> SizeOfBlock * SizeOfBlock;
        }
        return keys;
    }

    public int[] sbox(int[] a, boolean reverse) {
        for (int i = 0; i < a.length; i++) {
            if (reverse) {
                a[i] = hashSBOXrevers.get(a[i]);
            } else {
                a[i] = hashSBOX.get(a[i]);
            }
        }
        return a;
    }
    private static final HashMap<Integer, Integer> hashSBOX = new HashMap<>() {{
        for (int i = 0; i < sBOXValues.length; i++) {
            put(i, sBOXValues[i]);
        }
    }};
    private static final HashMap<Integer, Integer> hashSBOXrevers = new HashMap<>() {{
        for (int i = 0; i < sBOXValues.length; i++) {
            put(sBOXValues[i], i);
        }
    }};

    public int bitpermutation(int bits, int n, int m, int[] bpValues) {
       String bitString = Integer.toBinaryString(bits);

        bitString = bitPaddingWithZeros(bitString, n,m);
        String tempBitString = "";

        for (int i = 0; i < bpValues.length; i++) {
            System.out.println(bpValues[i]);
            tempBitString += bitString.charAt(bpValues[i]);
        }
        return Integer.parseInt(tempBitString,2);
    }

    public String bitPaddingWithZeros(String bitString, int n, int m){
        while (bitString.length() < n*m){
            bitString = "0" + bitString;
        }
        return bitString;
    }

    private static int intArryToInt(int[] a){
        return 0;
    }
    public static int[] intTOIntArray(int a){
        int[] intArray = new int[4];
        int intBlockDown = 3;
        for (int i = 0; i < intArray.length; i++) {
            int keyValue = (int) a << intBlockDown * i;
            intArray[i] = keyValue >>> 4*intBlockDown;
        }
        return intArray;
    }

    private static int[] bitpermutationValues = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

    private static int[] sBOXValues = {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};


}