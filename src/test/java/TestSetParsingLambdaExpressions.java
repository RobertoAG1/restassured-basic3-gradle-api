import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import jdk.jfr.Category;
import org.junit.Test;


import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;


public class TestSetParsingLambdaExpressions extends BaseTest{

    //Using method from() of JsonPath to do parsing
    @Test
    public void getAllUsersTest2Set4(){
        String response = given()
                .when()
                .get("users?page=2").then().extract().body().asString();

        // parsing
        int page = from(response).get("page");
        int totalPages = from(response).get("total_pages");
        int idFirstUser = from(response).get("data[0].id");

        System.out.println("page: "+page);
        System.out.println("total pages: " + totalPages);
        System.out.println("id first user: " + idFirstUser);

        // create filters using lambda expressions to do parsing after
        List<Map> usersWithIdGreaterThan10 = from(response).get("data.findAll { x -> x.id > 10 }"); // lambda expression
        String email = usersWithIdGreaterThan10.get(0).get("email").toString();

        List<Map> usersListCustom = from(response).get("data.findAll { x -> x.id > 10 && x.last_name == 'Howell'}");
        int userId = Integer.parseInt(usersListCustom.get(0).get("id").toString());
    }
}
