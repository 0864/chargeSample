package code.sample.premcharge.charge.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "job_item_msg",
        indexes = {
                @Index(name="idx_msg_itemid", columnList = "itemId"),
                @Index(name="idx_msg_type", columnList = "type")
        })
public class JobItemMsg {
    @Id
    private Long id;

    private Long itemId;
    @Enumerated(EnumType.STRING)
    private MessageType type; // WARNING/ERROR/INFO
    private String code;
    @Column(length = 4000)
    private String message;
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
