package com.sanish.url.services;

import com.sanish.url.dtos.ClicksInfoDto;
import com.sanish.url.dtos.UrlManagementDto;
import com.sanish.url.entities.ClicksInfo;
import com.sanish.url.entities.UrlMapping;
import com.sanish.url.entities.User;
import com.sanish.url.repositories.ClicksInfoRepository;
import com.sanish.url.repositories.UrlMappingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UrlManagementService {

    private final UrlMappingRepository urlMappingRepository;
    private final ClicksInfoRepository clicksInfoRepository;

    @Autowired
    public UrlManagementService(UrlMappingRepository urlMappingRepository, ClicksInfoRepository clicksInfoRepository) {
        this.urlMappingRepository = urlMappingRepository;
        this.clicksInfoRepository = clicksInfoRepository;
    }

    public UrlManagementDto createShortUrl(String originalUrl, User user) {
        String shortUrl = generateNewShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setShortUrl(shortUrl);

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return mapToUrlMappingDto(savedUrlMapping);
    }

    private UrlManagementDto mapToUrlMappingDto(UrlMapping urlMapping){
        UrlManagementDto urlManagementDto = new UrlManagementDto();
        urlManagementDto.setId(urlMapping.getId());
        urlManagementDto.setOriginalUrl(urlMapping.getOriginalUrl());
        urlManagementDto.setShortUrl(urlMapping.getShortUrl());
        urlManagementDto.setUsername(urlMapping.getUser().getUsername());
        urlManagementDto.setClickCount(urlMapping.getClickCount());
        urlManagementDto.setCreatedAt(urlMapping.getCreatedAt());

        return urlManagementDto;
    }

    private String generateNewShortUrl() {
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(7);

        String characterSet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for(int i=0; i<shortUrl.capacity(); i++){
            int randomNumber = random.nextInt(characterSet.length());
            shortUrl.append(characterSet.charAt(randomNumber));
        }

        return shortUrl.toString();
    }

    public List<UrlManagementDto> getAllUserUrls(User user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::mapToUrlMappingDto).toList();
    }

    public LocalDateTime parseDateTime(String dateTimeString){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.parse(dateTimeString,dateTimeFormatter);
    }

    /* public List<ClicksInfoDto> getClicksInfoByDate(String shortUrl, LocalDateTime from, LocalDateTime to) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        if(urlMapping != null){
            clicksInfoRepository.findByUrlMappingAndClickStampBetween(urlMapping, from, to)
                    .stream().collect(Collectors.groupingBy(urlClick ->
                            urlClick.getClickStamp().toLocalDate(), Collectors.counting())).entrySet().stream()
                    .map(entry -> {
                        ClicksInfoDto clicksInfoDto = new ClicksInfoDto();
                        clicksInfoDto.setClickDate(entry.getKey());
                        clicksInfoDto.setClickCount(entry.getValue());
                        return clicksInfoDto;
                    }).toList();
        }

        return null;
    } */

    public List<ClicksInfoDto> getClicksInfoByDate(String shortUrl, LocalDateTime from, LocalDateTime to) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        if (urlMapping == null) {
            return Collections.emptyList();
        }

        return clicksInfoRepository.findByUrlMappingAndClickStampBetween(urlMapping, from, to)
                .stream()
                .collect(Collectors.groupingBy(
                        urlClick -> urlClick.getClickStamp().toLocalDate(),
                        Collectors.counting()
                )).entrySet()
                .stream()
                .map(entry -> new ClicksInfoDto(entry.getKey(),entry.getValue()))
                .toList();
    }

    public Map<LocalDate, Long> getUsersTotalClicksByDate(User user, LocalDate from, LocalDate to) {
        List<UrlMapping> urlMappingsList = urlMappingRepository.findByUser(user);
        List<ClicksInfo> clicksInfoList = clicksInfoRepository
                .findByUrlMappingInAndClickStampBetween(urlMappingsList, from.atStartOfDay(), to.plusDays(1).atStartOfDay());

        return clicksInfoList.stream()
                .collect(Collectors.groupingBy(click -> click.getClickStamp().toLocalDate(),
                        Collectors.counting()));
    }

    public UrlMapping getCorrespondingOriginalUrl(String shortUrl) {
        UrlMapping fetchedShortUrl = urlMappingRepository.findByShortUrl(shortUrl);

        //Record the click/visit of short url and update its click stats
        if(fetchedShortUrl != null){
            fetchedShortUrl.setClickCount(fetchedShortUrl.getClickCount() + 1); //Update click count
            urlMappingRepository.save(fetchedShortUrl);

            //Update ClicksInfo table data
            ClicksInfo clicksInfo = new ClicksInfo();
            clicksInfo.setClickStamp(LocalDateTime.now());
            clicksInfo.setUrlMapping(fetchedShortUrl);
            clicksInfoRepository.save(clicksInfo);
        }

        return fetchedShortUrl;
    }

    @Transactional
    public String deleteShortUrlAndItsData(String shortUrl){
        UrlMapping fetchedEntry = urlMappingRepository.findByShortUrl(shortUrl); //Search Short URL in UrlMapping table
        if(fetchedEntry != null) {
            clicksInfoRepository.deleteAllByUrlMappingInBatch(fetchedEntry); //Delete all info of corr. url in cIR
            urlMappingRepository.deleteById(fetchedEntry.getId()); //Delete the url data
        }

        return "Short Url deleted successfully!";
    }
}
