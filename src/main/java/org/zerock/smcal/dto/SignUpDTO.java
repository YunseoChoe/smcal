package org.zerock.smcal.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class SignUpDTO {
    private String username;
    private String password;
}
