package org.aeterasu.petstore;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.json.*;

import java.net.http.HttpResponse;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests
{
    private static final int USER_ID = 999;
    private static final String USERNAME = "Kartoshka";
    private static final String PASSWORD = "1234567890";

    private JSONObject makeUser(String username, String password) 
	{
        long id = Math.abs(UUID.randomUUID().getMostSignificantBits());
        return new JSONObject()
                .put("id", id)
                .put("username", username)
                .put("firstName", "Fn")
                .put("lastName", "Ln")
                .put("email", username + "@example.com")
                .put("password", password)
                .put("phone", "1234567890")
                .put("userStatus", 0);
    }

	@Test
	@Order(21)
	public void testPostCreateUser() throws Exception
	{
		JSONObject json = new JSONObject()
            .put("id", USER_ID)
			.put("username", USERNAME)
			.put("firstName", "Ivan")
			.put("lastName", "Invanovich")
            .put("email", "kartoshka@kartoshka.com")
            .put("password", PASSWORD)
            .put("phone", "1234567890")
            .put("userStatus", 0);

		HttpResponse<String> response = HttpUtils.post(HttpUtils.BASE_URL + "/user/", json.toString(), HttpUtils.APPLICATION_JSON);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(22)
	public void testLogin() throws Exception
	{
        String username = "testuser";
        String password = "12345";

        // Build URL with query parameters
        String url = String.format(HttpUtils.BASE_URL + "/user/login?username=%s&password=%s",
                java.net.URLEncoder.encode(username, "UTF-8"),
                java.net.URLEncoder.encode(password, "UTF-8"));

		HttpResponse<String> response = HttpUtils.get(url);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(23)
	public void testPutUpdateUser() throws Exception
	{
		JSONObject json = new JSONObject()
            .put("id", USER_ID)
			.put("username", USERNAME)
			.put("firstName", "Andrey")
			.put("lastName", "Andreevich")
            .put("email", "kartoshka@kartoshka.com")
            .put("password", "1234567890")
            .put("phone", "1234567890")
            .put("userStatus", 0);

		HttpResponse<String> response = HttpUtils.put(HttpUtils.BASE_URL + "/user/" + USERNAME, json.toString(), HttpUtils.APPLICATION_JSON);
		assertEquals(200, response.statusCode());
	}

    @Test
    @Order(24)
    public void testGetUser() throws Exception
    {
		HttpResponse<String> response = HttpUtils.get(HttpUtils.BASE_URL + "/user/" + USERNAME);
		assertEquals(200, response.statusCode());        
    }

	@Test
	@Order(25)
	public void testLogout() throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(HttpUtils.BASE_URL + "/user/logout");
		assertEquals(200, response.statusCode());
	}


	@Test
	@Order(26)
	public void testDeleteUser() throws Exception
	{
		HttpResponse<String> response = HttpUtils.delete(HttpUtils.BASE_URL + "/user/" + USERNAME);
		
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(27)
	public void testCreateUsersWithArray() throws Exception
	{
		String u1 = "userA_" + UUID.randomUUID().toString().substring(0, 6);
        String u2 = "userB_" + UUID.randomUUID().toString().substring(0, 6);

        JSONObject user1 = makeUser(u1, "password");
        JSONObject user2 = makeUser(u2, "password");

        JSONArray arr = new JSONArray();
        arr.put(user1);
        arr.put(user2);

        // createWithArray
        HttpResponse<String> respArray = HttpUtils
			.post(HttpUtils.BASE_URL + "/user/createWithArray/", arr.toString(), HttpUtils.APPLICATION_JSON);
        assertEquals(200, respArray.statusCode());

        // createWithList (sending same array is fine)
        HttpResponse<String> respList = HttpUtils
			.post(HttpUtils.BASE_URL + "/user/createWithList/", arr.toString(), HttpUtils.APPLICATION_JSON);
        assertEquals(200, respList.statusCode());
	}
}