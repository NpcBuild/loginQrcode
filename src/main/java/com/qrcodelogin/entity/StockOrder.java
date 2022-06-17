package com.qrcodelogin.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class StockOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer sid;
    private String name;
    private Date create_time;
}
