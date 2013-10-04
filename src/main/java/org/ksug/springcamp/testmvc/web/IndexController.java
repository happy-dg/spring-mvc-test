package org.ksug.springcamp.testmvc.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="", method = RequestMethod.GET)
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping
    public String index() {
        LOGGER.debug("Welcome to Spring MVC TEST");
        return "redirect:/user";
    }
}
