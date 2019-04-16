import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SPNTest {

    @Test
    void sboxTest(){
        //given
        SPN spn = new SPN();
        int[] actual = new int[]{0, 1, 2, 3};
        int[] expected = new int[]{14, 4, 13, 1};


        //when
        int spnSBOXED = spn.sbox(actual);
        int exp = spn.convertToInt(expected);

        //then
        assertEquals(spnSBOXED, exp);
    }

    @Test
    void bitpermutationTest(){
        //given
        SPN spn = new SPN();
        int actual = 0b1110_0001_0110_1000;
        System.out.println(actual);
        int expected = 0b1001_1010_1010_0100;

        //when
        int spnBP = spn.bitpermutation(actual);

        //then
        assertEquals(spnBP, expected);
    }

}