package org.zerock.smcal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class UpdateDTO {
    private String user_id;
    private String cal_id;
    private String cal_content;
    public String cal_date;
}
