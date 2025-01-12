package okhttp;

import dto.UserDtoLombok;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseApi;
import utils.PropertiesReader;

import java.io.IOException;

public class LoginTestsOkHttp implements BaseApi {

    @Test
    public void loginPositiveTest(){
        UserDtoLombok user = UserDtoLombok.builder()
                .username("1897bob_mail@mail.com")
                .password("Pass123!")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.toString());
        Assert.assertEquals(response.code(),200);
    }


    @Test
    public void loginNegativeTest_InvalidEmail(){
        UserDtoLombok user = UserDtoLombok.builder()
                .username("1897bob_mail")
                .password("Pass123!")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.toString());
        Assert.assertEquals(response.code(),401);
    }

    @Test
    public void loginNegativeTest1(){
        UserDtoLombok user = UserDtoLombok.builder()
                .username("1897bob_mail@mail.com")
                .password("Pass123!")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), MediaType.parse("text/plain"));
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.toString());
       // Assert.assertEquals(response.code(),500);
    }

    @Test
    public void loginNegativeTest_WrongURL(){
        UserDtoLombok user = UserDtoLombok.builder()
                .username("1897bob_mail@mail.com")
                .password("Pass123!")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+"/invalid_endpoint")
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.toString());
        Assert.assertEquals(response.code(),403);
    }
}
