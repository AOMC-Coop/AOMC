package com.aomc.coop.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */

// ***** 여기서 upload directory를 관리하자. -> 상대 경로로 지정할 것
    private String location = "E:\\FileStorage";
//    private String location = "E:\\FileStorage";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
