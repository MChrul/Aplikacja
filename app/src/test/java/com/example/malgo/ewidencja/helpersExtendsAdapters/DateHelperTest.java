package com.example.malgo.ewidencja.helpersExtendsAdapters;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class DateHelperTest {

    @Test
    public void getMonthAsNumber_correctString_resultsAreCorrect() {
        DateHelper dateHelper = new DateHelper();

        assertThat(dateHelper.getMonthAsNumber("luty")).isEqualTo("02");
    }

    @Test
    public void getMonthAsNumber_incorrectString_doesntReturnException() {
        DateHelper dateHelper = new DateHelper();

        assertThat(dateHelper.getMonthAsNumber("niepoprawna wartosc")).isEqualTo("");
    }

    @Test
    public void getMonthFromNumber_correctInput_resultsAreCorrect() {
        DateHelper dateHelper = new DateHelper();

        assertThat(dateHelper.getMonthFromNumber("03")).isEqualTo("marzec");
    }

    @Test
    public void getMonthFromNumber_IncorrectInput_doesntReturnException() {
        DateHelper dateHelper = new DateHelper();

        assertThat(dateHelper.getMonthFromNumber("16")).isEqualTo("");
    }

    @Test
    public void getMonthAsNumber1_correctInput_resultsAreCorrect() {
        DateHelper dateHelper = new DateHelper();

        assertThat(dateHelper.getMonthAsNumber(5)).isEqualTo("05");
    }

    @Test
    public void getMonthAsNumber1_incorrectInput_doesntReturnException() {
        DateHelper dateHelper = new DateHelper();

        assertThat(dateHelper.getMonthAsNumber(14)).isEqualTo("");
    }
}