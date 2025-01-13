package okhttp;

import dto.*;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;
import utils.PropertiesReader;

import static utils.PropertiesReader.getProperty;

import java.io.IOException;
import java.util.Random;


public class GetUserCarsTestsOkHttp implements BaseApi {
    TokenDto tokenDto;
    CarDtoApi carDtoApi;
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

    @BeforeMethod
    public void AddNewCar(){
        int i = new Random().nextInt(10000);
        carDtoApi = CarDtoApi.builder()
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
                ResponseMessageDto responseMessageDto =
                        GSON.fromJson(response.body().string(), ResponseMessageDto.class);
            }else {
                Assert.fail("Car hasn't been created, status code -->"+ response.code());
            }

        }catch (IOException e){
            Assert.fail("created exception");
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
                softAssert.assertEquals(response.code(),200,"status code assert");
                boolean checkSerialNumber = false;
                for (CarDtoApi car : carsDto.getCars()){
                    if (car.getSerialNumber().equals(carDtoApi.getSerialNumber())){
                        // System.out.println(carDtoApi.getSerialNumber());
                        checkSerialNumber = true;
                        break;
                    }
                }
                softAssert.assertTrue(checkSerialNumber,"serial number assert");
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

    @Test // Status Code 401
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
                softAssert.assertEquals(errorMessageDtoString.getStatus(),401,"status code assert");
                softAssert.assertTrue(errorMessageDtoString.getError().equals("Unauthorized"),"error name assert");
                softAssert.assertTrue(errorMessageDtoString.getMessage().toString().contains("must contain exactly"),"message text assert");
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
