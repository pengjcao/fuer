package org.example.fuer_xitong.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class UserInformationDTO implements Serializable {

    @JsonProperty("ID")
    private String ID;

    private String username;
    /*keshi是科室名称的英文表示*/
    private String keshi;

    /*这个ID工号擅长的领域*/
    private String shanchang;
}
