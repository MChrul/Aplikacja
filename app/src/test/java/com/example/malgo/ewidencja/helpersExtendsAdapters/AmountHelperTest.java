package com.example.malgo.ewidencja.helpersExtendsAdapters;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class AmountHelperTest {

    @Test
    public void amountInWords_CorrectAmounts_ResultsAreCorrect() {
        AmountHelper amountHelper = new AmountHelper();

        assertThat(amountHelper.amountInWords(123.00))
                .isEqualTo(" sto dwadzieścia trzy złote [0/100] groszy");
        assertThat(amountHelper.amountInWords(1125.00))
                .isEqualTo(" tysiąc sto dwadzieścia pięć złotych [0/100] groszy");
        assertThat(amountHelper.amountInWords(58.65))
                .isEqualTo(" pięćdziesiąt osiem złotych [65/100] groszy");
        assertThat(amountHelper.amountInWords(32.99))
                .isEqualTo(" trzydzieści dwa złote [99/100] groszy");
    }

    @Test
    public void amountInWords_AmountToBig_DoesntReturnException() {
        AmountHelper amountHelper = new AmountHelper();

        assertThat(amountHelper.amountInWords(1231235.00)).isEqualTo("");
    }

    @Test
    public void amountInWords_AmountLimit_DoesntReturnException() {
        AmountHelper amountHelper = new AmountHelper();

        assertThat(amountHelper.amountInWords(1000000.00)).isEqualTo("");
    }

    @Test
    public void amountInWords_AmountCloseToLimit_ReturnCorrectValue() {
        AmountHelper amountHelper = new AmountHelper();

        assertThat(amountHelper.amountInWords(999999.99)).isEqualTo(
                " dziewięćset dziewięćdziesiąt dziewięć tysięcy " +
                        "dziewięćset dziewięćdziesiąt dziewięć złotych [99/100] groszy");
    }
}