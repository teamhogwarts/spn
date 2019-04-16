import java.util.Arrays;

public class CTRD {

    private int yMinus1;
    private int[] Yi;
    private int[] Xi;
    private int length;
    private SPN system;

    public CTRD(SPN system) {
        this.system = system;
    }

    public String deCrypt(String y, int length) {
        this.length = length;

        int[] tempy = zerlegenVonY(y, length);

        System.out.println("Y -------------------");
        for (int i = 0; i < tempy.length; i++) {
            System.out.println(Integer.toBinaryString(tempy[i]));
        }

        this.yMinus1 = tempy[0];
        this.Yi = Arrays.copyOfRange(tempy, 1, tempy.length);

        int[] resultX = new int[Yi.length];

        for (int i = 0; i < Yi.length; i++) {
            int xi = (this.yMinus1 + i) % (1 << this.length); //(int) Math.pow(2, this.length);
//            System.out.println(xi + " = y-1 + "+i+ " mod " + (int) Math.pow(2, this.length));

//            System.out.println("Start SPN mit: x"+ i + " = " + xi);
            xi = system.startSPN(xi, false);
//            System.out.println("Ende SPN mit: x"+ i + " = " + xi + " / " + Integer.toBinaryString(xi));
            xi = xi ^ Yi[i];
//            System.out.println((xi ^ yi[i]) + " = x" + i + " XOR y" + i+ " (" + yi[i] + " / " + Integer.toBinaryString(yi[i]) +")");
            resultX[i] = xi;
//            System.out.println("x" + i + " = " + resultX[i]);
//            System.out.println(" -----------------------");
        }


        // entfernen das letzte mit dem Pattern 1er gefüllt mit 0er
        resultX[resultX.length - 1] = Integer.numberOfTrailingZeros(resultX[resultX.length - 1] + 1);

        for (int x : resultX) {
            System.out.println(Integer.toBinaryString(x));
        }

        char[] chars = new char[resultX.length * 2];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (resultX[i / 2] >>> 8);
            i++;
            chars[i] = (char) (resultX[i / 2] & 0b00000_0000_1111_1111);
        }

        return new String(chars).trim();
    }



    public String enCrypt(String asciiText, int length) {

        // define the range of y-1
        int max = 1 << length;
        int min = 1;
        int range = max - min + 1;
        this.yMinus1 =  (int) (Math.random() * range) + min;
        System.out.println("y-1 : " + Integer.toBinaryString(yMinus1));

        // One String -> 14 Chars -> 7 Ints
        char[] charArray = asciiText.toCharArray();

        System.out.println(Arrays.toString(charArray));
        int[] intArrayOfChars = new int[(charArray.length / 2) + 1];
        for (int i = 0; i < charArray.length; i++) {
            intArrayOfChars[i / 2] = (int) charArray[i] << 8;
            i++;
            intArrayOfChars[i / 2] += (int) charArray[i] | intArrayOfChars[i / 2];
        }

        System.out.println("intArray: " + Arrays.toString(intArrayOfChars));

        for (int intArrayOfChar : intArrayOfChars) {
            System.out.println(Integer.toBinaryString(intArrayOfChar));
        }

        // padding 1 gefüllt mit 0 bis durch 16 teilbar
        int paddin = 0;
        int secLastIntArray = intArrayOfChars[intArrayOfChars.length - 2];
        int spacesOfZeros = Integer.numberOfTrailingZeros(secLastIntArray);

        System.out.println("spaceOfZeros: " + spacesOfZeros + "  secLasInt: " + Integer.toBinaryString(secLastIntArray));
        if (secLastIntArray > (1 << 8) && (spacesOfZeros > 0)) {
            System.out.println("isGrösser und hat Platz für eine 1 ---> " + (1 << spacesOfZeros - 1));
            intArrayOfChars[intArrayOfChars.length-2] = secLastIntArray | (1 << spacesOfZeros - 1);
        } else {
            intArrayOfChars[intArrayOfChars.length-1] = 0b1000_0000_0000_0000;
        }

        for (int intArrayOfChar : intArrayOfChars) {
            System.out.println(Integer.toBinaryString(intArrayOfChar));
        }

        //
        int effectiveLengthOfArray = 0;
        for (int i = 0; i < intArrayOfChars.length; i++) {
            if (intArrayOfChars[i] > 0) effectiveLengthOfArray++;
        }

        int[] resultY = new int[effectiveLengthOfArray];


        // E (SPN, K) XOR y-1
        for (int i = 0; i < resultY.length; i++) {
            int yi = (this.yMinus1 + i) % (1 << this.length);
            yi = system.startSPN(yi, false);
            yi = yi ^ intArrayOfChars[i];
            resultY[i] = yi;
        }


        System.out.println("ResultY: ");
        String ouput = "";
        for (int value : resultY) {
            System.out.println(String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0')  );
            ouput += String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
        }


        return ouput;
    }


    //zerlege Y in Blöcke der Länge "laenge" zerlegen = y-1, y0, y1 ....yn-1
    public static int[] zerlegenVonY(String XY, int length) {
        int amountOfBlocks = XY.length() / length; // 128 / 16 = 8
        int[] result = new int[amountOfBlocks];

        for (int i = 0; i < amountOfBlocks; i++) {
            result[i] = Integer.parseInt(XY.substring(i * length, i * length + length), 2);
        }
        return result;
    }
}
