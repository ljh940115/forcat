package com.forcat.forcat.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {//페이징 관련, 검색 종류, 키워드 추가
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    private String type; // 검색의 종류 t,c, w, tc,tw, twc
    private String keyword;

    //검색 조건 문자열을 배열로 반환
    public String[] getTypes(){//검색 조건 종류가 비어있으면 null 반환, 있으면 한글자씩 분리하여 배열 처리 반환
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }

    //페이징 처리 기능 반환
    public Pageable getPageable(String...props) {
        // 현재 페이지 번호(this.page)를 기반으로 1을 뺀 값을 사용하여 PageRequest를 생성합니다.
        // 여기서 this.page는 현재 페이지를 나타내며, this.size는 페이지 크기를 나타냅니다.
        // 즉, 현재 페이지 번호 - 1을 한 결과가 실제로 사용할 페이지 번호가 됩니다.
        // props 배열에 지정된 속성을 기준으로 내림차순 정렬을 수행합니다.
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    //검색 조건 및 페이징 조건 문자열 구성
    private String link;

    public String getLink() {
        if(link == null){//link가 null인 경우에 실행
            StringBuilder builder = new StringBuilder();//StringBuilder 객체 생성.page와 size 값을 URL 문자열에 추가
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if(type != null && type.length() > 0){//type 값이 있으면 URL 문자열에 추가
                builder.append("&type=" + type);
            }

            if(keyword != null){//keyword 값이 있으면 URL 문자열에 추가
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));//특수문자 인코딩
                } catch (UnsupportedEncodingException e) {//인코딩 에러 처리
                }
            }
            link = builder.toString();//완성된 URL 문자열 저장
        }
        return link;//URL 문자열을 반환
    }
}
