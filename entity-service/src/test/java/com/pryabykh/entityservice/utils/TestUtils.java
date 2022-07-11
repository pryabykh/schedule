package com.pryabykh.entityservice.utils;

import com.pryabykh.entityservice.userContext.UserContext;

public class TestUtils {
    public static UserContext shapeUserContext() {
        UserContext userContext = new UserContext();
        userContext.setUserId(5L);
        userContext.setUserEmail("test@test.ru");
        return userContext;
    }
}
