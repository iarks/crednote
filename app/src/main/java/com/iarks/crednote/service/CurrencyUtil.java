package com.iarks.crednote.service;

import java.math.BigDecimal;

public class CurrencyUtil {


    /* The first string is not used, it is to make
            array indexing simple */
    private static final String[] tillTen = new String[] {
            "", "One", "Two", "Three", "Four",
            "Five", "Six", "Seven", "Eight", "Nine", "Ten"
    };

    /* The first string is not used, it is to make
        array indexing simple */
    private static final String[] elevenToNineteen = new String[] {
            "",          "Eleven",  "Twelve",
            "Thirteen",  "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"
    };

    /* The first two string are not used, they are to
     * make array indexing simple*/
    private static final String[] tensMultiple = new String[] {
            "",      "",      "Twenty",  "Thirty", "Forty",
            "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };


    public static String getMoneyInWords(BigDecimal money)
    {
        if(BigDecimal.ZERO.equals(money))
            return "Zero";

        String moneyInString = String.valueOf(money);
        String[] decimalSplit = moneyInString.split("\\.");
        int principal = Integer.parseInt(decimalSplit[0]);
        int change = Integer.parseInt(decimalSplit[1]);

        StringBuilder result = new StringBuilder();
        int lakh = principal/100000;
        principal = principal%100000;

        if(lakh!=0)
            result.append(getDoubleDigitNumberInWords(lakh) + " Lakh, ");

        int thousand = principal/1000;
        principal = principal % 1000;

        if(thousand!=0) {
            result.append(getDoubleDigitNumberInWords(thousand) + " thousand, ");
        }

        int hundred = principal/100;
        principal = principal%100;

        if(hundred!=0) {
            result.append(getDoubleDigitNumberInWords(hundred) + " hundred ");
        }

        if(principal!=0)
            result.append(getDoubleDigitNumberInWords(principal)+" ");

        String changeInWords = getDoubleDigitNumberInWords(change);
        result.append("Rupees and "+ (changeInWords.equals("") ? "Zero": changeInWords) + " Paise");

        return result.toString();
    }

    private static String getDoubleDigitNumberInWords(int num)
    {
        num = Math.abs(num);

        System.out.println("tryin to eval" + num);
        if(num<=10)
            return tillTen[num];

        if(num>=11 && num<=20)
            return elevenToNineteen[num%10];

        int tensDigit = num/10;
        int onesDigit = num%10;

        return tensMultiple[tensDigit]+" "+tillTen[onesDigit];
    }
}
