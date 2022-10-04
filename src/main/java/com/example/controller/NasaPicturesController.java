package com.example.controller;


import com.example.exception.PictureNotFoundException;
import com.example.service.NasaPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/mars/pictures")
@RequiredArgsConstructor
public class NasaPicturesController {

    private final NasaPictureService nasaPictureService;

    @GetMapping(value = "/{sol}/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getLargestPicture(@PathVariable Integer sol, @RequestParam Optional<String> camera) {
        return nasaPictureService.findLargestPicture(sol, camera)
                .orElseThrow(() -> new PictureNotFoundException(sol, camera));
    }
}
