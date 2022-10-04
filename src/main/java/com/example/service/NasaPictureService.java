package com.example.service;

import com.example.dto.NasaResponse;
import com.example.dto.Photo;
import com.example.exception.NasaServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
public class NasaPictureService {

    private final RestTemplate restTemplate;
    @Value("${nasa.api.url}")
    private String nasaBaseUrl;
    @Value("${nasa.api.key}")
    private String nasaApiKey;

    @Cacheable("largestPicture")
    public Optional<byte[]> findLargestPicture(Integer sol, Optional<String> camera) {
        var roverPhotos = getCuriosityRoverPhotos(sol, camera);
        return roverPhotos
                .parallelStream()
                .map(this::enrichPhotoWithSize)
                .max(comparing(Photo::getSize))
                .map(photo -> getPictureContent(photo.getPhotoUrl()));
    }

    private byte[] getPictureContent(String pictureUri) {
        return restTemplate.getForObject(pictureUri, byte[].class);
    }

    private Photo enrichPhotoWithSize(Photo photo) {
        var headers = restTemplate.headForHeaders(photo.getPhotoUrl());
        return photo.setSize(headers.getContentLength());
    }

    private List<Photo> getCuriosityRoverPhotos(Integer sol, Optional<String> camera) {
        try {
            String nasaUrl = buildNasaUrl(sol, camera);
            var response = restTemplate.getForObject(nasaUrl, NasaResponse.class);

            return Optional.ofNullable(response)
                    .map(NasaResponse::photos)
                    .orElseGet(ArrayList::new);
        } catch (RestClientException e) {
            throw new NasaServiceException("Call to NASA API failed with " + e.getMessage());
        }
    }

    private String buildNasaUrl(Integer sol, Optional<String> camera) {
        return UriComponentsBuilder.fromHttpUrl(nasaBaseUrl)
                .path("mars-photos/api/v1/rovers/curiosity/photos")
                .queryParam("api_key", nasaApiKey)
                .queryParam("sol", sol)
                .queryParamIfPresent("camera", camera)
                .toUriString();
    }
}
