package com.aomc.coop.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.mapper.FileMapper;
import com.aomc.coop.model.File;
import com.aomc.coop.model.Message;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.storage.StorageException;
import com.aomc.coop.storage.StorageFileNotFoundException;
import com.aomc.coop.storage.StorageProperties;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.DateFormatCustom;
import com.aomc.coop.utils.ResponseType;
import com.aomc.coop.utils.rabbitMQ.RabbitMQUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


// ***** 방 단위로 나눠서 스케일 아웃 -> MAX_DIRECTORY_SIZE 변수를 설정하고, 이 용량을 넘으면 자동으로 상위 디렉토리를 파도록 하자
// ***** 지금은 E:\FileStorage\{channel_idx} 의 구조이지만,
// ***** 추후엔 E:\FileStorage\{new_hdd}\{channel_idx} 의 구조로 변경할 것
// ***** new_hdd = new hard drive의 줄임말, 의미는 아래 참조

//       하드웨어 추가를 통한 스케일 아웃 -> 디렉토리 구조를 나눠서 만들어보자.
//       실제/ 물리적                    -> 가상/ 논리적
//       ex) 한 상위 디렉토리 파일 스토리지의 최대 용량을 32GB로 설정하고, 이 용량을 초과하면 새로운 디렉토리 생성(스케일 아웃)
//       파일 매니저(매니징) 서버와 파일 서버(+스토리지)는 나눠서 짤 것 : 그럼 저장 공간이 무한대가 됨
//       파일 URL과 디렉토리 루트를 redis에 저장


@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private RabbitMQUtil rabbitMQUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

// Message 객체를 보낼 것
    @Override
    public ResponseType upload(MultipartFile file, Message message, final int channel_idx) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
// ***** filename 중복시, time 변수 혹은 다른 방식을 통해 filename 중복을 막도록 코드를 변경할 것

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );
        String time = sdf.format( new Date( timestamp.getTime( ) ) );

        try {
            if (file.isEmpty()) {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_File_Upload.getStatus());
            }
            if (filename.contains("..")) {
                // This is a security check
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_File_Upload.getStatus());
// "Cannot store file with relative path outside current directory"
            }

            String location = "E:\\FileStorage\\" + channel_idx;
            Path path = Paths.get(location);

            // 디렉토리 생성
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

            String url = "http://localhost:8085/files/" + channel_idx + "/" + filename;

            File fileToBeInserted = new File();

            fileToBeInserted.setName(filename);
            fileToBeInserted.setUrl(url);
            // ***** RabbitMQ에 실어주는 것도 여기서 해야하나?


            // ***** Mapper로 db에 file 정보를 저장할 것 -> 잘 되나 테스트 해보자
            if(fileMapper.insertFile(fileToBeInserted) == 0){
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_File_Upload.getStatus());
            }

            //message info setting
            message.setFile_url(url);
            rabbitMQUtil.sendRabbitMQ(message);

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

    // Path : object that may be used to locate a file in a file system.
    //        It will typically represent a system dependent file path.
    @Override
    public Path getFilePath(String filename, final int channel_idx) {
        String location = channel_idx + "\\" +filename;
        return rootLocation.resolve(location);
    }

    @Override
    public Stream<Path> getAllFilesPaths() {
        try {
// * Files.walk
// Return a Stream that is lazily populated with Path
// by walking the file tree rooted at a given starting file.
// The file tree is traversed depth-first,
// the elements in the stream are Path objects
// that are obtained as if by resolving the relative path against start.

// The maxDepth parameter is the maximum number of levels of directories to visit.
// A value of 0 means that only the starting file is visited, unless denied by the security manager.

// ***** channel_idx 디렉토리를 한 단계 더 생성하므로, maxDepth : 1 -> 2
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


}
