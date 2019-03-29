package com.example.demo.model;

import com.example.demo.service.BaseService;

import java.io.Serializable;

public class CachedUser implements Serializable {
    private final int id;
    private int serviceId;

    public CachedUser(int id, int serviceId) {
        this.id = id;
        this.serviceId = serviceId;
    }

    public int getId() {
        return id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
