package com.aomc.coop.model;

import lombok.Data;

import java.util.List;

@Data
public class ChannelInvite {
    private User fromInvite;
    private List<User> toInvite;
    private Channel channel;
}
