package com.aomc.coop.storage;
import com.aomc.coop.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */

// ***** 여기서 upload directory를 관리하자. -> 상대 경로로 지정할 것
//    private String location = "C:\\FileStorage";
//    private String location = "E:\\FileStorage";

// ***** 아래 설명을 꼭 참조하고 짤 것!!!
//    / : 루트
//    ./ : 현재 위치
//   ../ : 현재 위치의 상단 폴더
//
//    ex) index.php가 C:\index\a에 위치한다면,
//
//    여기서 / 는 C:
//          ./ 는 a
//          ../ 는 index라는 것.
//
//        - 3가지를 간단히 정리하자면,
//            1  '/'    -> 가장 최상의 디렉토리로 이동한다.(Web root)
//            2  './'   -> 파일이 현재 디렉토리를 의미한다.
//            3  '../'  -> 상위 디렉토리로 이동한다.
//
//        - 만약 두단계 상위 디렉토리로 이동하려면
//            '../../' 이렇게 사용하면 된다.

    private String location = "/Users/iyunjae/FileStorage";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    Path path = Paths.get(location);
    

}
