package org.aeterasu.petstore;

import java.net.http.*;

public class UserUtils
{
    public static HttpResponse<String> login(String username, String password) throws Exception
	{
        String url = String.format(ApiInfo.BASE_URL + "/user/login?username=%s&password=%s",
                java.net.URLEncoder.encode(username, "UTF-8"),
                java.net.URLEncoder.encode(password, "UTF-8"));

		return HttpUtils.get(url);
	}

    public static HttpResponse<String> logout() throws Exception
    {
        HttpResponse<String> response = HttpUtils
			.get(ApiInfo.BASE_URL + "/user/logout");

        return response;
    }
}