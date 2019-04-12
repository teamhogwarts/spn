import java.nio.IntBuffer;

import static java.lang.Integer.toBinaryString;

public class SPN {

    int r = 4;      //Rundenschlüssel
    int n = 4;      //Anzahl Bit eines Blocks
    int m = 4;      //Anzahl Blöcke
    int s = 32;     //Länge des Schlüssel

    static int[] sboxValues = new int[]{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};

    public static int[] sbox(int[] a){
        int[] b = new int[16];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < sboxValues.length; j++) {
                if (a[i] == sboxValues[j]){
                    b[i] = sboxValues[i];
                }
            }
        }
        return b;
    }

    public static int[] bitpermutation(int[] a){
        int[] b = new int[16];

        b[0] = a[0];
        b[1] = a[4];
        b[2] = a[8];
        b[3] = a[12];
        b[4] = a[1];
        b[5] = a[5];
        b[6] = a[9];
        b[7] = a[13];
        b[8] = a[2];
        b[9] = a[6];
        b[10] = a[10];
        b[11] = a[14];
        b[12] = a[3];
        b[13] = a[7];
        b[14] = a[11];
        b[15] = a[15];

        return b;
    }

    public static int key(int a){

        int s = 0;

        switch (a){
            case 0:
                s = 0b0001_0001_0010_1000;
                break;
            case 1:
                s = 0b0001_0010_1000_1000;
                break;
            case 2:
                s = 0b0010_1000_1000_1100;
                break;
            case 3:
                s = 0b1000_1000_1100_0000;
                break;
            case 4:
                s = 0b1000_1100_0000_0000;
        }
        return s;
    }

    public static int[] convertToIntArray(int a) {
        int[] intArray = new int[4];
        int blockStart = 4;
        for (int i = 0; i < intArray.length; i++) {
            int actValue = a << blockStart++ * 4;
            intArray[i] = actValue >>> 7 * 4;
        }
        return intArray;
    }

    public static int convertToInt(int[] array) {
        int integer;

        array[0] = array[0] << 3*4;
        array[1] = array[0] << 2*4;
        array[2] = array[0] << 1*4;

        integer = array[0] ^ array[1] ^ array[2] ^ array[3];

        return integer;
    }


    public static String spn(int x){
        int[] b1;

        int a1 = (x ^ key(0));
        //System.out.println(toBinaryString(a1));

        b1 = sbox(convertToIntArray(a1));
        //System.out.println(toBinaryString(convertToInt(b1)));

        int[] b2 = bitpermutation(b1);
        //System.out.println(toBinaryString(convertToInt(b2)));

        int a2 = convertToInt(b2) ^ key(1);
        //System.out.println(toBinaryString(a2));

        b2 = sbox(convertToIntArray(a2));
        //System.out.println(toBinaryString(convertToInt(b2)));

        int[] b3 = bitpermutation(b2);
        //System.out.println(toBinaryString(convertToInt(b3)));

        int a3 = convertToInt(b3) ^ key(2);
        //System.out.println(toBinaryString(a3));

        b3 = sbox(convertToIntArray(a3));
        //System.out.println(toBinaryString(convertToInt(b3)));

        int[] b4 = bitpermutation(b3);
        //System.out.println(toBinaryString(convertToInt(b4)));

        int a4 = convertToInt(b4) ^ key(3);
        //System.out.println(toBinaryString(a4));

        b4 = sbox(convertToIntArray(a4));
        //System.out.println(toBinaryString(convertToInt(b4)));

        int a5 = convertToInt(b4) ^ key(4);
        //System.out.println(toBinaryString(a5));

        return toBinaryString(a5);
    }

    public static void main(String[] args) {
        System.out.println("is " + spn( 0b0001_0010_1000_1111) + " equals " +  "1010 1110 1011 0100: " + (spn(35087) == "1010111010110100"));

    }
}
