package com.jarics.postit.controllers;

import com.jarics.postit.Note;
import com.jarics.postit.services.StampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@RestController
@RequestMapping("/notes")
public class StampController {

    @Autowired
    StampService stampService;
    private Note pName;

//    @CrossOrigin
//    @RequestMapping(value = "/upload_", method = RequestMethod.POST)
//    public @ResponseBody
//    String annotate(@RequestBody Note pNote) throws Exception {
//        return stampService.annotateAndStore(pNote);
//    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Note find(@RequestParam("note") String pNote) {
        Note wNote = new Note();
        wNote.setNote("Hello Satan!");
        return wNote;
    }

    @CrossOrigin
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String saveAuto(
            @RequestPart(value = "note") Note pNote,
            @RequestParam(value = "some-random") String random,
            @RequestParam(value = "data", required = false) List<MultipartFile> files) throws Exception {
        System.out.println(random);
        System.out.println(pNote.getNote());
        for (MultipartFile file : files) {
            System.out.println(file.getOriginalFilename());
        }
        stampService.annotateAndStore(pNote, files.get(0));
        return "success";
    }


}
