package org.aeterasu.petstore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils 
{
	private static final HttpClient client = HttpClient.newHttpClient();

	public static HttpClient getClient()
	{
		return client;
	}

	public static HttpResponse<String> get(String url) throws Exception 
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.GET()
			.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> postJson(String url, String jsonBody) throws Exception 
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.header("Content-Type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(jsonBody))
			.build();

		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public static HttpResponse<String> putJson(String url, String jsonBody) throws Exception 
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.header("Content-Type", "application/json")
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

	public static HttpResponse<String> delete(String url, String headerName, String headerValue) throws Exception 
	{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.DELETE()
			.header(headerName, headerValue)
			.build();
			
		return client.send(request, HttpResponse.BodyHandlers.ofString());
	}
}