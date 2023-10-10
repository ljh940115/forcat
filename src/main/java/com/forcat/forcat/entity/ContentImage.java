package com.forcat.forcat.entity;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "content")
public class ContentImage implements Comparable<ContentImage> {

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Content content;

    @Override
    public int compareTo(ContentImage other) {
        return this.ord - other.ord;
    }

    public void changeContent(Content content){
        this.content = content;
    }

} // ContentImage
