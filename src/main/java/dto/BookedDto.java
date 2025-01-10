package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class BookedDto {
    private String email;    //": "string",
    private String startDate; //: "2023-11-19",
    private String endDate;   //: "2023-11-19"
}
