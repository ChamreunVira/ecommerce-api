package com.kh.vira_dev.ecommerceapi.mapper;

import com.kh.vira_dev.ecommerceapi.dto.request.BannerRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.BannerResponse;
import com.kh.vira_dev.ecommerceapi.entity.Banner;
import com.kh.vira_dev.ecommerceapi.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BannerMapper {

    private final FileUploadUtils fileUploadUtils;

    public Banner toEntity(BannerRequest request) {
        Banner banner = new Banner();
        applyFields(banner, request);
        return banner;
    }

    public BannerResponse toResponse(Banner banner) {
        return BannerResponse.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .subtitle(banner.getSubtitle())
                .imageUrl(banner.getImageUrl())
                .buttonLabel(banner.getButtonLabel())
                .buttonLink(banner.getButtonLink())
                .active(banner.getActive())
                .sortOrder(banner.getSortOrder())
                .createdAt(banner.getCreatedAt())
                .updatedAt(banner.getUpdatedAt())
                .build();
    }

    public List<BannerResponse> toResponseList(List<Banner> banners) {
        return banners.stream().map(this::toResponse).toList();
    }

    public void applyFields(Banner banner, BannerRequest request) {
        banner.setTitle(request.getTitle());
        banner.setSubtitle(request.getSubtitle());
        banner.setImageUrl(toBannerImage(request.getImage()));
        banner.setButtonLabel(request.getButtonLabel());
        banner.setButtonLink(request.getButtonLink());
        if (request.getActive() != null) banner.setActive(request.getActive());
        if (request.getSortOrder() != null) banner.setSortOrder(request.getSortOrder());
    }

    private String toBannerImage(MultipartFile file) {
       if(file.isEmpty()) return null;
       return fileUploadUtils.upload(file);
    }
}
