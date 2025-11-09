package org.aeterasu.petstore;

public class PetStatus
{
    public static final String AVAILABLE = "available";
    public static final String PENDING = "pending";
    public static final String SOLD = "sold";

    public static String getStatusAvailable()
    {
        return AVAILABLE;
    }

    public static String getStatusPending()
    {
        return PENDING;
    }

    public static String getStatuSold()
    {
        return SOLD;
    }
}