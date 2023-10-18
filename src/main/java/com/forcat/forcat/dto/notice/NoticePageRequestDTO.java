package com.forcat.forcat.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Positive;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticePageRequestDTO {

    @Builder.Default
    private int noticePage = 1;

    @Builder.Default
    private int noticeSize = 10;

    private String type; // 검색의 종류 t,c, w, tc,tw, twc

    private String keyword;

    //검색 조건 및 페이징 조건 문자열 구성
    private String noticeLink;

    public String[] getTypes(){

        if(type == null || type.isEmpty()){

            return null;
        }

        return type.split("");
    }

    //페이징 처리 기능 반환
    public Pageable getNoticePageable(String...props) {

        return PageRequest.of(this.noticePage -1, this.noticeSize, Sort.by(props).descending());
    }


    public String getNoticeLink() {

        if(noticeLink == null){//link가 null인 경우에 실행

            StringBuilder builder = new StringBuilder();//StringBuilder 객체 생성.page와 size 값을 URL 문자열에 추가
            builder.append("page=" + this.noticePage);
            builder.append("&size=" + this.noticeSize);

            if(type != null && type.length() > 0){//type 값이 있으면 URL 문자열에 추가

                builder.append("&type=" + type);

            }

            if(keyword != null){//keyword 값이 있으면 URL 문자열에 추가

                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));//특수문자 인코딩

                } catch (Exception e) {//인코딩 에러 처리

                }

            }

            noticeLink = builder.toString(); // 완성된 URL 문자열 저장
        }

        return noticeLink; // URL 문자열을 반환
    }


}
