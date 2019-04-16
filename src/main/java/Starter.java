public class Starter {

    public static void main(String[] args) {

        final int r = 4;      //Rundenschlüssel
        final int n = 4;      //Anzahl Bit eines Blocks
        final int m = 4;      //Anzahl Blöcke
        final int s = 32;     //Länge des Schlüssel
        int[] bitpermutationValues = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};
        int[] sBOXValues = {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};

        final int fullkey = 0b0011_1010_1001_0100_1101_0110_0011_1111;

        //SPN wird initalisiert mit den nötigen Daten
        SPN spn = new SPN(r,n,m,s, fullkey, bitpermutationValues, sBOXValues);

        /* --- CTRD --- */

        //CTRD wird initalisiert mit Daten und dem SPN-System
        CTRD ctrd = new CTRD(spn);

        String asciiText = "Hallo Pascal";

        //when
        String y = ctrd.enCrypt(asciiText, 16);

        String x = ctrd.deCrypt(y,16);

        System.out.println(x);

        //Key generate show

//        int[] keys = spn.generateSpnKeys(testKey, r, n);
//        System.out.println("-------------------------------" + testKey);
//        for (int i = 0; i < keys.length; i++) {
//            System.out.println(Integer.toBinaryString(keys[i]));
//        }

    }
}
