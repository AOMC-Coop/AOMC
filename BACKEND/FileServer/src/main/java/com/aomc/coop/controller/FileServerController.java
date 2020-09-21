package com.aomc.coop.controller;

import java.io.IOException;

import com.aomc.coop.dto.MessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.aomc.coop.service.StorageService;
import com.aomc.coop.storage.StorageFileNotFoundException;

@CrossOrigin
@Controller
@RequestMapping("/api/files")
public class FileServerController {

    private final StorageService storageService;

    public FileServerController(StorageService storageService) {
        this.storageService = storageService;
    }

    @ApiOperation(value = "파일 업로드 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "파일 업로드 성공"),
            @ApiResponse(code = 400, message = "파일 업로드 실패")
    })
    @PostMapping(path = "/upload/{channel_idx}")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file, @RequestParam("message") String stringMessage, @PathVariable final int channel_idx) throws IOException {
        MessageRequest messageRequest = new ObjectMapper().readValue(stringMessage, MessageRequest.class);
        return new ResponseEntity(storageService.upload(file, messageRequest, channel_idx), HttpStatus.OK);
    }

    @GetMapping(path = "/download/{channel_idx}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String filename, @PathVariable final int channel_idx) {
        Resource file = storageService.download(filename, channel_idx);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ApiOperation(value = "프로필 사진 업로드 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 사진 업로드 성공"),
            @ApiResponse(code = 400, message = "프로필 사진 업로드 실패")
    })
    @PostMapping(path = "/upload/profile/{user_idx}")
    public ResponseEntity uploadProfilePicture(@RequestParam("file") MultipartFile file, @PathVariable final int user_idx) {
        return new ResponseEntity(storageService.uploadProfilePicture(file, user_idx), HttpStatus.OK);
    }

    @GetMapping(path = "/download/profile/{filename:.+}")
    public ResponseEntity downloadProfilePicture(@PathVariable String filename) {
        Resource file = storageService.downloadProfilePicture(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }


}
