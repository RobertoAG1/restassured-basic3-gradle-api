
import org.apache.http.HttpStatus;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestSetWithFilters extends BaseTest{

    @Test
    public void postLoginTestSet7(){
        given()
        .body("{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}")
        .post("login")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("token",notNullValue());
    }

    @Test
    public void getSingleUserTestSet7(){
        given()
        .get("users/2")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("data.id",equalTo(2));
    }
}
