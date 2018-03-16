package com.jarics.postit.controllers;

import com.jarics.postit.Note;
import com.jarics.postit.services.StampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notes")
public class StampController {

    @Autowired
    StampService stampService;
    private Note pName;

    @CrossOrigin
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String annotate(@RequestBody Note pNote) throws Exception {
        return stampService.annotateAndStore(pNote);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Note find(@RequestParam("note") String pNote) {
        Note wNote = new Note();
        wNote.setNote("Hello Satan!");
        return wNote;
    }


}
