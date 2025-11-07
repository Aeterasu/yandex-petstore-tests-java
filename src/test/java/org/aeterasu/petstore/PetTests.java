package org.aeterasu.petstore;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import org.json.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PetTests
{
	// TODO: POST create valid pet
	// TODO: POST create invalid pet!

	// TODO: GET invalid pet!

	private static final int PET_ID = 999;

	private static final String API_KEY = "special-key";

	@Test
	@Order(1)
	public void testPostCreatePet() throws Exception
	{
		JSONObject json = new JSONObject()
			.put("id", PET_ID)
			.put("name", "bobby")
			.put("status", "available");

		HttpResponse<String> response = HttpUtils.post(HttpUtils.BASE_URL + "/pet", json.toString(), HttpUtils.APPLICATION_JSON);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(2)
	public void testPutUpdatePet() throws Exception
	{
		JSONObject json = new JSONObject()
			.put("id", PET_ID)
			.put("name", "bobby")
			.put("status", "sold");

		HttpResponse<String> response = HttpUtils.put(HttpUtils.BASE_URL + "/pet", json.toString(), HttpUtils.APPLICATION_JSON);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(3)
	public void testGetPetById() throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(HttpUtils.BASE_URL + "/pet/" + Integer.toString(PET_ID));
		assertEquals(200, response.statusCode());
	}

	@ParameterizedTest
	@ValueSource(strings = {"available", "pending", "sold"})
	@Order(4)
	public void testGetPetByStatus(String status) throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(HttpUtils.BASE_URL + "/pet/findByStatus?status=" + status);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(5)
	public void testPutUpdateFormPet() throws Exception
	{
		JSONObject json = new JSONObject()
			.put("id", PET_ID)
			.put("name", "billy")
			.put("status", "pending");

		HttpResponse<String> response = HttpUtils.put(HttpUtils.BASE_URL + "/pet", json.toString(), HttpUtils.APPLICATION_JSON);
		assertEquals(200, response.statusCode());
	}

	// TODO: cleanup and use HttpUtils

	@Test
	@Order(6)
	public void testPostUploadImage() throws Exception
	{
		java.nio.file.Path imagePath = java.nio.file.Paths.get("src/test/resources/fumo.jpg");

		String boundary = "Boundary-" + System.currentTimeMillis();

		String metadataPart = "--" + boundary + "\r\n" +
				"Content-Disposition: form-data; name=\"additionalMetadata\"\r\n\r\n" +
				"My pet image\r\n";

		String fileHeader = "--" + boundary + "\r\n" +
				"Content-Disposition: form-data; name=\"file\"; filename=\"" + imagePath.getFileName() + "\"\r\n" +
				"Content-Type: image/jpeg\r\n\r\n";

		String closingBoundary = "\r\n--" + boundary + "--\r\n";

		byte[] metaBytes = metadataPart.getBytes();
		byte[] headerBytes = fileHeader.getBytes();
		byte[] closingBytes = closingBoundary.getBytes();

		byte[] fileBytes = java.nio.file.Files.readAllBytes(imagePath);

		byte[] body = new byte[metaBytes.length + headerBytes.length + fileBytes.length + closingBytes.length];
		System.arraycopy(metaBytes, 0, body, 0, metaBytes.length);
		System.arraycopy(headerBytes, 0, body, metaBytes.length, headerBytes.length);
		System.arraycopy(fileBytes, 0, body, metaBytes.length + headerBytes.length, fileBytes.length);
		System.arraycopy(closingBytes, 0, body, metaBytes.length + headerBytes.length + fileBytes.length, closingBytes.length);

		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(HttpUtils.BASE_URL  + "/pet/" + Integer.toString(PET_ID) + "/uploadImage"))
			.header("Content-Type", "multipart/form-data; boundary=" + boundary)
			.POST(HttpRequest.BodyPublishers.ofByteArray(body))
			.build();

		HttpResponse<String> response = HttpUtils.client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
	}

	// TODO: cleanup and use HttpUtils

	@Test
	@Order(7)
	public void testDeletePet() throws Exception
	{
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(HttpUtils.BASE_URL + "/pet/" + PET_ID))
				.header("api_key", API_KEY)
				.DELETE()
				.build();

		HttpResponse<String> response = HttpUtils.client.send(request, HttpResponse.BodyHandlers.ofString());
		
		assertEquals(200, response.statusCode());	

		// check if we can GET a deleted pet

		HttpResponse<String> getInvalidPetResponse = HttpUtils.get(HttpUtils.BASE_URL + "/pet/" + PET_ID);
    	assertEquals(404, getInvalidPetResponse.statusCode());
	}
}