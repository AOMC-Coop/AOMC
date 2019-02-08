package com.aomc.coop.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.aomc.coop.model.File;

@Mapper
@Repository
public interface FileMapper {

    // send_date는 db에서 messages table에서 join 연산을 통해 읽어오면 된다.
}
