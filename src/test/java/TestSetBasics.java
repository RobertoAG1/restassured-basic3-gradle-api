import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestSetBasics extends BaseTest{

    @Test
    public void postLoginTestSet1(){

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("https://reqres.in/api/login")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("token",notNullValue());
    }

    @Test
    public void getSingleUserTestSet1(){

        RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .get("https://reqres.in/api/users/2")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("data.id",equalTo(2));
    }
}
