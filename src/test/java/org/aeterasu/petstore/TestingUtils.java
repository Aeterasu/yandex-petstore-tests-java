package org.aeterasu.petstore;

import java.time.Duration;
import java.util.UUID;

import static org.awaitility.Awaitility.*;
import org.awaitility.core.ThrowingRunnable;

public class TestingUtils
{
	public static long getRandomId()
	{
		return Math.abs(UUID.randomUUID().getMostSignificantBits());
	}

	public static void pollAwait(ThrowingRunnable assertion)
	{
		await()
			.atMost(Duration.ofSeconds(Api.MAX_WAIT_TIME))
			.pollInterval(Duration.ofMillis(Api.POLL_INTERVAL))
			.untilAsserted(() -> assertion.run());
	}
}