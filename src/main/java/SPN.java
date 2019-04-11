import java.util.Arrays;
import java.util.HashMap;

public class SPN {
    int r;
    int n;
    int m;
    int s;
    int fullKey;

    public SPN(int r, int n, int m, int s, int fullKey) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.s = s;
        this.fullKey = fullKey;
    }
    public SPN(){};


    /**
     *
     * @param fullKey
     * @param rounds
     * @param SizeOfBlock
     * @return
     */
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
        System.out.println(Arrays.toString(a));
        return a;
    }

    public int bitpermutation(int bits, int n, int m, int[] bpValues) {
       String bitString = Integer.toBinaryString(bits);

        bitString = bitPaddingWithZeros(bitString, n,m);
        String tempBitString = "";

        for (int i = 0; i < bpValues.length; i++) {
            System.out.println(bpValues[i]);
            tempBitString += bitString.charAt(bpValues[i]);
        }
        System.out.println(tempBitString);
        return Integer.parseInt(tempBitString,2);
    }

    public String bitPaddingWithZeros(String bitString, int n, int m){
        while (bitString.length() < n*m){
            bitString = "0" + bitString;
        }
        return bitString;
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