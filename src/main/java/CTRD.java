import java.util.Arrays;

public class CTRD {

    private int yMinus1;
    private int[] yi;
    private int length;
    private SPN system;

    public CTRD(String y, int length, SPN system) {
        int[] tempy = zerlegenVonY(y, length);
        this.yMinus1 = tempy[0];
        this.yi = Arrays.copyOfRange(tempy, 1, tempy.length);

        this.length = length;
        this.system = system;

        System.out.println("y-1 =" + Integer.toBinaryString(this.yMinus1));
        for (int i = 0; i < this.yi.length; i++) {
            System.out.println("y" + i + " =" + Integer.toBinaryString(this.yi[i]));
        }
    }

    public CTRD() {
    }

    public String[] decrypt() {

        String[] resultX = new String[yi.length];

        for (int i = 0; i < yi.length; i++) {
            int xi = (this.yMinus1 + i) % (int) Math.pow(2, this.length);
            System.out.println(xi + " = y-1 + "+i+ " mod " + (int) Math.pow(2, this.length));

            System.out.println("Start SPN mit: x"+ i + " = " + xi);
            xi = system.startSPN(xi, false);
            System.out.println("Ende SPN mit: x"+ i + " = " + xi + " / " + Integer.toBinaryString(xi));
            xi = xi ^ yi[i];
            System.out.println((xi ^ yi[i]) + " = x" + i + " XOR y" + i+ " (" + yi[i] + " / " + Integer.toBinaryString(yi[i]) +")");
            resultX[i] = Integer.toBinaryString(xi);
            System.out.println("x" + i + " = " + resultX[i]);
            System.out.println(" -----------------------");
        }

        System.out.println(Arrays.toString(resultX));

        return resultX;
    }


    public char intToASCII(int bit16){
//        first-+

        return '1';
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
