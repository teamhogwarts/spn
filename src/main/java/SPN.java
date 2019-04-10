import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SPN {


    public static void main(String[] args) {
        int r = 4;      //Rundenschlüssel
        int n = 4;      //Anzahl Bit eines Blocks
        int m = 4;      //Anzahl Blöcke
        int s = 32;     //Länge des Schlüssel

        final int fullKey = 0b0011_1010_1001_0100_1101_0110_0011_1111;
        System.out.println(fullKey);

        int[] keys = generateSpnKeys(fullKey, r, n);

        System.out.println(hashSBOX.containsKey(14) + " " + hashSBOX.get(14));


        System.out.println("-------------------------------" + fullKey);
        for (int i = 0; i < keys.length; i++) {
            System.out.println(Integer.toBinaryString(keys[i]));
        }


    }

    public static int[] generateSpnKeys(int fullKey, int rounds, int SizeOfBlock) {
        int[] keys = new int[rounds + 1];
        for (int i = 0; i < 5; i++) {
            int keyValue = (int) fullKey << SizeOfBlock * i;
            keys[i] = keyValue >>> SizeOfBlock * SizeOfBlock;
        }
        return keys;
    }

    public int[] sbox(int[] a, boolean reverse) {
        for (int i = 0; i < a.length; i++) {
            if (reverse){
                a[i] = hashSBOXrevers.get(a[i]);
            } else {
                a[i] = hashSBOX.get(a[i]);
            }
        }
        System.out.println(Arrays.toString(a));
        return a;
    }

    public static byte getSBOXvalue(byte a, HashMap<Integer, Integer> sbox, boolean isInvers) {

        return a;
    }

    public int[] bitpermutation(int[] a) {

        return a;
    }


    private static final HashMap<Integer, Integer> hashSBOX = new HashMap<>() {{
        put(0, 14);
        put(1, 4);
        put(2, 13);
        put(3, 1);
        put(4, 2);
        put(5, 15);
        put(6, 11);
        put(7, 8);
        put(8, 3);
        put(9, 10);
        put(10, 6);
        put(11, 12);
        put(12, 5);
        put(13, 9);
        put(14, 0);
        put(15, 7);
    }};
    private static final HashMap<Integer, Integer> hashSBOXrevers = new HashMap<>() {{
        put(14, 0);
        put(4, 1);
        put(13, 2);
        put(1, 3);
        put(2, 4);
        put(15, 5);
        put(11, 6);
        put(8, 7);
        put(3, 8);
        put(10, 9);
        put(6, 10);
        put(12, 11);
        put(5, 12);
        put(9, 13);
        put(0, 14);
        put(7, 15);
    }};
}