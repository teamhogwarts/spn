import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CTRDTest {

    @Test
    void zerlegeYTest() {
    //given
        int[] expect = {
                0b0000_0100_1101_0010,
                0b0000_1011_1011_1000,
                0b0000_0010_1000_1111,
                0b1000_1110_0111_1111,
                0b0110_0000_0101_0001,
                0b0100_0011_1010_0000,
                0b0001_0011_0110_0111,
                0b0010_1011_1011_0000};

        String chiffreText = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";

        int length = 16;
        CTRD CTRD = new CTRD(null);
        //when
        int[] result = CTRD.zerlegenVonY(chiffreText, length);

        //then
        assertArrayEquals(expect, result);
    }

    @Test
    void decryptTest(){
        //given

        /* ---- SPN ---- */
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
        final String chiffreText = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";

        //CTRD wird initalisiert mit Daten und dem SPN-System
        CTRD ctrd = new CTRD(spn);


        //when
        String actual = ctrd.deCrypt(chiffreText, 16);

        String exepted = "Gut gemacht!";

        //then
        assertEquals(actual, exepted);
    }

    @Test
    void enCryptTest(){
        //given

        /* ---- SPN ---- */
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
        String actual = ctrd.enCrypt(asciiText, 16);

        final String chiffreTextExpected = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";

        System.out.println(ctrd.deCrypt(actual, 16));

        //then
        assertEquals(actual, chiffreTextExpected);
    }

}