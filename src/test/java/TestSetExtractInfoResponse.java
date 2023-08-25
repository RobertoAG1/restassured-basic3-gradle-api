
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestSetExtractInfoResponse extends BaseTest{

    @Test
    public void getAllUsersTestSet2(){
        Response response = given().get("users?page=2");
        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String contentType = response.getContentType();
        String body =   response.getBody().asString();

        assertThat(statusCode,equalTo(HttpStatus.SC_OK));
        System.out.println("******** main info from response ****************");
        System.out.println("* content type:\n" + contentType);
        System.out.println("* Status code:\n" + statusCode);
        System.out.println("* body:\n" + body);
        System.out.println("* headers:\n" + headers.toString());

        System.out.println("\n*** info from headers ******");
        System.out.println("* content type:\n" + headers.get("Content-Type"));
        System.out.println("* Transfer encoding:\n" + headers.get("Transfer-Encoding"));

    }
}
