package com.qrcodelogin.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author wow
 */
@Data
@ToString
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;
    private Integer count;
    private Integer sale;
    private Integer version;

}
