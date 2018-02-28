package com.jarics.postit.controllers;

import com.jarics.postit.Note;
import com.jarics.postit.services.StampService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RestController
@RequestMapping("/notes")
public class StampController {

    @Autowired
    StampService stampService;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    String annotate(@RequestBody Note pNote) throws Exception {

// This works
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pNote.getDirectory() + "original_upload.pdf");
            fos.write(pNote.getBytes());
        } catch (IOException e) {
            throw new Exception("You failed to upload because the file was empty.");
        } finally {
            fos.close();
            return "Done";
        }
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String find(@RequestParam("name") String pName) {
        return "not implemented yet";
    }


}
