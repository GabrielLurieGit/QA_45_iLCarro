package okhttp;

import dto.*;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;

import java.io.IOException;
import java.util.Random;

import static utils.PropertiesReader.getProperty;

public class AddNewCarTestsOkHttp implements BaseApi {
    TokenDto tokenDto;
    SoftAssert softAssert = new SoftAssert();
    @BeforeClass(alwaysRun = true)
    public void login(){
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
    public void AddNewCarPositiveTest(){
        int i = new Random().nextInt(10000);
        CarDtoApi carDtoApi = CarDtoApi.builder()
                .serialNumber("number"+i)
                .manufacture("Ford")
                .model("Focus")
                .year("2020")
                .fuel("Electric")
                .seats(5)
                .carClass("A")
                .pricePerDay(345.5)
                .about("about my car")
                .city("Haifa")
                .build();

        RequestBody requestBody = RequestBody.create(GSON.toJson(carDtoApi),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+ADD_NEW_CAR)
                .addHeader(AUTHORIZATION,tokenDto.getAccessToken())
                .post(requestBody)
                .build();

        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            System.out.println(response.isSuccessful() + " code " + response.code());
            if (response.isSuccessful()){
                softAssert.assertEquals(response.code(),200,"status code assert");
                ResponseMessageDto responseMessageDto =
                        GSON.fromJson(response.body().string(), ResponseMessageDto.class);
                softAssert.assertTrue(responseMessageDto.getMessage().equals("Car added successfully"),"response message assert");
                System.out.println(responseMessageDto.toString());
                softAssert.assertAll();
            }else {
                Assert.fail("Car hasn't been created, status code -->"+ response.code());
            }

        }catch (IOException e){
            Assert.fail("created exception");
        }
    }

    @Test // Status Code 400
    public void AddNewCarNegativeTest_EmptySerialNumber(){
        CarDtoApi carDtoApi = CarDtoApi.builder() // object doesn't contain serial number
                .manufacture("Ford")
                .model("Focus")
                .year("2020")
                .fuel("Electric")
                .seats(5)
                .carClass("A")
                .pricePerDay(345.5)
                .about("about my car")
                .city("Haifa")
                .build();

        RequestBody requestBody = RequestBody.create(GSON.toJson(carDtoApi),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+ADD_NEW_CAR)
                .addHeader(AUTHORIZATION,tokenDto.getAccessToken())
                .post(requestBody)
                .build();

        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
          //  System.out.println(response.isSuccessful() + " code " + response.code());
            if (!response.isSuccessful()){
               ErrorMessageDtoString errorMessageDtoString =
                       GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                softAssert.assertEquals(errorMessageDtoString.getStatus(),400,"status code assert");
                softAssert.assertTrue(errorMessageDtoString.getError().equals("Bad Request"),"error name assert");
                softAssert.assertTrue(errorMessageDtoString.getMessage().toString().contains("must not be blank"),"message text assert");
                softAssert.assertAll();
            }else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request --> "+ response.code());
            }

        }catch (IOException e){
            Assert.fail("created exception");
        }
    }

    @Test // Status Code 400
    public void AddNewCarNegativeTest_SerialNumberAlreadyExists(){
        CarDtoApi carDtoApi = CarDtoApi.builder()
                .serialNumber("12340923") //serial number already exists in added cars
                .manufacture("Ford")
                .model("Focus")
                .year("2020")
                .fuel("Electric")
                .seats(5)
                .carClass("A")
                .pricePerDay(345.5)
                .about("about my car")
                .city("Haifa")
                .build();

        RequestBody requestBody = RequestBody.create(GSON.toJson(carDtoApi),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+ADD_NEW_CAR)
                .addHeader(AUTHORIZATION,tokenDto.getAccessToken())
                .post(requestBody)
                .build();

        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            //  System.out.println(response.isSuccessful() + " code " + response.code());
            if (!response.isSuccessful()){
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                softAssert.assertEquals(errorMessageDtoString.getStatus(),400,"status code assert");
                softAssert.assertTrue(errorMessageDtoString.getError().equals("Bad Request"),"error name assert");
                softAssert.assertTrue(errorMessageDtoString.getMessage().toString().contains("already exists"),"message text assert");
                softAssert.assertAll();
            }else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request --> "+ response.code());
            }

        }catch (IOException e){
            Assert.fail("created exception");
        }
    }

    @Test // Status Code 401
    public void AddNewCarNegativeTest_InvalidToken(){
        CarDtoApi carDtoApi = CarDtoApi.builder()
                .serialNumber("12340923")
                .manufacture("Ford")
                .model("Focus")
                .year("2020")
                .fuel("Electric")
                .seats(5)
                .carClass("A")
                .pricePerDay(345.5)
                .about("about my car")
                .city("Haifa")
                .build();

        RequestBody requestBody = RequestBody.create(GSON.toJson(carDtoApi),JSON);
        Request request = new Request.Builder()
                .url(BASE_URL+ADD_NEW_CAR)
                .addHeader(AUTHORIZATION,"Invalid token") // invalid token value
                .post(requestBody)
                .build();

        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            //  System.out.println(response.isSuccessful() + " code " + response.code());
            if (!response.isSuccessful()){
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                softAssert.assertEquals(errorMessageDtoString.getStatus(),401,"status code assert");
                softAssert.assertTrue(errorMessageDtoString.getError().equals("Unauthorized"),"error name assert");
                softAssert.assertTrue(errorMessageDtoString.getMessage().toString().contains("must contain exactly"),"message text assert");
                softAssert.assertAll();
            }else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request --> "+ response.code());
            }

        }catch (IOException e){
            Assert.fail("created exception");
        }
    }


    @Test // Status Code 500
    public void AddNewCarNegativeTest_InvalidContentType(){
        CarDtoApi carDtoApi = CarDtoApi.builder()
                .serialNumber("12340923")
                .manufacture("Ford")
                .model("Focus")
                .year("2020")
                .fuel("Electric")
                .seats(5)
                .carClass("A")
                .pricePerDay(345.5)
                .about("about my car")
                .city("Haifa")
                .build();

        RequestBody requestBody = RequestBody.create(GSON.toJson(carDtoApi), MediaType.parse("text/plain"));
        Request request = new Request.Builder()
                .url(BASE_URL+ADD_NEW_CAR)
                .addHeader(AUTHORIZATION,tokenDto.getAccessToken())
                .post(requestBody)
                .build();

        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()){
            //  System.out.println(response.isSuccessful() + " code " + response.code());
            if (!response.isSuccessful()){
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                softAssert.assertEquals(errorMessageDtoString.getStatus(),500,"status code assert");
                softAssert.assertTrue(errorMessageDtoString.getError().equals("Internal Server Error"),"error name assert");
                softAssert.assertTrue(errorMessageDtoString.getMessage().toString().contains("Content type"),"message text assert");
                softAssert.assertAll();
            }else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request --> "+ response.code());
            }
        }catch (IOException e){
            Assert.fail("created exception");
        }
    }


}
