package org.aeterasu.petstore.user;

import java.net.http.*;

import org.aeterasu.petstore.Api;
import org.aeterasu.petstore.HttpUtils;
import org.json.JSONObject;

public class User
{
	private long id = 0;
	private String username = "";
	private String firstName = "";
	private String lastName = "";
	private String email = "";
	private String password = "";
	private String phone = "";
	private int userStatus = 0;

	public User
	(
		long id,
		String username,
		String firstName,
		String lastName,
		String email,
		String password,
		String phone,
		int userStatus
	)
	{
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.userStatus = userStatus;
	}

	public long getId()
	{
		return this.id;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public String getEmail()
	{
		return this.email;
	}

	public String getPassword()
	{
		return this.password;
	}

	public String getPhone()
	{
		return this.phone;
	}

	public int getUserStatus()
	{
		return this.userStatus;
	}

	public JSONObject getJson()
	{
		JSONObject result = new JSONObject()
			.put("id", id)
			.put("username", username)
			.put("firstName", firstName)
			.put("lastName", lastName)
			.put("email", email)
			.put("password", password)
			.put("phone", phone)
			.put("userStatus", userStatus);

		return result;
	}

	public static HttpResponse<String> login(String username, String password) throws Exception
	{
		String url = String.format(Api.BASE_URL + "/user/login?username=%s&password=%s",
				java.net.URLEncoder.encode(username, "UTF-8"),
				java.net.URLEncoder.encode(password, "UTF-8"));

		return HttpUtils.get(url);
	}

	public static HttpResponse<String> logout() throws Exception
	{
		HttpResponse<String> response = HttpUtils
			.get(Api.BASE_URL + "/user/logout");

		return response;
	}
}