package org.zerock.smcal.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginVO {
    private int user_id;
    private String username;
    private String password;
}
