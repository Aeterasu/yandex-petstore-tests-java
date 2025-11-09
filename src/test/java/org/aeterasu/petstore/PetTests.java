package org.aeterasu.petstore;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.awaitility.Awaitility.await;
import java.time.Duration;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PetTests
{
	// Tests for creating pets
    @Nested
    class CreatePetTests
    {
		// Creating a new pet using a valid JSON
        @Test
        public void testPostCreatePet() throws Exception
        {
            long id = TestingUtils.getRandomId();
            Pet pet = new Pet(id, "Bobby", PetStatus.getStatusAvailable());
            HttpResponse<String> response = HttpUtils.postJson(Api.BASE_URL + "/pet/", pet.getJson().toString());
            assertEquals(200, response.statusCode());
        }

		// Creating a new pet using an invalid JSON
        @Test
        public void testPostCreateInvalidPet() throws Exception
        {
            HttpResponse<String> response = HttpUtils.postJson(Api.BASE_URL + "/pet/", "");
            assertEquals(405, response.statusCode());
        }

		@Test
        public void testPutUpdateInvalidPet() throws Exception
        {
            HttpResponse<String> response = HttpUtils.putJson(Api.BASE_URL + "/pet/", "");
            assertEquals(405, response.statusCode());
        }
    }
	
    @Nested
    class ExistingPetTests
    {
        private Pet testPet = null;
        private long petId = 0;

        @BeforeEach
        public void setupPet() throws Exception
        {
            petId = TestingUtils.getRandomId();
            testPet = new Pet(petId, "Bobby" + petId, PetStatus.getStatusAvailable());

            HttpResponse<String> postResponse = HttpUtils.postJson(Api.BASE_URL + "/pet/", testPet.getJson().toString());
            assertEquals(200, postResponse.statusCode());

			// poll the api to make sure out pet is actually created
			// this accounts for API latency
			// i had the issue that if I, say, create and instantly get then the tests shit itself
			// hopefully this will fix this - tests will fail only if the API is completely dead :
			// which seems appropriate!

			// we will do a lot of the same later!
            await()
                .atMost(Duration.ofSeconds(Api.MAX_WAIT_TIME))
                .pollInterval(Duration.ofMillis(Api.POLL_INTERVAL))
                .untilAsserted(() -> 
					{
						HttpResponse<String> getResponse = HttpUtils.get(Api.BASE_URL + "/pet/" + petId);
						assertEquals(200, getResponse.statusCode());
					}
				);
        }

		// Retrieve pets by status
        @ParameterizedTest
        @ValueSource(strings = { PetStatus.AVAILABLE, PetStatus.PENDING, PetStatus.SOLD })
        public void testGetPetByStatus(String status) throws Exception
        {
            String url = Api.BASE_URL + "/pet/findByStatus?status=" + status;
            HttpResponse<String> response = HttpUtils.get(url);
            assertEquals(200, response.statusCode());
        }

		// Get pet by ID
        @Test
        public void testGetPetById() throws Exception
        {
            await()
                .atMost(Duration.ofSeconds(Api.MAX_WAIT_TIME))
                .pollInterval(Duration.ofMillis(Api.POLL_INTERVAL))
                .untilAsserted(() -> 
					{
						HttpResponse<String> response = HttpUtils.get(Api.BASE_URL + "/pet/" + Long.toString(petId));
						assertEquals(200, response.statusCode());
					}
				);
        }

		// PUT update a pet!
        @Test
        public void testPutUpdatePet() throws Exception
        {
            Pet updatedPet = new Pet(petId, "Billy", PetStatus.getStatuSold());
			HttpResponse<String> response = HttpUtils.putJson(Api.BASE_URL + "/pet/", updatedPet.getJson().toString());
			assertEquals(200, response.statusCode());
        }

        @Test
        public void testPostUploadImage() throws Exception
        {
            Path imagePath = Paths.get("src/test/resources/fumo.jpg");
            String boundary = "Boundary-" + System.currentTimeMillis();
            
            byte[] body = buildMultipartBody(boundary, "cool pet image", imagePath);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Api.BASE_URL + "/pet/" + Long.toString(petId) + "/uploadImage"))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();

            HttpResponse<String> response = HttpUtils.getClient().send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
        }

        @Test
        public void testDeletePet() throws Exception
        {
            await()
                .atMost(Duration.ofSeconds(Api.MAX_WAIT_TIME))
                .pollInterval(Duration.ofMillis(Api.POLL_INTERVAL))
                .untilAsserted(() -> 
                {
                    HttpResponse<String> response = HttpUtils.delete(Api.BASE_URL + "/pet/" + Long.toString(petId), "api_key", Api.API_KEY);
                    assertEquals(200, response.statusCode());
                });
        }
    }

	// form-multipart-whatever expect very rigid formatting, not sure how to work around this!
    private byte[] buildMultipartBody(String boundary, String metadata, Path imagePath) throws Exception
    {
        String metadataPart = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"additionalMetadata\"\r\n\r\n" +
                metadata + "\r\n";

        String fileHeader = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + imagePath.getFileName() + "\"\r\n" +
                "Content-Type: " + Files.probeContentType(imagePath) + "\r\n\r\n";

        String closingBoundary = "\r\n--" + boundary + "--\r\n";

        byte[] metaBytes = metadataPart.getBytes();
        byte[] headerBytes = fileHeader.getBytes();
        byte[] closingBytes = closingBoundary.getBytes();
        byte[] fileBytes = Files.readAllBytes(imagePath);

        byte[] body = new byte[metaBytes.length + headerBytes.length + fileBytes.length + closingBytes.length];
        System.arraycopy(metaBytes, 0, body, 0, metaBytes.length);
        System.arraycopy(headerBytes, 0, body, metaBytes.length, headerBytes.length);
        System.arraycopy(fileBytes, 0, body, metaBytes.length + headerBytes.length, fileBytes.length);
        System.arraycopy(closingBytes, 0, body, metaBytes.length + headerBytes.length + fileBytes.length, closingBytes.length);

        return body;
    }
}