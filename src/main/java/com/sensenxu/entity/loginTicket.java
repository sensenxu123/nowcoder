package com.sensenxu.entity;

import lombok.Data;

import java.util.Date;
@Data
public class loginTicket {
    private Integer id;
    private Integer userId;
    private String ticket;
    private Integer status;
    private Date expired;


}
