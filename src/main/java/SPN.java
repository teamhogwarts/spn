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
    private HashMap<Integer, Integer> sBoxReverseHashMap;

    public SPN(int r, int n, int m, int s, int fullKey, int[] bpValues, int[] sBoxValues) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.bitpermutationHashMap = generateHashMap(bpValues, false);
        this.sBoxHashMap = generateHashMap(sBoxValues, false);
        this.sBoxReverseHashMap = generateHashMap(sBoxValues, true);
        this.keys = generateSpnKeys(fullKey);
        this.keysReverse = generateSpnKeysReverse();

        for (int j = 0; j < keys.length; j++) {
            System.out.println(j + "te Key = " + Integer.toBinaryString(keys[j]));
        }
    }


    public int startSPN(int x, boolean inverse) {

        int[] usedKeys = (inverse) ? getKeysReverse() : getKeys();
        //initialer Weisschritt
        int intX = usedKeys[0] ^ x;
        System.out.println("Weisschritt XOR = " + Integer.toBinaryString(intX));
        int[] arrX;
        //reguläre runde
        for (int i = 1; i < r; i++) {
            arrX = intToIntArray(intX);
            arrX = sBox(arrX, inverse);
            intX = intArrayToInt(arrX);
            System.out.println(i + ". SBOX = " + Integer.toBinaryString(intX));
            intX = bitPermutation(intX);
            System.out.println(i + ". BP = " + Integer.toBinaryString(intX));
            intX = intX ^ usedKeys[i];
            System.out.println(i + ". Key = " + Integer.toBinaryString(usedKeys[i]));
            System.out.println(i + ". XOR = " + Integer.toBinaryString(intX));
        }
        System.out.println("Verkürzte Runde");
        //verkürzte runde
        arrX = intToIntArray(intX);
        arrX = sBox(arrX, inverse);
        intX = intArrayToInt(arrX);
        System.out.println("Verk. Runde SBOX = " + Integer.toBinaryString(intX));
        intX = intArrayToInt(arrX) ^ usedKeys[usedKeys.length - 1];
        System.out.println("Verk. Runde XOR = " + Integer.toBinaryString(intX));

        return intX;
    }

    public int[] generateSpnKeys(int fullKey) {
        int[] keys = new int[getAmountRound() + 1];
        for (int i = 0; i < 5; i++) {
            int keyValue = fullKey << getN() * i;
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

        for (int i = 1; i < regKeys.length - 1; i++) {
            keyReserve[i] = bitPermutation(regKeys[lastIndex - i]);
        }

        return keyReserve;
    }

    public int[] sBox(int[] a, boolean inverse) {
        for (int i = 0; i < a.length; i++) {
            a[i] = (inverse) ? sBoxReverseHashMap.get(a[i]) : sBoxHashMap.get(a[i]);
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

    public int[] intToIntArray(int a) {
        int[] intArray = new int[m];
        int blockStart = n;
        for (int i = 0; i < intArray.length; i++) {
            int actValue = a << blockStart++ * n;
            intArray[i] = actValue >>> 7 * n;
        }
        return intArray;
    }

    public int intArrayToInt(int[] a) {
        int result = a[0];
        for (int i = 1; i < a.length; i++) {
            result = (result << n) ^ a[i];
        }
        return result;
    }

    private HashMap<Integer, Integer> generateHashMap(int[] arrToHashMap, boolean inverse) {
        HashMap<Integer, Integer> newHashMap = new HashMap<>();
        for (int i = 0; i < arrToHashMap.length; i++) {
            if (inverse) {
                newHashMap.put(arrToHashMap[i], i);
            } else {
                newHashMap.put(i, arrToHashMap[i]);
            }
        }
        return newHashMap;
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