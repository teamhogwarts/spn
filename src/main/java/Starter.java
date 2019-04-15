public class Starter {

    public static void main(String[] args) {
        int r = 4;      //Rundenschlüssel
        int n = 4;      //Anzahl Bit eines Blocks
        int m = 4;      //Anzahl Blöcke
        int s = 32;     //Länge des Schlüssel
        int[] bitpermutationValues = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
int[] sBOXValues = {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};


        // final int fullKey = 0b0011_1010_1001_0100_1101_0110_0011_1111;

        final int testKey = 0b0001_0001_0010_1000_1000_1100_0000_0000;
        final int testX = 0b0001_0010_1000_1111;



        SPN spn = new SPN(r,n,m,s, testKey, bitpermutationValues, sBOXValues);

        int y = spn.startSPN(testX, false);

        System.out.println("y = " + Integer.toBinaryString(y));


        //Key generate show

//        int[] keys = spn.generateSpnKeys(testKey, r, n);
//        System.out.println("-------------------------------" + testKey);
//        for (int i = 0; i < keys.length; i++) {
//            System.out.println(Integer.toBinaryString(keys[i]));
//        }

    }
}
