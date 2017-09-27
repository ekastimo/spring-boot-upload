package com.ginomai.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * My Template
 * Created by Timothy on 27-Sep-17.
 */
@Controller
public class IndexController {
    @GetMapping(value = "/")
    public String index() {
        return "index";
    }
}
