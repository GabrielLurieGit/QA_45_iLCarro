package okhttp;

import dto.ErrorMessageDtoString;
import dto.TokenDto;
import dto.UserDtoLombok;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;

import java.io.IOException;
import java.util.Random;

public class RegistrationTestsOkHttp implements BaseApi {
    SoftAssert softAssert = new SoftAssert();
    @Test
    public void registrationPositiveTest(){
        int i  = new Random().nextInt(1000)+1000;
        UserDtoLombok user = UserDtoLombok.builder()
                .username(i+"bob_mail@mail.com")
                .password("Pass123!")
                .firstName("Bob")
                .lastName("Doe")
                .build();
        System.out.println(user);
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION)
                .post(requestBody)
                .build();
        Response response;
        try {
           response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.isSuccessful());
        System.out.println(response.toString());
        try {
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(),200);

    }



    @Test
    public void registrationNegativeTest_InvalidEmail(){
        UserDtoLombok user = UserDtoLombok.builder()
                .username("bob_mail@")
                .password("Pass123!")
                .firstName("Bob")
                .lastName("Doe")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION)
                .post(requestBody)
                .build();
        Response response;

        try {
           response = OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.toString());
        Assert.assertEquals(response.code(),400);
    }


    @Test
    public void registrationNegativeTest_InvalidContentType(){
        UserDtoLombok user = UserDtoLombok.builder()
                .username("bob_mail@mail.com")
                .password("Pass123!")
                .firstName("Bob")
                .lastName("Doe")
                .build();
        RequestBody requestBody =
                RequestBody.create(GSON.toJson(user), MediaType.parse("text/plain")); // instead of JSON - text/plain
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION)
                .post(requestBody)
                .build();

        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            if (!response.isSuccessful()){
               ErrorMessageDtoString errorMessageDtoString =
                       GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                softAssert.assertEquals(errorMessageDtoString.getStatus(),500);
                softAssert.assertTrue(errorMessageDtoString.getError().equals("Internal Server Error"));
                softAssert.assertTrue(errorMessageDtoString.getMessage().toString().contains("Content type"));
                softAssert.assertAll();
            }else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request -->" + response.code());
            }

        } catch (IOException e) {
            Assert.fail("Created exception!");
            throw new RuntimeException(e);
        }
    }

    @Test   //403 HOW????
    public void registrationNegativeTest_WrongURL(){
        UserDtoLombok user = UserDtoLombok.builder()
                .username("bob_mail@mail.com")
                .password("Pass123!")
                .firstName("Bob")
                .lastName("Doe")
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+"/invalid_endpoint") // url is broken
                .post(requestBody)
                .build();

        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            if(!response.isSuccessful()){
                System.out.println(response.toString());
               softAssert.assertEquals(response.code(),403);
               softAssert.assertAll();
            }else {
                softAssert.fail("wrong request, status code --> " + response.code());
            }

        } catch (IOException e) {
            softAssert.fail("created exception");
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registrationPositiveTest_ValidateToken(){
        int i  = new Random().nextInt(1000)+1000;
        UserDtoLombok user = UserDtoLombok.builder()
                .username(i+"bob_mail@mail.com")
                .password("Pass123!")
                .firstName("Bob")
                .lastName("Doe")
                .build();
        System.out.println(user);
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION)
                .post(requestBody)
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            if(response.isSuccessful()){
             TokenDto tokenDto = GSON.fromJson(response.body().string(), TokenDto.class);
                System.out.println(tokenDto.toString());
                Assert.assertFalse(tokenDto.getAccessToken().isBlank());
            }else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request -->"+response.code());
            }

        } catch (IOException e) {
            Assert.fail("Created exception");
            throw new RuntimeException(e);
        }

    }

    @Test
    public void registrationNegativeTest_InvalidPassword(){
        int i  = new Random().nextInt(1000)+1000;
        UserDtoLombok user = UserDtoLombok.builder()
                .username(i+"bob_mail@mail.com")
                .password("Pass123qw")
                .firstName("Bob")
                .lastName("Doe")
                .build();
        System.out.println(user);
        RequestBody requestBody = RequestBody.create(GSON.toJson(user),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+REGISTRATION)
                .post(requestBody)
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            if(!response.isSuccessful()){
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                softAssert.assertEquals(response.code(),400);
                softAssert.assertTrue(errorMessageDtoString.getError().equals("Bad Request"));
                softAssert.assertTrue(errorMessageDtoString.getMessage().toString().contains("At least 8 characters"));

                softAssert.assertAll();
            }else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request -->"+response.code());
            }
        } catch (IOException e) {
            Assert.fail("Created exception");
            throw new RuntimeException(e);
        }
    }






}
