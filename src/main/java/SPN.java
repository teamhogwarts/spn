import static java.lang.Integer.toBinaryString;

public class SPN {

    int r = 4;      //Rundenschlüssel
    int n = 4;      //Anzahl Bit eines Blocks
    int m = 4;      //Anzahl Blöcke
    int s = 32;     //Länge des Schlüssel

    static int[] sboxValues = new int[]{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};

    public static int sbox(int[] a){
        int[] b = new int[4];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < sboxValues.length; j++) {
                if (a[i] == sboxValues[j]){
                    b[i] = sboxValues[i];
                }
            }
        }

        return convertToInt(b);
    }

    public static int bitpermutation(int a){
        int[] arr = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
        int XOR;
        int b = 0;

        for (int i = 0; i < arr.length; i++) {

            XOR = a << arr.length + i;

            XOR = XOR >>> arr.length * 2 - 1;

            XOR = XOR << arr.length - 1  - arr[i] ;

            b = b ^ XOR;
        }
        return b;
    }

    public static int keyXOR(int a, int x){

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
        return s^x;
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

        int weisschritt = keyXOR(0, x);
        System.out.println("XOR beim Weisschritt: " + toBinaryString(weisschritt));

        weisschritt = sbox(convertToIntArray(weisschritt));
        System.out.println("SBOX beim Weisschritt: " + toBinaryString(weisschritt));

        int round1 = bitpermutation(weisschritt);
        System.out.println("BP Runde 1: " + toBinaryString(weisschritt));

        round1 = keyXOR(1, round1);
        System.out.println("XOR Runde 1: " + toBinaryString(weisschritt));

        round1 = sbox(convertToIntArray(round1));
        System.out.println("SBOX Runde 1: " + toBinaryString(weisschritt));

        int round2 = bitpermutation(round1);


        round2 = keyXOR(2, round2);


        round2 = sbox(convertToIntArray(round2));


        int round3 = bitpermutation(round2);


        round3 = keyXOR(3, round3);


        round3 = sbox(convertToIntArray(round3));


        int verkürzteRunde = keyXOR(4, round3);


        return toBinaryString(verkürzteRunde);
    }

    public static void main(String[] args) {
        System.out.println("is " + spn(  0b0001001010001111) + " equals " +  "1010111010110100: " + (spn(35087) == "1010111010110100"));

    }
}
