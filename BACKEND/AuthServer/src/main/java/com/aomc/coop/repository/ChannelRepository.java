package com.aomc.coop.repository;

import com.aomc.coop.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    
}
