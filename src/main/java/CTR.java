public class CTR {

    public CTR(){}

    public static int encrypt(String XY, int length, int[] keys){
        //zerlege Y in Blöcke der Länge "laenge" zerlegen = y-1, y0, y1 ....yn-1
        int[] y = zerlelgenVonXY(XY, length);

        return 1;
    }

    public static int[] zerlelgenVonXY(String  XY, int length) {
        int amountOfBlocks = XY.length() / length; // 128 / 16 = 8
        int[] result = new int[amountOfBlocks];

        for (int i = 0; i < amountOfBlocks; i++) {
             result[i] = Integer.parseInt(XY.substring(i*length,i*length+length), 2);
        }

        return result;
    }

}
