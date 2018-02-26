package com.jarics.postit.controllers;

import com.jarics.postit.Note;
import com.jarics.postit.services.StampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/notes")
public class StampController {

    @Autowired
    StampService stampService;

    //TODO UNIT TEST
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String create(@RequestBody Note dontknowYet) {
        try {
            stampService.manipulatePdf("Hello world", "input pdf", "output pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "not implemented yet";
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String find(@RequestParam("name") String pName) {
        return "not implemented yet";
    }


}
