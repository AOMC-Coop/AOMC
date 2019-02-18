package com.aomc.coop.service;

import com.aomc.coop.model.Message;
import com.aomc.coop.utils.ResponseType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

// You will need to provide a StorageService for the controller to interact with a storage layer (e.g. a file system).
// The interface is like this:
public interface StorageService {

    void init();

    ResponseType upload(MultipartFile file, Message message, final int channel_idx);

    Stream<Path> getAllFilesPaths();

    Path getFilePath(String filename, final int channel_idx);

    Path getFilePathForProfile(String filename, final int channel_idx);

    Resource download(String filename, final int channel_idx);

    void deleteAll();

    ResponseType uploadProfilePicture(MultipartFile file, int channel_idx, int user_idx);

    Resource downloadProfilePicture(String filename, int channel_idx);

}