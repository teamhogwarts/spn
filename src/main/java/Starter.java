public class Starter {



    public static void main(String[] args) {
        int r = 4;      //Rundenschlüssel
        int n = 4;      //Anzahl Bit eines Blocks
        int m = 4;      //Anzahl Blöcke
        int s = 32;     //Länge des Schlüssel

        final int fullKey = 0b0011_1010_1001_0100_1101_0110_0011_1111;
        System.out.println(fullKey);

        SPN spn = new SPN(r,n,m,s, fullKey);

        int[] keys = spn.generateSpnKeys(fullKey, r, n);


        System.out.println("-------------------------------" + fullKey);
        for (int i = 0; i < keys.length; i++) {
            System.out.println(Integer.toBinaryString(keys[i]));
        }
    }
}
