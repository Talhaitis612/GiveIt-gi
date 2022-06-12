package com.example.giveit_gi.Utils;

import com.example.giveit_gi.Models.Donor;

import java.util.Random;

public class CONSTANTS {
    public static Donor currentloggedInDonor;


    public static final String USER_EMAIL_KEY = "EMAIL";
    public static final String USER_PASSWORD_KEY = "PASSWORD";

    public static final String DONOR_COLLECTION_PATH = "donors";

    public static final String DONATION_COLLECTION_PATH = "donation-item";

    public static final String REQUEST_ITEM_COLLECTION_PATH = "requested-donation";

    public static final String APPLY_ITEM_COLLECTION_PATH = "applied-donation";

    public static final String EVENT_COLLECTION_PATH = "events";





    public static final String DONATIONS_PICTURES_PATH = "donation-pictures";

    public static final String EVENTS_PICTURES_PATH = "event-pictures";


    public static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnmabcdefgh";

//  FOR INTENT
    public static final String DONOR_ID = "donorID";

    public static final String DONATION_ID = "donationID";
    public static final String DONATION_TITLE = "donationTitle";
    public static final String DONATION_DESCRIPTION = "donationDescription";
    public static final String DONATION_CATEGORY = "donationCategory";
    public static final String DONATION_LOCATION = "donationLocation";
    public static final String DONATION_IMAGE_URL = "donationImageURL";
    public static final String DONATION_TIME = "donationTime";





    public static String generateUniqueKey(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }



}
