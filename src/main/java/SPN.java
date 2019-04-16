import java.util.HashMap;

/**
 * @author Benjamin Brodwolf
 * krysi FS-2019
 * @version 1.0
 */
public class SPN {
    private int r;   //Rundenschlüssel
    private int n;   //Anzahl Bit eines Blocks
    private int m;   //Anzahl Blöcke
    private int s;   //Länge des Schlüssel in Bit
    private int[] keys; //Schlüssel-Reihenfolge bei ENCRYPT
    private int[] keysInverse; //Schlüssel-Reihenfolge bei DECRYPT
    private HashMap<Integer, Integer> bitpermutationHashMap;
    private HashMap<Integer, Integer> sBoxHashMap;
    private HashMap<Integer, Integer> sBoxInverseHashMap;

    /**
     * Konstruktor - initialisiert und generiert alle nötigen Werten (Keys, SBOX, BP)
     *
     * @param r
     * @param n
     * @param m
     * @param s
     * @param fullKey
     * @param bpValues
     * @param sBoxValues
     */
    public SPN(int r, int n, int m, int s, int fullKey, int[] bpValues, int[] sBoxValues) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.bitpermutationHashMap = generateHashMap(bpValues, false);
        this.sBoxHashMap = generateHashMap(sBoxValues, false);
        this.sBoxInverseHashMap = generateHashMap(sBoxValues, true);
        this.keys = generateSpnKeys(fullKey);
        this.keysInverse = generateSpnKeysInverse();
    }

    /**
     * Ver- und Entschlüsselter gem. des SPN-Algorithmus.
     * Kann auch mit den Inversen-Keys (boolean inverse) gestartet werden.
     * @param x
     * @param inverse
     * @return Ver- oder Entschlüsselter-Wert (X oder bzw. Y)
     */
    public int startSPN(int x, boolean inverse) {
        // Falls eine Inverse-SPN abfolge sein sol, werden die Reverse-Keys benutzt - ansonsten die Regulären
        int[] usedKeys = (inverse) ? keysInverse : keys;

        // initialer Weisschritt
        int intX = usedKeys[0] ^ x;
        int[] arrX;

        // reguläre runde
        for (int i = 1; i < r; i++) {
            arrX = intToArray(intX);
            arrX = sBox(arrX, inverse);
            intX = arrayToInt(arrX);
            intX = bitPermutation(intX);
            intX = intX ^ usedKeys[i];
        }

        // verkürzte runde
        arrX = intToArray(intX);
        arrX = sBox(arrX, inverse);
        intX = arrayToInt(arrX) ^ usedKeys[usedKeys.length - 1];

        return intX;
    }

    /**
     * Erstellt eine Array von Keys aus dem BitString gem. Anforderungen
     * @param fullKey
     * @return Array von Keys
     */
    public int[] generateSpnKeys(int fullKey) {
        int[] keys = new int[r + 1];
        for (int i = 0; i < 5; i++) {
            int keyValue = fullKey << n * i;
            keys[i] = keyValue >>> n * n;
        }
        return keys;
    }

    /**
     * Erstellt eine Array von Inverse-Keys aus den Regulären-Keys
     *
     * @return Array von Inverse-Keys
     */
    public int[] generateSpnKeysInverse() {
        int[] keyRegular = keys;
        int[] keyInverse = new int[keyRegular.length];
        int lastIndex = keyRegular.length - 1;

        keyInverse[0] = keyRegular[lastIndex];
        keyInverse[lastIndex] = keyRegular[0];

        for (int i = 1; i < keyRegular.length - 1; i++) {
            keyInverse[i] = bitPermutation(keyRegular[lastIndex - i]);
        }

        return keyInverse;
    }

    /**
     * Die SBOX
     * @param a
     * @param inverse
     * @return Transformierte-Werte gem. SBOX
     */
    public int[] sBox(int[] a, boolean inverse) {
        for (int i = 0; i < a.length; i++) {
            a[i] = (inverse) ? sBoxInverseHashMap.get(a[i]) : sBoxHashMap.get(a[i]);
        }
        return a;
    }

    /**
     * Die BitPermutation
     * @param bits
     * @return Transformierter-Wert gem. SBOX
     */
    public int bitPermutation(final int bits) {
        int higestBit = (n * m);
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


    /**
     * Helfermethode, zerstückelt das n*m Bit-Muster in m Blöcke
     *
     * @param a
     * @return m grosse Int-Blöcke
     */
    public int[] intToArray(int a) {
        int[] intArray = new int[m];
        int blockStart = n;
        for (int i = 0; i < intArray.length; i++) {
            int actValue = a << blockStart++ * n;
            intArray[i] = actValue >>> 7 * n;
        }
        return intArray;
    }

    /**
     * Analog zu "intToArray" vereint diese Helfermethode die Int-Blöcke zu einem ganzen int
     *
     * @param a
     * @return n * m grosses Int
     */
    public int arrayToInt(int[] a) {
        int result = a[0];
        for (int i = 1; i < a.length; i++) {
            result = (result << n) ^ a[i];
        }
        return result;
    }

    /**
     * Generiet die HashMaps für die BitPermutation, SBOX sowie SBOX-Inverse.
     * @param arrToHashMap
     * @param inverse
     * @return HashMap (BP, SBOX, SBOX-INVERSE)
     */
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


    /*
        Getters für Testzwecke
     */
    public int[] getKeys() {
        return this.keys;
    }

    public int[] getKeysInverse() {
        return this.keysInverse;
    }
}