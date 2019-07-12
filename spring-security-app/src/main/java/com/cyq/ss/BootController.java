package com.cyq.ss;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BootController {

    @RequestMapping("/boot")
    public String boot(HttpServletRequest request){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return "Hello Boot" + request.getSession().getId();
    }
}
