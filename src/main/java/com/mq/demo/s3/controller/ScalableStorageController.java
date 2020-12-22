package com.mq.demo.s3.controller;

import com.mq.demo.s3.service.ScalableStorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class ScalableStorageController {

    private final ScalableStorageService scalableStorageService;

    public ScalableStorageController(ScalableStorageService scalableStorageService) {
        this.scalableStorageService = scalableStorageService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> upload (@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
        scalableStorageService.upload(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/{as}")
    public ResponseEntity<Object> download(@PathVariable("id") String id, @PathVariable("as") String as) {
        InputStream dataInputStream = scalableStorageService.download(id);

        InputStreamResource resource = new InputStreamResource(dataInputStream);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"" + as + "\"")
                .body(resource);
    }

}
