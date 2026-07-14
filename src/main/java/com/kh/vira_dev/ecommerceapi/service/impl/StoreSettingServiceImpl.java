package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.StoreSettingRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.StoreSettingResponse;
import com.kh.vira_dev.ecommerceapi.entity.StoreSetting;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.mapper.StoreSettingMapper;
import com.kh.vira_dev.ecommerceapi.repository.StoreSettingRepository;
import com.kh.vira_dev.ecommerceapi.service.StoreSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreSettingServiceImpl implements StoreSettingService {

    private final StoreSettingRepository storeSettingRepository;
    private final StoreSettingMapper storeSettingMapper;


    @Override
    public StoreSettingResponse create(StoreSettingRequest request) {
        StoreSetting setting = storeSettingMapper.toEntity(request);
        StoreSetting savedStore = storeSettingRepository.save(setting);
        return storeSettingMapper.toResponse(savedStore);
    }

    @Override
    public StoreSettingResponse update(Long id, StoreSettingRequest request) {
        StoreSetting setting = findByOrThrow(id);
        storeSettingMapper.applyStoreSettingFields(setting, request);
        StoreSetting savedStore = storeSettingRepository.save(setting);
        return storeSettingMapper.toResponse(savedStore);
    }

    @Override
    public void delete(Long id) {
        StoreSetting storeSetting = findByOrThrow(id);
        log.info("Delete StoreSetting with id={}", id);
        storeSettingRepository.delete(storeSetting);
    }

    @Override
    public List<StoreSettingResponse> getAll() {
        List<StoreSetting> storeSettings = storeSettingRepository.findAll();
        return storeSettings
                .stream()
                .map(storeSettingMapper::toResponse)
                .toList();
    }


    @Override
    public StoreSettingResponse getById(Long id) {
        StoreSetting setting = findByOrThrow(id);
        return storeSettingMapper.toResponse(setting);
    }

    private StoreSetting findByOrThrow(Long id) {
        return storeSettingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StoreSetting"));
    }
}
