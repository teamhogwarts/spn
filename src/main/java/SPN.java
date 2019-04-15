import java.util.HashMap;

public class SPN {
    private int r;   //Rundenschlüssel
    private int n;   //Anzahl Bit eines Blocks
    private int m;   //Anzahl Blöcke
    private int s;   //Länge des Schlüssel in Bit
    private int[] keys; //Schlüssel-Reihenfolge bei ENCRYPT
    private int[] keysReverse; //Schlüssel-Reihenfolge bei DECRYPT
    private HashMap<Integer, Integer> bitpermutationHashMap;
    private HashMap<Integer, Integer> sBoxHashMap;

    public SPN(int r, int n, int m, int s, int fullKey, int[] bpValues) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.bitpermutationHashMap = generateBitPermutationsHashMap(bpValues);
        this.keys = generateSpnKeys(fullKey);
        this.keysReverse = generateSpnKeysReverse();

        for (int j = 0; j < keys.length; j++) {
            System.out.println(j + "te Key = " + Integer.toBinaryString(keys[j]));
        }
    }


    public int startSPN(int x, boolean inverse) {

        int[] usedKeys = (inverse)? getKeysReverse() : getKeys();
        //initialer Weisschritt
        int intX = usedKeys[0] ^ x;
        System.out.println("Weisschritt XOR = " + Integer.toBinaryString(intX));
        int[] arrX = intTOIntArray(intX);
        //reguläre runde
        for (int i = 1; i < r; i++) {
            arrX = intTOIntArray(intX);
            arrX = sbox(arrX, inverse);
            intX = intArryToInt(arrX);
            System.out.println(i + ". SBOX = " + Integer.toBinaryString(intX));
            intX = bitPermutation(intX);
            System.out.println(i + ". BP = " + Integer.toBinaryString(intX));
            intX = intX ^ usedKeys[i];
            System.out.println(i + ". Key = " + Integer.toBinaryString(usedKeys[i]));
            System.out.println(i + ". XOR = " + Integer.toBinaryString(intX));
        }
        System.out.println("Verkürzte Runde");
        //verkürzte runde
        arrX = intTOIntArray(intX);
        arrX = sbox(arrX, inverse);
        intX = intArryToInt(arrX);
        System.out.println("Verk. Runde SBOX = " + Integer.toBinaryString(intX));
        intX = intArryToInt(arrX) ^ usedKeys[usedKeys.length - 1];
        System.out.println("Verk. Runde XOR = " + Integer.toBinaryString(intX));

        return intX;
    }

    public int[] generateSpnKeys(int fullKey) {
        int[] keys = new int[getAmountRound() + 1];
        for (int i = 0; i < 5; i++) {
            int keyValue = (int) fullKey << getN() * i;
            keys[i] = keyValue >>> getN() * getN();
        }
        return keys;
    }

    public int[] generateSpnKeysReverse() {

        int[] regKeys = getKeys();
        int[] keyReserve = new int[regKeys.length];
        int lastIndex = regKeys.length - 1;

        keyReserve[0] = regKeys[lastIndex];
        keyReserve[lastIndex] = regKeys[0];

        for (int i = 1; i < regKeys.length-1; i++) {
            keyReserve[i] = bitPermutation(regKeys[lastIndex-i]);
        }

        return keyReserve;
    }

    public int[] sbox(int[] a, boolean inverse) {
        for (int i = 0; i < a.length; i++) {
            a[i] = (inverse)? hashSBOXrevers.get(a[i]) : hashSBOX.get(a[i]);
        }
        return a;
    }

    public int bitPermutation(final int bits) {
        int higestBit = (getN() * getM());
        int resultBitMask = 0, checkBitValue;

        for (int i = 0; i < bitpermutationHashMap.size(); i++) {
            higestBit--;
            checkBitValue = bits & (1 << higestBit);

            checkBitValue = (i > bitpermutationHashMap.get(i)) ?
                    checkBitValue << Math.abs(bitpermutationHashMap.get(i) - i) :
                    checkBitValue >>> (bitpermutationHashMap.get(i) - i);

            resultBitMask = resultBitMask ^ checkBitValue;
        }
        return resultBitMask;
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

//    private static int[] bitpermutationHashMap = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

    private HashMap<Integer, Integer> generateBitPermutationsHashMap(int[] bitBPValues) {
        HashMap<Integer, Integer> hashMapBP = new HashMap<>();
        for (int i = 0; i < bitBPValues.length; i++) {
            hashMapBP.put(i, bitBPValues[i]);
        }
        return hashMapBP;
    }

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


    public HashMap<Integer, Integer> getBP() {
        return this.bitpermutationHashMap;
    }

    public int getN() {
        return this.n;
    }

    public int getM() {
        return this.m;
    }

    public int getAmountRound() {
        return this.r;
    }

    public int[] getKeys() {
        return this.keys;
    }

    public int[] getKeysReverse() {
        return this.keysReverse;
    }
}