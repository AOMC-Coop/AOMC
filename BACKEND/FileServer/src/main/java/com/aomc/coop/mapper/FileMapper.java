package com.aomc.coop.mapper;

import com.aomc.coop.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileMapper {

    // insert 성공시 1을 return, 실패시 0을 return.
    @Insert("INSERT INTO file(url, name) VALUES(#{url},#{name})")
    int insertFile(FileInfo file);

//
//        private int idx;
//        private String url;
//        private String type;
//        private String name;

    // send_date는 db에서 messages table에서 join 연산을 통해 읽어오면 된다.
}
