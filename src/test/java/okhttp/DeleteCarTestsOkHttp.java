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

import java.io.IOException;

import static utils.PropertiesReader.getProperty;

public class DeleteCarTestsOkHttp implements BaseApi {
    TokenDto tokenDto;
    CarDtoApi carDtoApi;
    CarsDto carsDto;
    SoftAssert softAssert = new SoftAssert();

    @BeforeClass
    public void login() { //authentication in @before class for access token
        UserDtoLombok user = UserDtoLombok.builder()
                .username(getProperty("login.properties", "email"))
                .password(getProperty("login.properties", "password"))
                .build();
        RequestBody requestBody = RequestBody.create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN)
                .post(requestBody)
                .build();
        try (Response response =
                     OK_HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                tokenDto = GSON.fromJson(response.body().string(), TokenDto.class);
                // System.out.println(tokenDto);
            } else {
                System.out.println("Something went wrong");
            }

        } catch (IOException e) {
            System.out.println("login wrong, created exception");
        }

    }

    @BeforeMethod
    public void GetUserCars() {
        Request request = new Request.Builder()
                .url(BASE_URL + GET_USER_CARS)
                .addHeader(AUTHORIZATION, tokenDto.getAccessToken())
                .get()
                .build();
        try (Response response =
                     OK_HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                carsDto = GSON.fromJson(response.body().string(), CarsDto.class);
                if (carsDto.getCars() != null && carsDto.getCars().length != 0) { // checking that an array is not null or empty
                    carDtoApi = carsDto.getCars()[0]; // getting index 0 from the array
                    System.out.println(carDtoApi.getSerialNumber());
                } else {
                    System.out.println("there's no cars to delete");
                }

            } else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request, status code --> " + response.code());
            }

        } catch (IOException e) {
            Assert.fail("created exception");
        }
    }

    @Test
    public void deleteCarPositiveTest() {
        Request request = new Request.Builder()
                .url(BASE_URL + DELETE_CAR_BY_ID + carDtoApi.getSerialNumber())
                .addHeader(AUTHORIZATION, tokenDto.getAccessToken())
                .delete()
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseMessageDto responseMessageDto =
                        GSON.fromJson(response.body().string(), ResponseMessageDto.class);
                softAssert.assertEquals(response.code(), 200);
                softAssert.assertTrue(responseMessageDto.getMessage().equals("Car deleted successfully"));
                // after deleting a car, using boolean method that calls GET request again
                System.out.println(responseMessageDto.getMessage() + " "+ carDtoApi.getSerialNumber());
                softAssert.assertFalse(isCarPresents(carsDto));
                softAssert.assertAll();
            } else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request, status code --> " + response.code());
            }

        } catch (IOException e) {
            Assert.fail("created exception");
        }

    }

    private boolean isCarPresents(CarsDto carsDto) {
        Request request = new Request.Builder()
                .url(BASE_URL + GET_USER_CARS)
                .addHeader(AUTHORIZATION, tokenDto.getAccessToken())
                .get()
                .build();
        try (Response response =
                     OK_HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                carsDto = GSON.fromJson(response.body().string(), CarsDto.class);
                if (carsDto.getCars() != null && carsDto.getCars().length != 0) {
                    for (CarDtoApi car : carsDto.getCars()) { // passing the array with for each, comparing serial numbers from the array with the serial number of the deleted car
                        if (car.getSerialNumber().equals(carDtoApi.getSerialNumber())){
                            return true; //when for each passed the array and didn't find deleted serial number, the method returns false.
                        }
                    }
                } else {
                    System.out.println("there's no cars to delete");
                }

            } else {
                ErrorMessageDtoString errorMessageDtoString =
                        GSON.fromJson(response.body().string(), ErrorMessageDtoString.class);
                System.out.println(errorMessageDtoString.toString());
                Assert.fail("Invalid request, status code --> " + response.code());
            }
        } catch (IOException e) {
            Assert.fail("created exception");
        }
        return false;
    }



}
