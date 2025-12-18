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
public class AdminLoginVO implements Serializable {
    /*ID是工号，唯一表示符*/
    private String id;

    private String password;

    /*token是登陆过后能够访问其他接口的凭证要给我*/
    private String token;
}
