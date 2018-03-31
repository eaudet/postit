package com.jarics.postit.controllers;

import com.jarics.postit.Note;
import com.jarics.postit.services.StampService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RestController
@RequestMapping("/notes")
public class StampController {

    @Autowired
    StampService stampService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Note find(@RequestParam("note") String pNote) {
        Note wNote = new Note();
        wNote.setNote("Hello Satan!");
        return wNote;
    }

    //TODO should return the file. The use should be able to redirect to the cloud drive of his choice...
    @CrossOrigin
    @RequestMapping(value = "/annotate", method = RequestMethod.POST)
    @ResponseBody
    public String annotate(@RequestParam(value = "note") String pNote,
                           @RequestParam(value = "directory") String pDirectory,
                           @RequestParam(value = "data", required = false) List<MultipartFile> files) throws Exception {
        stampService.annotateAndStore(pNote, pDirectory, files.get(0));
        return "success";
    }

    @CrossOrigin
    @RequestMapping(value = "/annotate2", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> merge(@RequestParam(value = "note") String pNote,
                                          @RequestParam(value = "data", required = false) List<MultipartFile> files) throws Exception {


        HttpHeaders headers = new HttpHeaders();

        File wUploadedFile = new File( files.get(0).getOriginalFilename() );
        files.get(0).transferTo(wUploadedFile);

        File wAnnotatedFile = stampService.annotate(pNote, wUploadedFile);

//        FileUtils.writeByteArrayToFile(new File("annotated_before_return.pdf"), aFile.getB);

        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData("filename", wAnnotatedFile.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        ByteArrayResource resource = new ByteArrayResource( FileUtils.readFileToByteArray(wAnnotatedFile) );

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

    }


}
