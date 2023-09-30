import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics {
    public static void main(String[] args) {
        // Given - All input details,
        // When - Submit API - resource, http method,
        // There - Validate Response

        // Add a Place
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response =
                given().log().all().queryParam("key", "qaclick123") // GIVEN
                .header("Content-Type", "application/json")
                .body(payload.AddPlace())
                .when().post("maps/api/place/add/json") // WHEN POST -------
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))// THEN - Validate Response
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();

        System.out.println(response);
        JsonPath jsonPath = new JsonPath(response);
        String placeId = jsonPath.getString("place_id");

        System.out.println(placeId);

        // Update Place
        String newAddress = "Summer Wal, Africa";
        given().log().all().queryParam("key", "qaclick123") // GIVEN
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("maps/api/place/update/json") // WHEN PUT ------
                .then().assertThat().log().all().statusCode(200) // THEN
                .body("msg", equalTo("Address successfully updated"));

        // Get PLace
        String getPlaceResponse =
        given().log().all().queryParam("key", "qaclick123") // GIVEN
                .queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json") // WHEN GET --------
                .then().assertThat().log().all().statusCode(200).extract().response().asString();
    }
}