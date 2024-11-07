package org.zerock.smcal.vo;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CalendarVO {
    private int cal_id;
    private int user_id;
    private String cal_content;
    private Date cal_date;
}
