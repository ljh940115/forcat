package com.forcat.forcat.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

public class CompositeKey {

    //복합키 사용
    @Embeddable
    public class MemberId implements Serializable {
        private String member_id;
        private String email;
    }
}
