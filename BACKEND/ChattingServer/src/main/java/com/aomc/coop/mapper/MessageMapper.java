package com.aomc.coop.mapper;

import com.aomc.coop.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageMapper {

    //messages 테이블
    @Insert("INSERT INTO messages(content, channel_idx, user_idx) VALUES(#{message.content}, #{channelIdx}, #{userIdx})")
    int createMessage(final Message message, int channelIdx, int userIdx);

}
