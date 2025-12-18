package org.example.fuer_xitong.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {

    /*ID是工号，唯一表示符*/
    private String ID;

    private String password;

    private Integer role;

}
