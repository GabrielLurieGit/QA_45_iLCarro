package data_providers;

import dto.CarDto;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static utils.PropertiesReader.*;

public class CarDP {

    @DataProvider
    public Iterator<CarDto> dataProviderCarFile() {
        List<CarDto> carList = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/data_car.csv"));
            //   Haifa;s-123-1;man-1;model-1;2023;//option[@value='Diesel'];4;C;222.2;about1;
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] splitArray = line.split(";"); // [Haifa][s-123-1][man-1][model-1][2023] ...
                CarDto car = CarDto.builder()
                        .city(splitArray[0])   //[Haifa]
                        .serialNumber(splitArray[1]) //[s-123-1]
                        .manufacture(splitArray[2])  //[man-1]
                        .model(splitArray[3])  //[model-1]
                        .year(splitArray[4])
                        .fuel(splitArray[5])
                        .seats(Integer.parseInt(splitArray[6]))
                        .carClass(splitArray[7])
                        .pricePerDay(Double.parseDouble(splitArray[8]))
                        .about(splitArray[9])
                        .build();
                carList.add(car);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return carList.listIterator();
    }

    @DataProvider
    public Iterator<CarDto> dataProviderCarFileProperties() {
        List<CarDto> carList = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(
                    new FileReader("src/main/resources/"+getProperty("login.properties","fileNameDPCar")));
            //   Haifa;s-123-1;man-1;model-1;2023;//option[@value='Diesel'];4;C;222.2;about1;
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] splitArray = line.split(";"); // [Haifa][s-123-1][man-1][model-1][2023] ...
                CarDto car = CarDto.builder()
                        .city(splitArray[0])   //[Haifa]
                        .serialNumber(splitArray[1]) //[s-123-1]
                        .manufacture(splitArray[2])  //[man-1]
                        .model(splitArray[3])  //[model-1]
                        .year(splitArray[4])
                        .fuel(splitArray[5])
                        .seats(Integer.parseInt(splitArray[6]))
                        .carClass(splitArray[7])
                        .pricePerDay(Double.parseDouble(splitArray[8]))
                        .about(splitArray[9])
                        .build();
                carList.add(car);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return carList.listIterator();
    }
}
