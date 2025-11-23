package com.example.assurity.hooks;

import com.example.assurity.util.ApiHandler;
import io.cucumber.java.Before;

public class Hooks {

    public static ApiHandler helper;

    @Before(order = 0)
    public void load_config() {
         helper = new ApiHandler();
        helper.loadConfig();
        System.out.println("Hook @Before executed, Api handler initialized");
    }
}
