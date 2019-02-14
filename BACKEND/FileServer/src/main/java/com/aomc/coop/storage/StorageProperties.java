package com.aomc.coop.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */

// ***** 여기서 upload directory를 관리하자.
// ***** 해당 채널로 생성된 디렉토리가 없다면 생성하고, 있다면 사용하는 코드 작성
    private String location = "E:\\FileStorage";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
