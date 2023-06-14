package lk.ijse.garment.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Incomedetails {
    private String id;
    private String month;
    private Double empaymenttotal;
    private Double cuspaymentotal;
    private Double proflose;
}
