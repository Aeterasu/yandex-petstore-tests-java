package org.aeterasu.petstore;
import org.aeterasu.petstore.user.User;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.*;

import java.net.http.HttpResponse;

public class UserTests
{
	@Test
	public void testPostCreateUser() throws Exception
	{
		User user = new User(
			TestingUtils.getRandomId(), 
			"Kartoshka",
			"Ivan",
			"Ivanovich",
			"ivan@example.com",
			"1234567890",
			"+1234567890",
			0
			);

		HttpResponse<String> response = HttpUtils.postJson(Api.BASE_URL + "/user/", user.getJson().toString());
		assertEquals(200, response.statusCode());
	}

	@Test
	public void testPostCreateInvalidUser() throws Exception
	{
		HttpResponse<String> response = HttpUtils.postJson(Api.BASE_URL + "/user/", "");
		assertEquals(405, response.statusCode());
	}

    @Nested
	@SuppressWarnings("unused")
    class ExistingUserTest
	{
        private User testUser = null;

        @BeforeEach
        public void setupUser() throws Exception
        {
			long id = TestingUtils.getRandomId();

			testUser = new User(
				TestingUtils.getRandomId(), 
				"Kartoshka" + Long.toString(id),
				"Ivan",
				"Ivanovich",
				"ivan@example.com",
				"1234567890",
				"+1234567890",
				0
				);

			HttpResponse<String> response = HttpUtils.postJson(Api.BASE_URL + "/user/", testUser.getJson().toString());
			assertEquals(200, response.statusCode());

			TestingUtils.pollAwait(() ->
			{
				HttpResponse<String> getResponse = HttpUtils.get(Api.BASE_URL + "/user/" + testUser.getUsername());
				assertEquals(200, getResponse.statusCode());
			});
        }

		@Test
		public void testPutUpdateUser() throws Exception
		{
			User.login(testUser.getUsername(), testUser.getPassword());

			User newUser = new User(
				TestingUtils.getRandomId(), 
				"Kapusta",
				"Andrey",
				"Andreevich",
				"ivan@example.com",
				"1234567890",
				"+1234567890",
				0
				);

			HttpResponse<String> response = HttpUtils
				.putJson(Api.BASE_URL + "/user/" + testUser.getUsername(), newUser.getJson().toString());
			assertEquals(200, response.statusCode());
		}

		@Test
		public void testLogin() throws Exception
		{
			HttpResponse<String> response = User.login(testUser.getUsername(), testUser.getPassword());
			assertEquals(200, response.statusCode());
		}

		@Test
		void testGetUser() throws Exception 
		{
			TestingUtils.pollAwait(() ->
			{
				HttpResponse<String> response = HttpUtils.get(Api.BASE_URL + "/user/" + testUser.getUsername());
				assertEquals(200, response.statusCode());
			});
		}

		@Test
		public void testLogout() throws Exception
		{
			HttpResponse<String> response = User.logout();
			assertEquals(200, response.statusCode());
		}

		@Test
		public void testDeleteUser() throws Exception
		{
			User.login(testUser.getUsername(), testUser.getPassword());

			HttpResponse<String> loginResponse = User.login(testUser.getUsername(), testUser.getPassword());
			assertEquals(200, loginResponse.statusCode());

			TestingUtils.pollAwait(() ->
			{
				HttpResponse<String> response = HttpUtils
					.delete(Api.BASE_URL + "/user/" + testUser.getUsername());

				assertEquals(200, response.statusCode());
			});
		}
	}

	@Test
	public void testCreateUsersWithArray() throws Exception
	{
		User userA = new User(
			TestingUtils.getRandomId(),
			"UserA",
			"John",
			"California",
			"john@example.com",
			"1234567890",
			"+1234567890",
			0			
		);

		User userB = new User(
			TestingUtils.getRandomId(),
			"UserB",
			"Ivan",
			"Rusich",
			"ivan@example.ru",
			"1234567890",
			"+1234567890",
			0			
		);

		JSONArray arr = new JSONArray();
		arr.put(userA.getJson());
		arr.put(userB.getJson());

		// no, I have no idea why there's two identical methods with different names!

		// createWithArray
		HttpResponse<String> respArray = HttpUtils
			.postJson(Api.BASE_URL + "/user/createWithArray/", arr.toString());
		assertEquals(200, respArray.statusCode());

		// createWithList
		HttpResponse<String> respList = HttpUtils
			.postJson(Api.BASE_URL + "/user/createWithList/", arr.toString());
		assertEquals(200, respList.statusCode());
	}

	@Test
	public void testCreateUsersWithInvalidArray() throws Exception
	{
		// no, I have no idea why there's two identical methods with different names!

		// createWithArray
		HttpResponse<String> respArray = HttpUtils
			.postJson(Api.BASE_URL + "/user/createWithArray/", "");
		assertEquals(405, respArray.statusCode());

		// createWithList
		HttpResponse<String> respList = HttpUtils
			.postJson(Api.BASE_URL + "/user/createWithList/", "");
		assertEquals(405, respList.statusCode());
	}
}