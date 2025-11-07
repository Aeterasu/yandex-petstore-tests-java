package org.aeterasu.petstore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils 
{
	public static final HttpClient client = HttpClient.newHttpClient();

	public static final String BASE_URL = "https://petstore.swagger.io/v2";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";

	public static HttpResponse<String> get(String url) throws Exception 
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.GET()
			.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> post(String url, String jsonBody, String contentType) throws Exception 
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.header(CONTENT_TYPE, contentType)
			.POST(HttpRequest.BodyPublishers.ofString(jsonBody))
			.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> put(String url, String jsonBody, String contentType) throws Exception 
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
			.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> delete(String url) throws Exception 
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.DELETE()
			.build();
			
		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}    
}