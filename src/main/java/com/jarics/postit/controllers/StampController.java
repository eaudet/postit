package com.jarics.postit.controllers;

import com.jarics.postit.Note;
import com.jarics.postit.services.StampService;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/notes")
public class StampController {

    @Autowired
    StampService stampService;
    private Note pName;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String annotate(@RequestBody Note pNote) throws Exception {
        return stampService.annotateAndStore(pNote);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Note find(@RequestParam("note") String pNote) {
        Note wNote = new Note();
        wNote.setNote("Hello Satan!");
        return wNote;
    }


}
