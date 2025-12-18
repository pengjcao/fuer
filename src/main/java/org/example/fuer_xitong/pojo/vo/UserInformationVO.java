package org.example.fuer_xitong.pojo.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserInformationVO implements Serializable {
    /*ID是工号，唯一表示符*/
    private String ID;
    /*ID工号对应的姓名*/
    private String username;
    /*keshi是科室英文标识符*/
    private String keshi;

    /*中文科室名称*/
    private String shanchang;
}

