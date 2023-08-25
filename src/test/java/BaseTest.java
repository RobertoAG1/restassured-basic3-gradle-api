import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeClass
    public static void setup() {
        logger.info("Starting the configuration");
        RestAssured.requestSpecification = defaultRequestSpecification();
        logger.info("Configuration successful");
    }

    private static RequestSpecification defaultRequestSpecification(){

        List<Filter> filters = new ArrayList<>();
        filters.add(new ResponseLoggingFilter());
        filters.add(new RequestLoggingFilter());
        filters.add(new AllureRestAssured());

        return new RequestSpecBuilder()
                .setBaseUri(ConfigVariables.getHost())
                .setBasePath(ConfigVariables.getPath())
                .setContentType(ContentType.JSON)
                .addFilters(filters)
                .build();
    }

    private static RequestSpecification prodRequestSpecification(){

        return new RequestSpecBuilder()
                .setBaseUri("https://prod.reqres.in")
                .setBasePath("/api3")
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification defaultResponseSpecification(){

        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }
}
