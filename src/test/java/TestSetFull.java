import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import pojo.CreateUserRequest;
import pojo.CreateUserResponse;
import pojo.RegisterUserRequest;
import pojo.RegisterUserResponse;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestSetFull extends BaseTest {

    @Test
    public void postLoginTestSet3() {
        given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("login")
                .then()
                .spec(defaultResponseSpecification())
                .body("token", notNullValue());
    }

    @Test
    public void getSingleUserTestSet3() {
        given()
                .get("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data.id", equalTo(2));
    }

    @Test
    public void deleteUserTestSet3() {
        given()
                .delete("users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    // patch update only a field
    @Test
    public void patchUserTestSet3() {
        String nameUpdated = given()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .patch("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("name");

        assertThat(nameUpdated, equalTo("morpheus"));
    }

    @Test
    public void putUserTestSet3() {
        String jobUpdated = given()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .put("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("job");

        assertThat(jobUpdated, equalTo("zion resident"));
    }

    @Test
    public void getAllUsersTestSet3() {
        Response response = given().get("users?page=2");
        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String contentType = response.getContentType();
        String body = response.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        System.out.println("******** main info from response ****************");
        System.out.println("content type:" + contentType);
        System.out.println("Status code:" + statusCode);
        System.out.println("body:" + body);
        System.out.println("headers:" + headers.toString());

        System.out.println("\n*** info from headers ******");
        System.out.println("content type:" + headers.get("Content-Type"));
        System.out.println("Transfer encoding:" + headers.get("Transfer-Encoding"));

    }

    //Using method from() of JsonPath to do parsing
    @Test
    public void getAllUsersTest2Set3() {
        String response = given()
                .when()
                .get("users?page=2").then().extract().body().asString();

        // parsing
        int page = from(response).get("page");
        int totalPages = from(response).get("total_pages");
        int idFirstUser = from(response).get("data[0].id");

        System.out.println("page: " + page);
        System.out.println("total pages: " + totalPages);
        System.out.println("id first user: " + idFirstUser);

        // create filters using lambda expressions to do parsing after
        List<Map> usersWithIdGreaterThan10 = from(response).get("data.findAll { x -> x.id > 10 }"); // lambda expression
        String email = usersWithIdGreaterThan10.get(0).get("email").toString();

        List<Map> usersListCustom = from(response).get("data.findAll { x -> x.id > 10 && x.last_name == 'Howell'}");
        int userId = Integer.parseInt(usersListCustom.get(0).get("id").toString());
    }

    // parsing String to java class (POJO)
    @Test
    public void createUserTestSet3() {
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
    public void createUserTestVersionPojoSet3() {
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
    public void registerUserTestSet3() {
        RegisterUserRequest userRequest = new RegisterUserRequest();
        userRequest.setEmail("eve.holt@reqres.in");
        userRequest.setPassword("pistol");

        RegisterUserResponse userResponse = given()
                .when()
                .body(userRequest)
                .post("register")
                .then()
                .spec(defaultResponseSpecification())
                .extract()
                .body()
                .as(RegisterUserResponse.class);

        assertThat(userResponse.getId(), equalTo(4));
        assertThat(userResponse.getToken(), equalTo("QpwL5tke4Pnpja7X4"));
    }



}
