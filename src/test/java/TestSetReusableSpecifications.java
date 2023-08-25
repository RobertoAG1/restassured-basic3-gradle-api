
import org.junit.Test;
import pojo.RegisterUserRequest;
import pojo.RegisterUserResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestSetReusableSpecifications extends BaseTest{


    // using register user endpoint with a POST
    @Test
    public void registerUserTestSet6() {
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
