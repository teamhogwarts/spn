import java.util.Arrays;
import java.util.HashMap;

public class SPN {
   private int r;   //Rundenschlüssel
   private int n;   //Anzahl Bit eines Blocks
   private int m;   //Anzahl Blöcke
   private int s;   //Länge des Schlüssel in Bit
   private int[] keys; //Schlüssel-Reihenfolge bei ENCRYPT
    private int[] keysReverse; //Schlüssel-Reihenfolge bei DECRYPT
    private int[] bitpermutationValues;


    public SPN(int r, int n, int m, int s, int fullKey, int[] bitpermutationValues) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.bitpermutationValues = bitpermutationValues;
        this.keys = generateSpnKeys(fullKey,r, n);


        for (int j = 0; j < keys.length; j++) {
            System.out.println(j + "te Key = " + Integer.toBinaryString(keys[j]));
        }
    }

    public SPN() {
    }

    public int rounds(int x, boolean reverse) {

        //initialer Weisschritt
        int intX = keys[0] ^ x;
        System.out.println("Weisschritt XOR = " + Integer.toBinaryString(intX));
        int[] arrX = intTOIntArray(intX);
        //reguläre runde
        for (int i = 1; i < r; i++) {
            arrX = intTOIntArray(intX);
            arrX = sbox(arrX, reverse);
            intX = intArryToInt(arrX);
            System.out.println(i + ". SBOX = " + Integer.toBinaryString(intX));
            intX = bitpermutation(intX, n, m, getBP());
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
        System.out.println("Verk. Runde SBOX = " + Integer.toBinaryString(intX));
        intX = intArryToInt(arrX) ^ keys[keys.length - 1];
        System.out.println("Verk. Runde XOR = " + Integer.toBinaryString(intX));

        return intX;
    }

    public int[] generateSpnKeys(int fullKey, int rounds, int SizeOfBlock) {
        int[] keys = new int[rounds + 1];
        for (int i = 0; i < 5; i++) {
            int keyValue = (int) fullKey << SizeOfBlock * i;
            keys[i] = keyValue >>> SizeOfBlock * SizeOfBlock;
        }
        return keys;
    }

    public int[] generateSpnKeysReverse(int[] regKeys, int n , int m){

        System.out.println("n: " + n +" m:" + m + " bp: " + Arrays.toString(bitpermutationValues));

        int[] keyReserve = new int[regKeys.length];

        int lastIndex = regKeys.length-1;

        keyReserve[0] = regKeys[lastIndex];
        keyReserve[lastIndex] = regKeys[0];

        System.out.println("Array keyReserve: " + Arrays.toString(keyReserve));
        System.out.println("Array regKeys: " + Arrays.toString(regKeys));

        System.out.println("regKeys[1] before BP: " + Integer.toBinaryString(regKeys[1]));
        System.out.println("regKeys[1] after BP: " + Integer.toBinaryString(bitpermutation(regKeys[1], n,m, getBP())));

        System.out.println("regKeys[2] before BP: " + Integer.toBinaryString(regKeys[2]));
        System.out.println("regKeys[2] after BP: " + Integer.toBinaryString(bitpermutation(regKeys[2], n,m, getBP())));


        System.out.println("regKeys[3] before BP: " + Integer.toBinaryString(regKeys[3]));
        System.out.println("regKeys[3] after BP: " + Integer.toBinaryString(bitpermutation(regKeys[3], n,m, getBP())));

//       keyReserve[3] = bitpermutation(regKeys[1], n,m, bitpermutationValues);
//        keyReserve[2] = bitpermutation(regKeys[2], n,m, bitpermutationValues);
//        for (int i = 1; i < regKeys.length-1; i++) {
//            keyReserve[i] = bitpermutation(regKeys[lastIndex-i], n,m, bitpermutationValues);
//        }
        System.out.println("Array keyReserve: " + Arrays.toString(keyReserve));
        return keyReserve;
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

        System.out.println(bitString + " length: " + bitString.length());

        bitString = bitPaddingWithZeros(bitString, n, m);

        System.out.println(bitString + " length: " + bitString.length());

        String tempBitString = "";
        for (int i = 0; i < bpValues.length; i++) {
            tempBitString += bitString.charAt(bpValues[i]);
        }
        return Integer.parseInt(tempBitString, 2);
    }

    public String bitPaddingWithZeros(String bitString, int n, int m) {
        while (bitString.length() < n * m) {
            bitString = "0" + bitString;
        }
//        assert bitString.length() != n*m : bitString + " mit Länge " + bitString.length() +" hat nicht die Länge von " + n*m;
      if (bitString.length() != n*m) System.out.println("ERROR: " +bitString + " mit Länge " + bitString.length() +" hat nicht die Länge von " + n*m);
        return bitString;
    }

    public static int[] intTOIntArray(int a) {
        int[] intArray = new int[4];
        int blockStart = 4;
        for (int i = 0; i < intArray.length; i++) {
            int actValue = a << blockStart++ * 4;
            intArray[i] = actValue >>> 7 * 4;
        }
        return intArray;
    }

    public static int intArryToInt(int[] a) {
        int result = a[0];
        for (int i = 1; i < a.length; i++) {
            result = (result << 4) ^ a[i];
        }
        return result;
    }

    private int[] getBP(){
        return bitpermutationValues;
    }

//    private static int[] bitpermutationValues = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

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