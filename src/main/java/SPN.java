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

    public int rounds(int x, boolean reverse){
        int intX;
        int[] keys = generateSpnKeys(fullKey, r, n);

        for (int j = 0; j < keys.length; j++) {
            System.out.println(j+"te Key = " + Integer.toBinaryString(keys[j]));

        }

        //initialer Weisschritt
        intX = keys[0] ^ x;
        System.out.println("Weisschritt = " + Integer.toBinaryString(intX));
        int[] arrX = intTOIntArray(intX);
        //reguläre runde
        for (int i = 1; i < r; i++) {
            arrX = intTOIntArray(intX);
           arrX = sbox(arrX, reverse);
           intX = intArryToInt(arrX);
           System.out.println(i + ". SBOX = " +Integer.toBinaryString(intX));
           intX = bitpermutation(intX, n,m, bitpermutationValues);
           System.out.println(i + ". BP = " + Integer.toBinaryString(intX));
           intX = intX ^ keys[i];
            System.out.println(i + ". Key = " + Integer.toBinaryString(keys[i]));
           System.out.println(i + ". XOR = " + Integer.toBinaryString(intX));
        }
        System.out.println("Verkürzte Runde");
        //verkürzte runde
        arrX = intTOIntArray(intX);
        arrX = sbox(arrX, reverse);
        intX = intArryToInt(arrX);
        System.out.println(" Letze SBOX = " + Integer.toBinaryString(intX));
        intX = intArryToInt(arrX)  ^ keys[keys.length-1];


        return intX; //return Chiffretext
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


    public int bitpermutation(int bits, int n, int m, int[] bpValues) {
       String bitString = Integer.toBinaryString(bits);

        bitString = bitPaddingWithZeros(bitString, n,m);
        String tempBitString = "";

        for (int i = 0; i < bpValues.length; i++) {
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

    public static int[] intTOIntArray(int a){
        int[] intArray = new int[4];
        int blockStart = 4;
        for (int i = 0; i < intArray.length; i++) {
            int actValue = a << blockStart++ * 4;
            intArray[i] = actValue >>> 7 * 4;
        }
        return intArray;
    }

    public static int intArryToInt(int[] a){
        int result = a[0];
        for (int i = 1; i < a.length; i++) {
            result = (result << 4) ^ a[i];
        }
        return result;
    }

    private static int[] bitpermutationValues = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

    private static int[] sBOXValues = {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};

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
}