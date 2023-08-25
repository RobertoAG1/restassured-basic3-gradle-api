
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import pojo.CreateUserRequest;
import pojo.CreateUserResponse;
import pojo.RegisterUserRequest;
import pojo.RegisterUserResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestSetParsingUsingPOJO extends BaseTest{

    // parsing String to java class (POJO)
    @Test
    public void createUserTestSet5() {
        String response = given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
                .post("users")
                .then().extract().body().asString();

        // You have to use library 'jackson-databind' or similar to do the deserialization correctly
        // it's needed to add this library dependency to gradle compiler

        // set the POJO of the response of user creation endpoint
        CreateUserResponse userResponse = from(response).getObject("", CreateUserResponse.class);
        // Verifications
        System.out.println(userResponse.getId());
        System.out.println(userResponse.getJob());
        System.out.println(userResponse.getName());

        assertThat(userResponse.getName(), equalTo("morpheus"));
        assertThat(userResponse.getJob(), equalTo("leader"));
    }

    // using create user endpoint
    @Test
    public void createUserTestVersionPojoSet5() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setName("morpheus");
        userRequest.setJob("leader");

        CreateUserResponse userResponse = given()
                .when()
                .body(userRequest)
                .post("users")
                .then()
                .extract()
                .body()
                .as(CreateUserResponse.class);

        assertThat(userResponse.getName(), equalTo("morpheus"));
        assertThat(userResponse.getJob(), equalTo("leader"));
    }

    // using register user endpoint with a POST
    @Test
    public void registerUserTestSet5() {
        RegisterUserRequest userRequest = new RegisterUserRequest();
        userRequest.setEmail("eve.holt@reqres.in");
        userRequest.setPassword("pistol");

        RegisterUserResponse userResponse = given()
                .when()
                .body(userRequest)
                .post("register")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .body()
                .as(RegisterUserResponse.class);

        assertThat(userResponse.getId(), equalTo(4));
        assertThat(userResponse.getToken(), equalTo("QpwL5tke4Pnpja7X4"));
    }
}
