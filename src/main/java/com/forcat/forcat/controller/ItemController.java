package com.forcat.forcat.controller;

import com.forcat.forcat.dto.shop.ItemFormDto;
import com.forcat.forcat.dto.shop.ItemSearchDto;
import com.forcat.forcat.entity.Item;
import com.forcat.forcat.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    @PreAuthorize("hasRole('ADMIN')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping(value = "/admin/item/new")// http://localhost/admin/item/new
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList) {
        // @Valid : RequestBody로 들어오는 객체 검증 -> ItemFormDto의 @NotBlank, @NotNull을 검증해 줌
        // BindingResult : 검증 오류가 발생 시 오류를 보관하고 있는 역할
        if(bindingResult.hasErrors()) { // 검증 오류 발생 시
            return "item/itemForm"; // 상품 등록 페이지로 리턴
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) { // 첫 번째 상품 이미지가 비어있다면
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm"; // 에러 메세지와 함께 상품 등록 페이지로 리턴
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList); // 상품 등록
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm"; // 에러 메세지와 함께 상품 등록 페이지로 리턴
        }
        return "redirect:/"; // 완료되면 메인페이지로 리다이렉트 시킴
    }

    @PreAuthorize("hasRole('ADMIN')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    // 배열을 통해 http://localhost/admin/items와 http://localhost/admin/페이지번호 모두 반응
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        // @PathVariable : URL경로에서 변수 값 추출하여 사용 가능
        // Optional -> NPE 방지 wrapper 클래스
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        // PageRequest.of(pageNumber, pageSize) -> 가져올 페이지 번호, 한 페이지 당 가져올 항목의 수
        // isPresent : Optional 객체에 값이 있다면 true 반환, 없다면 false 반환
        // URL 경로에 페이지 번호가 있다면? 해당 페이지 조회, 아니라면 0페이지 (첫 번째 페이지) 조회
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items); // 상품 데이터와 페이징 정보 뷰로 전달
        model.addAttribute("itemSearchDto", itemSearchDto);
        // 페이지 전환 시 기존 검색 조건 유지할 수 있도록 뷰에 다시 전달
        model.addAttribute("maxPage", 5); // 최대 페이지 번호 개수
        return "item/itemMng";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

}
