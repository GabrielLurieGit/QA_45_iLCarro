package dto;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class CarDtoApi {
    private String serialNumber; //": "string",
    private String manufacture; //": "string",
    private String model; //": "string",
    private String year; //": "string",
    private String fuel; //": "string",
    //Fuel fuel;
    private int seats; //": 0,
    private String carClass; //": "string",
    private double pricePerDay;//": 0,
    private String about; //: "string",
    private String city; //": "string",
    private double lat; //": 0,
    private double lng; //": 0,
    private String image; //": "string",
    private String owner; //": "string",
    private List<BookedDto> bookedPeriods;


}
