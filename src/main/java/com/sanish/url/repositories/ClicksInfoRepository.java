package com.sanish.url.repositories;

import com.sanish.url.entities.ClicksInfo;
import com.sanish.url.entities.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClicksInfoRepository extends JpaRepository<ClicksInfo, Long> {
    List<ClicksInfo> findByUrlMappingAndClickStampBetween(UrlMapping mapping,
                                                          LocalDateTime fromDate,
                                                          LocalDateTime toDate);

    List<ClicksInfo> findByUrlMappingInAndClickStampBetween(List<UrlMapping> mappingList,
                                                          LocalDateTime fromDate,
                                                          LocalDateTime toDate);

}
