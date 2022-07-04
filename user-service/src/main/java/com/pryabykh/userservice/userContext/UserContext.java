package com.pryabykh.userservice.userContext;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserContext {
    private Long userId = null;
    private String userEmail = "";
}
