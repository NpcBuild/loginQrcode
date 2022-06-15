package com.kafka.domain;

import lombok.Data;

/**
 * @author wow
 */
@Data
public class KafkaMessage {
    private Integer id ;
    private String name ;
    private String message;

    public KafkaMessage() {
    }

    public KafkaMessage(Integer id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    @Override
    public String toString() {
        return "KafkaMessage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                "}";
    }
}
