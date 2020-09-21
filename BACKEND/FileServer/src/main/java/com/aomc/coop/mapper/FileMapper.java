package com.aomc.coop.mapper;

import com.aomc.coop.dto.FileInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileMapper {

    @Insert("INSERT INTO file(url, name) VALUES(#{url},#{name})")
    int insertFile(FileInfo file);

}
