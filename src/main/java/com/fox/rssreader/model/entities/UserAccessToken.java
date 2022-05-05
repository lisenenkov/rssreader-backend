package com.fox.rssreader.model.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.nio.ByteBuffer;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_access_tokens", indexes = {@Index(columnList = "user_id")})
public class UserAccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(length = 64)
    private byte[] secret = new byte[64];

    @Column
    private Date lastUsed = new Date();

    @Column(length = 45)
    private String lastAccessIp;

    @Column(length = 200)
    private String userAgent;

    public String getToken() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES + getSecret().length);
        buffer.putLong(id);
        buffer.put(getSecret());
        return Base64Utils.encodeToString(buffer.array());
    }
}
