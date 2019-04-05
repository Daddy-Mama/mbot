package com.example.demo.interfaces;

public interface ICacheService {
     void addToCache(int cacheId, int userId);
     Integer getServiceByUserIdInCache(int userId);
     void removeFromCache(int userId);
}
