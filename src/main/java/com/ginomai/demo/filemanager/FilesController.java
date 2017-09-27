package com.ginomai.demo.filemanager;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginomai.demo.filemanager.models.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping(value = "/files")
public class FilesController {

    private final IFileService fileService;
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    public FilesController(IFileService fileService) {
        this.fileService = fileService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/", produces = "application/json")
    @ResponseBody
    public String getAllFiles() throws IOException {
        return mapper.writeValueAsString(fileService.loadAll());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = fileService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/add", produces = "application/json")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws IOException {
        return mapper.writeValueAsString(fileService.store(file, name));
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}