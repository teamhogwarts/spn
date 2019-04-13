import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CTRTest {

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
        CTR ctr = new CTR();
        //when
        int[] result = ctr.zerlelgenVonXY(chiffreText, length);

        //then
        assertArrayEquals(expect, result);
    }

}