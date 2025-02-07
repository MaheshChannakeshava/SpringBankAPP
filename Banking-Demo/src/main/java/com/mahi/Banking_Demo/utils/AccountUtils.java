package com.mahi.Banking_Demo.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "The User Already As AN Account!!";

    public static final String ACCOUNT_CREATION_CODE ="002";
    public static final String ACCOUNT_CREATION_MESSAGE = "ACCOUNT CREATED SUCCESSFULLY";

        public static String generateAccountNumber(){
        /* Here are we are trying to create random account number while creating an new user */
        /* Its of pattern Year+  9 random digits     */

        Year currentYear = Year.now();

        int min = 100000000;
        int max = 999999999;

        //Here we are creating an random number between the above min and max

        int randomNumber = (int) Math.floor(Math.random()* (max- min + 1) + min);

        //Now we have concatenate the above two values by converting them to string

        String year = String.valueOf(currentYear);
        String randNumber = String.valueOf(randomNumber);

        return year + randNumber;


    }
}
