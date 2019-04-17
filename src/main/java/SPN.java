import java.util.HashMap;

/**
 * @author Benjamin Brodwolf, Nadia Kramer, Pascal Andermatt
 * krysi FS-2019
 * @version 1.0
 */
public class SPN {
    private int r;   //Rundenschlüssel
    private int n;   //Anzahl Bit eines Blocks
    private int m;   //Anzahl Blöcke
    private int s;   //Länge des Schlüssel in Bit
    private int[] roundKeys; //Schlüssel-Reihenfolge bei ENCRYPT
    private int[] roundKeysInverse; //Schlüssel-Reihenfolge bei DECRYPT
    private HashMap<Integer, Integer> bitPermutation;
    private HashMap<Integer, Integer> sBox;
    private HashMap<Integer, Integer> sBoxInverse;
    private int[] usedKeysForSpn;


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
        this.bitPermutation = generateSpnComponent(bpValues, false);
        this.sBox = generateSpnComponent(sBoxValues, false);
        this.sBoxInverse = generateSpnComponent(sBoxValues, true);
        this.roundKeys = generateRoundKeys(fullKey);
        this.roundKeysInverse = generateSpnKeysInverse();
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
        this.usedKeysForSpn = (inverse) ? roundKeysInverse : roundKeys;

        int intX = initialSettingsStep(x); // usedKeys[0] ^ x;
        intX = regularRounds(intX, inverse);
        intX = shortRound(intX, inverse);

        return intX;
    }

    private int initialSettingsStep(int x) {
        return (usedKeysForSpn[0] ^ x);
    }

    private int regularRounds(int x, boolean inverse) {
        for (int i = 1; i < r; i++) {
            x = sBox(x, inverse);
            x = bitPermutation(x);
            x = x ^ usedKeysForSpn[i];
        }
        return x;
    }

    private int shortRound(int x, boolean inverse) {
        x = sBox(x, inverse);
        return (x ^ usedKeysForSpn[usedKeysForSpn.length - 1]);
    }


    /**
     * Erstellt eine Array von Keys aus dem BitString gem. Anforderungen
     * @param key
     * @return Array von Keys
     */
    public int[] generateRoundKeys(int key) { //  0000 0000 0000 0000 1010_1001_0100_1101;
        int[] roundKeys = new int[r + 1];
        for (int i = 0; i < (r + 1); i++) {
            int keyValue = key << n * i;
            roundKeys[i] = keyValue >>> n * n;
        }
        return roundKeys;
    }

    /**
     * Erstellt eine Array von Inverse-Keys aus den Regulären-Keys
     *
     * @return Array von Inverse-Keys
     */
    public int[] generateSpnKeysInverse() {
        int[] keyInverse = new int[roundKeys.length];
        int lastIndex = roundKeys.length - 1;

        keyInverse[0] = roundKeys[lastIndex];
        keyInverse[lastIndex] = roundKeys[0];

        for (int i = 1; i < lastIndex; i++) {
            keyInverse[i] = bitPermutation(roundKeys[lastIndex - i]);
        }

        return keyInverse;
    }

    /**
     * Die SBOX
     * @param initVector
     * @param inverse
     * @return Transformierte-Werte gem. SBOX
     */
    public int sBox(int initVector, boolean inverse) {
        int[] arr = intToArray(initVector);

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (inverse) ? sBoxInverse.get(arr[i]) : sBox.get(arr[i]);
        }

        return arrayToInt(arr);
    }

    /**
     * Die BitPermutation
     * @param bits
     * @return Transformierter-Wert gem. SBOX
     */
    public int bitPermutation(final int bits) {
        int higestBit = (n * m);
        int resultBitMask = 0, checkBitValue;

        for (int i = 0; i < bitPermutation.size(); i++) {
            higestBit--;

            checkBitValue = bits & (1 << higestBit);

            checkBitValue = (i > bitPermutation.get(i)) ?
                    checkBitValue << i - bitPermutation.get(i) :
                    checkBitValue >>> (bitPermutation.get(i) - i);

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
            result = (result << n) | a[i];
        }
        return result;
    }

    /**
     * Generiet die BitPermutation, SBOX sowie SBOX-Inverse.
     * @param initialComponent
     * @param inverse
     * @return HashMap (BP, SBOX, SBOX-INVERSE)
     */
    private HashMap<Integer, Integer> generateSpnComponent(int[] initialComponent, boolean inverse) {
        HashMap<Integer, Integer> finalSpnComponent = new HashMap<>();
        for (int i = 0; i < initialComponent.length; i++) {
            if (inverse) {
                finalSpnComponent.put(initialComponent[i], i);
            } else {
                finalSpnComponent.put(i, initialComponent[i]);
            }
        }
        return finalSpnComponent;
    }


    /*
        Getters für Testzwecke
     */
    public int[] getRoundKeys() {
        return this.roundKeys;
    }

    public int[] getRoundKeysInverse() {
        return this.roundKeysInverse;
    }
}