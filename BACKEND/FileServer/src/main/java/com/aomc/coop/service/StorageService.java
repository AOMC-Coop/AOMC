package com.aomc.coop.service;

import com.aomc.coop.dto.MessageRequest;
import com.aomc.coop.utils.ResponseType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    ResponseType upload(MultipartFile file, MessageRequest messageRequest, final int channel_idx);

    Stream<Path> getAllFilesPaths();

    Path getFilePath(String filename, final int channel_idx);

    Path getFilePathForProfile(String filename);

    Resource download(String filename, final int channel_idx);

    void deleteAll();

    ResponseType uploadProfilePicture(MultipartFile file, int user_idx);

    Resource downloadProfilePicture(String filename);

}
