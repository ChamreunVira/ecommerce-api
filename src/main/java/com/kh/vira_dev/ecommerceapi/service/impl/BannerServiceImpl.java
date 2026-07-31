package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.BannerRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.BannerResponse;
import com.kh.vira_dev.ecommerceapi.entity.Banner;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.BannerMapper;
import com.kh.vira_dev.ecommerceapi.repository.BannerRepository;
import com.kh.vira_dev.ecommerceapi.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    public List<BannerResponse> getAll() {
        return bannerMapper.toResponseList(bannerRepository.findAllByOrderBySortOrderAsc());
    }

    @Override
    public List<BannerResponse> getAllActive() {
        return bannerMapper.toResponseList(bannerRepository.findAllByActiveOrderBySortOrderAsc(true));
    }

    @Override
    public BannerResponse getById(Long id) {
        return bannerMapper.toResponse(findOrThrow(id));
    }

    @Override
    public BannerResponse create(BannerRequest request) {
        Banner banner = bannerMapper.toEntity(request);
        Banner saved = bannerRepository.save(banner);
        log.info("Created banner: {}", saved.getId());
        return bannerMapper.toResponse(saved);
    }

    @Override
    public BannerResponse update(Long id, BannerRequest request) {
        Banner banner = findOrThrow(id);
        bannerMapper.applyFields(banner, request);
        Banner saved = bannerRepository.save(banner);
        log.info("Updated banner: {}", saved.getId());
        return bannerMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Banner banner = findOrThrow(id);
        log.info("Deleted banner: {}", banner.getId());
        bannerRepository.delete(banner);
    }

    @Override
    public BannerResponse toggleActive(Long id) {
        Banner banner = findOrThrow(id);
        banner.setActive(!banner.getActive());
        bannerRepository.save(banner);
        return bannerMapper.toResponse(banner);
    }

    private Banner findOrThrow(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner"));
    }
}
