package org.zerock.smcal.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class RegisterDTO {
    private String user_id;
    private String cal_id;
    private String cal_content;
    private Date cal_date;
}
