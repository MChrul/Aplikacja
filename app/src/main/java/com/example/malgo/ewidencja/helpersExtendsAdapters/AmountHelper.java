package com.example.malgo.ewidencja.helpersExtendsAdapters;

public class AmountHelper {
    public String amountInWords(Double amount) {

        if (amount >= 1000000) {
            return "";
        }

        int afterComa = (int)((amount * 100) % 100);
        String pennyStr = " [" + String.valueOf(afterComa) + "/100] groszy";
        int number = amount.intValue();
        String amountInWords = "";
        int currentNumber;
        int u = 0, d = 0, h = 0;

        String[] unity = {"zero", "jeden", "dwa", "trzy", "cztery", "pięć", "sześć", "siedem", "osiem", "dziewięć"};
        String[] afterUnity = {"dziesięć", "jedenaście", "dwanaście", "trzynaście", "czternaście", "piętnaście", "szesnaście", "siedemnaście", "osiemnaście", "dziewiętnaście"};
        String[] dozens = {"", "dziesięć", "dwadzieścia", "trzydzieści", "czterdzieści", "pięćdziesiąt", "sześćdziesiąt", "siedemdziesiąt", "osiemdziesiąt", "dziewięćdziesiąt"};
        String[] hundreds = {"", "sto", "dwieście", "trzysta", "czterysta", "pięćset", "sześćset", "siedemset", "osiemset", "dziewięćset"};
        String[][] groups = {{"złoty", "złote", "złotych"},
                            {"tysiąc", "tysiące", "tysięcy"}};
        int groupNr = 0;
        int groupInd = 0;

        if (number == 0)
            return unity[0] + " " + groups[0][2] + pennyStr;

        while (number != 0 && groupNr < 2) {
            currentNumber = number % 1000;
            number /= 1000;
            h = currentNumber / 100;
            d = (currentNumber % 100) / 10;
            u = currentNumber % 10;

            if (currentNumber == 1)
                groupInd = 0;
            else if (currentNumber < 5 || (d != 1 && u > 1 && u < 5))
                groupInd = 1;
            else
                groupInd = 2;

            if (groupNr == 1 && currentNumber == 1) {
                amountInWords = " " + groups[groupNr][groupInd] + amountInWords;
            }
            else if (d == 1) {
                amountInWords = ((h != 0) ? " " + hundreds[h] : "") + ((d != 0) ? " " + afterUnity[u] + " " : "") + groups[groupNr][groupInd] + amountInWords;
            } else {
                amountInWords = ((h!= 0) ? " " + hundreds[h] : "") + ((d != 0) ? " " + dozens[d] : "") +((u != 0) ? " " + unity[u] : "") + " " + groups[groupNr][groupInd] + amountInWords;
            }

            groupNr++;
        }

        return amountInWords + pennyStr;
    }
}
