package com.kh.vira_dev.ecommerceapi.repository;

import com.kh.vira_dev.ecommerceapi.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByOrderBySortOrderAsc();

    List<Banner> findAllByActiveOrderBySortOrderAsc(boolean active);
}
