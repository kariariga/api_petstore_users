package Tests;

import Utils.Util;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class User {

    String path = "src/test/resources/user.json";
    JSONObject jsonObj = new JSONObject(Util.readJson(path));

    @DataProvider(name="user")
    public Object[][] getUserData() {
        return new Object[][]{
                {jsonObj.getJSONObject("user01")},
                {jsonObj.getJSONObject("user02")},
                {jsonObj.getJSONObject("user03")}
        };
    }

    @Test(priority = 1, dataProvider = "user")
    public void createUser(JSONObject jsonUser) {
        String result = Integer.toString(jsonUser.getInt("id"));

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonUser.toString())
                .when()
                .post("https://petstore.swagger.io/v2/user")
                .then()
                .log().all()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", equalTo(result))
        ;
    }

    @Test(priority = 2, dataProvider = "user")
    public void searchUser(JSONObject jsonUser) {
        given()
                .contentType("application/json")
                .log().all()
                .body(jsonUser.toString())
                .when()
                .get("https://petstore.swagger.io/v2/user/" + jsonUser.getString("username"))
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(jsonUser.getInt("id")))
                .body("username", equalTo(jsonUser.getString("username")))
                .body("email", equalTo(jsonUser.getString("email")))
                .body("password", equalTo(jsonUser.getString("password")))
                .body("phone", equalTo(jsonUser.getString("phone")))
                .body("userStatus", equalTo(jsonUser.getInt("userStatus")))
        ;
    }

    @Test(priority = 3, dataProvider = "user")
    public void deleteUser(JSONObject jsonUser) {
        given()
                .contentType("application/json")
                .log().all()
                .body(jsonUser.toString())
                .when()
                .delete("https://petstore.swagger.io/v2/user/" + jsonUser.getString("username"))
                .then()
                .log().all()
                .statusCode(200)
                .body("message", equalTo(jsonUser.getString("username")))
                .body("type", equalTo("unknown"))
                .body("code", equalTo(200))
        ;
    }
}
