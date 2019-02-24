package com.aomc.coop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChannelInvite implements Serializable {
    private User fromInvite;
    private User toInvite;
    private Channel channel;
}
