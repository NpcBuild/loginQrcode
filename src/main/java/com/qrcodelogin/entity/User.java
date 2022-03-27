package com.qrcodelogin.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
public class User implements Serializable{

        private static final long serialVersionUID = -6056125703075132981L;

        private Integer id;

        private String account;

        private String userPwd;

        private String userName;


}
