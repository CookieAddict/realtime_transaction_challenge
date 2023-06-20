package com.transaction.service.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.service.models.responses.PingResponse;
import com.transaction.service.utility.Helper;

@RestController
public class PingController {
    
    @Autowired
    private Helper helper;

    @GetMapping("/ping")
    public PingResponse ping() {
        PingResponse response = new PingResponse(helper.getFormattedSystemTime());

        return response;
    }
}
