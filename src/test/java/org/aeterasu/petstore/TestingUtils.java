package org.aeterasu.petstore;

import java.util.UUID;

public class TestingUtils
{
	public static long getRandomId()
	{
		return Math.abs(UUID.randomUUID().getMostSignificantBits());
	}
}