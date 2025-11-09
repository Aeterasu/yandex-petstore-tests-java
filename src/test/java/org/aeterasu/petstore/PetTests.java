package org.aeterasu.petstore;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import org.json.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetTests
{
	private static long testPetId = 0;

	@BeforeAll
	public static void initId()
	{
		testPetId = TestingUtils.getRandomId();
	}

	@Test
	@Order(1)
	public void testPostCreatePet() throws Exception
	{
	
		JSONObject json = new JSONObject()
			.put("id", testPetId)
			.put("name", "bobby")
			.put("status", "available");

		HttpResponse<String> response = HttpUtils.postJson(ApiInfo.BASE_URL + "/pet/", json.toString());
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(2)
	public void testPutUpdatePet() throws Exception
	{
		JSONObject json = new JSONObject()
			.put("id", testPetId)
			.put("name", "bobby")
			.put("status", "sold");

		HttpResponse<String> response = HttpUtils.putJson(ApiInfo.BASE_URL + "/pet/", json.toString());
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(3)
	public void testGetPetById() throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(ApiInfo.BASE_URL + "/pet/" + Long.toString(testPetId));
		assertEquals(200, response.statusCode());
	}

	@ParameterizedTest
	@ValueSource(strings = {"available", "pending", "sold"})
	@Order(4)
	public void testGetPetByStatus(String status) throws Exception
	{
		HttpResponse<String> response = HttpUtils.get(ApiInfo.BASE_URL + "/pet/findByStatus?status=" + status);
		assertEquals(200, response.statusCode());
	}

	@Test
	@Order(5)
	public void testPutUpdateFormPet() throws Exception
	{
		JSONObject json = new JSONObject()
			.put("id", testPetId)
			.put("name", "billy")
			.put("status", "pending");

		HttpResponse<String> response = HttpUtils.putJson(ApiInfo.BASE_URL + "/pet/", json.toString());
		assertEquals(200, response.statusCode());
	}

	// TODO: cleanup
	// form-data expect very rigid formatting, not sure how to work around this!
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
			.uri(URI.create(ApiInfo.BASE_URL  + "/pet/" + Long.toString(testPetId) + "/uploadImage"))
			.header("Content-Type", "multipart/form-data; boundary=" + boundary)
			.POST(HttpRequest.BodyPublishers.ofByteArray(body))
			.build();

		HttpResponse<String> response = HttpUtils.getClient().send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
	}

	// TODO: cleanup and use HttpUtils

	@Test
	@Order(7)
	public void testDeletePet() throws Exception
	{
		HttpResponse<String> response = HttpUtils.delete(ApiInfo.BASE_URL + "/pet/" + Long.toString(testPetId), "api_key", ApiInfo.API_KEY);
		
		assertEquals(200, response.statusCode());
	}
}