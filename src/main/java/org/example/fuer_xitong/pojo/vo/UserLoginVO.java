package org.example.fuer_xitong.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {
    /*ID是工号，唯一表示符*/
    private String id;

    private String password;

    /*登录过后的凭证*/
    private String token;

    private Integer role;

}
