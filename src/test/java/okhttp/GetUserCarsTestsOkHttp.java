package okhttp;

import dto.CarsDto;
import dto.ErrorMessageDtoString;
import dto.TokenDto;
import dto.UserDtoLombok;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;
import utils.PropertiesReader;

import static utils.PropertiesReader.getProperty;

import java.io.IOException;

import static utils.PropertiesReader.getProperty;

public class GetUserCarsTestsOkHttp implements BaseApi {
    TokenDto tokenDto;
    SoftAssert softAssert = new SoftAssert();
    @BeforeClass
    public void login(){   //authentication in @before class for access token
        UserDtoLombok user = UserDtoLombok.builder()
                .username(getProperty("login.properties", "email"))
                .password(getProperty("login.properties", "password"))
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+LOGIN)
                .post(requestBody)
                .build();
        try(Response response =
                    OK_HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()){
                tokenDto = GSON.fromJson(response.body().string(), TokenDto.class);
                // System.out.println(tokenDto);
            }else {
                System.out.println("Something went wrong");
            }

        }catch (IOException e){
            System.out.println("login wrong, created exception");
        }

    }

    @Test
    public void GetUserCarsPositiveTest(){
        Request request = new Request.Builder()
                .url(BASE_URL+GET_USER_CARS)
                .addHeader(AUTHORIZATION,tokenDto.getAccessToken())
                .get()
                .build();
        try(Response response =
                    OK_HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()){
                CarsDto carsDto = GSON.fromJson(response.body().string(), CarsDto.class);
                System.out.println(carsDto.toString());
                softAssert.assertEquals(response.code(),200);
                softAssert.assertAll();

            }else {
               ErrorMessageDtoString errorMessageDtoString =
                       GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request, status code --> " + response.code());
            }

        }catch (IOException e){
            Assert.fail("created exception");
        }
    }

    @Test
    public void GetUserCarsNegativeTest_InvalidToken(){
        Request request = new Request.Builder()
                .url(BASE_URL+GET_USER_CARS)
                .addHeader(AUTHORIZATION,"Invalid token") // Invalid token value
                .get()
                .build();
        try(Response response =
                    OK_HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()){
               ErrorMessageDtoString errorMessageDtoString =
                       GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                softAssert.assertEquals(errorMessageDtoString.getStatus(),401);
                softAssert.assertTrue(errorMessageDtoString.getError().equals("Unauthorized"));
                softAssert.assertTrue(errorMessageDtoString.getMessage().toString().contains("must contain exactly"));
                softAssert.assertAll();
            }else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request, status code --> " + response.code());
            }

        }catch (IOException e){
            Assert.fail("created exception");
        }
    }

}
