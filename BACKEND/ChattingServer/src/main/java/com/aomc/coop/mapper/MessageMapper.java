package com.aomc.coop.mapper;

import com.aomc.coop.model.File;
import com.aomc.coop.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageMapper {

    //messages 테이블
    @Insert("INSERT INTO messages(message_idx, content, channel_idx, user_idx) VALUES(#{message.message_idx}, #{message.content}, #{channelIdx}, #{userIdx})")
    int createMessage(final Message message, final int channelIdx, final int userIdx);

    //file 테이블
    @Insert("INSERT INTO file(content) VALUES(#{file.content})")
    @Options(useGeneratedKeys = true, keyProperty = "idx")
    int createFile(File file);

    //message with file_url 테이블
    @Insert("INSERT INTO messages(message_idx, content, channel_idx, user_idx, file_idx) VALUES(#{message.message_idx}, #{message.content}, #{channelIdx}, #{userIdx}, #{fileIdx})")
    void createMessageWithFile(Message message, int fileIdx);

}
