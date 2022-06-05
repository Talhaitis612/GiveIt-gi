package com.example.giveit_gi.Utils;

import com.example.giveit_gi.Models.Donor;

import java.util.Random;

public class CONSTANTS {
    public static Donor currentloggedInDonor;


    public static final String USER_EMAIL_KEY = "EMAIL";
    public static final String USER_PASSWORD_KEY = "PASSWORD";

    public static final String DONOR_COLLECTION_PATH = "donors";

    public static final String DONATION_COLLECTION_PATH = "donation";


    public static final String DONATIONS_PICTURES_PATH = "donation-pictures";

    public static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnmabcdefgh";

    public static String generateUniqueKey(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}
