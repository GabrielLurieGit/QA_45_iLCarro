package okhttp;

import dto.CarDtoApi;
import dto.ResponseMessageDto;
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

import java.io.IOException;
import java.util.Random;

import static utils.PropertiesReader.getProperty;

public class AddNewCarTestOkHttp implements BaseApi {
    TokenDto tokenDto;
    SoftAssert softAssert = new SoftAssert();
    @BeforeClass
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
                System.out.println(tokenDto);
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
                softAssert.assertEquals(response.code(),200);
                ResponseMessageDto responseMessageDto =
                        GSON.fromJson(response.body().string(), ResponseMessageDto.class);
                softAssert.assertTrue(responseMessageDto.getMessage().equals("Car added successfully"));
                System.out.println(responseMessageDto.toString());
                softAssert.assertAll();
            }else {
                Assert.fail("Car hasn't been created, status code -->"+ response.code());
            }

        }catch (IOException e){
            Assert.fail("created exception");
        }
    }
}
