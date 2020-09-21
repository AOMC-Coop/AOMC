package com.aomc.coop.service;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

import com.aomc.coop.mapper.FileMapper;
import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.dto.MessageRequest;
import com.aomc.coop.dto.UserDto;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.storage.StorageException;
import com.aomc.coop.storage.StorageFileNotFoundException;
import com.aomc.coop.storage.StorageProperties;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import com.aomc.coop.utils.rabbitMQ.RabbitMQUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private final Path rootLocation;
    private final FileMapper fileMapper;
    private final RabbitMQUtil rabbitMQUtil;
    private final RabbitTemplate rabbitTemplate;
    private final UserMapper userMapper;

    public FileSystemStorageService(StorageProperties properties, FileMapper fileMapper, RabbitMQUtil rabbitMQUtil, RabbitTemplate rabbitTemplate, UserMapper userMapper) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.fileMapper = fileMapper;
        this.rabbitMQUtil = rabbitMQUtil;
        this.rabbitTemplate = rabbitTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseType upload(MultipartFile file, MessageRequest messageRequest, final int channel_idx) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String location = "E:\\FileStorage\\" + channel_idx;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );
        String time = sdf.format( new Date( timestamp.getTime( ) ) );

        try {
            if (file.isEmpty()) {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_File_Upload.getStatus());
            }
            if (filename.contains("..")) {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_File_Upload.getStatus());
            }

            Path path = Paths.get(location);

            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    //fail to create directory
                    e.printStackTrace();
                }
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            String url = "http://localhost:8085/api/files/download/" + channel_idx + "/" + filename;

            messageRequest.setFile_url(url);
            messageRequest.setFile_name(filename);
            rabbitMQUtil.sendRabbitMQ(messageRequest);

            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_File_Upload.getStatus());
        }
        catch (IOException e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_File_Upload.getStatus());
        }
    }

    @Override
    public Resource download(String filename, final int channel_idx) {
        try {
            Path file = getFilePath(filename, channel_idx);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public ResponseType uploadProfilePicture(MultipartFile file, @PathVariable final int user_idx) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String location = "E:\\FileStorage\\"+ "profile";
        try {
            if (file.isEmpty()) {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Profile_Picture_Upload.getStatus());
            }
            if (filename.contains("..")) {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Profile_Picture_Upload.getStatus());
            }

            Path path = Paths.get(location);

            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }


            // MultipartFile -> File
            File convFile = new File(file.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            String locationToBeChanged = location + "\\";
            String ext = FilenameUtils.getExtension(filename);

            // original filename -> user_idx filename
            File oldFile = new File(locationToBeChanged + filename);
            File newFile = new File(locationToBeChanged + user_idx + "." + ext);

            boolean success = oldFile.renameTo(newFile);

            String url = "http://localhost:8085/api/files/download/profile/" + filename;

            // JPA로 수정해야 하는 부분
            UserDto user = new UserDto();
            user.setImage(url);
            userMapper.updateUserImage(user_idx, url);

            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Profile_Picture_Upload.getStatus(), url);
        }
        catch (IOException e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Profile_Picture_Upload.getStatus());
        }
    }


    @Override
    public Resource downloadProfilePicture(String filename) {
        try {
            Path file = getFilePathForProfile(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }


    @Override
    public Path getFilePath(String filename, final int channel_idx) {
        String location = channel_idx + "/" +filename;
        return rootLocation.resolve(location);
    }


    @Override
    public Path getFilePathForProfile(String filename) {
        String location = "profile/" +filename;
        return rootLocation.resolve(location);
    }


    @Override
    public Stream<Path> getAllFilesPaths() {
        try {
            return Files.walk(this.rootLocation, 2)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }
}
