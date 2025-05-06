package com.energo.car_metrics.controllers;

import com.energo.car_metrics.dto.FileInfo;
import com.energo.car_metrics.services.impl.CarsDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/cars")
public class CarsController {

    private static final Logger log = LoggerFactory.getLogger(CarsController.class);

    @Autowired
    private CarsDataInfo carsDataInfo;

    @GetMapping("list-parser")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public ResponseEntity<List<FileInfo>> getListParserCars() {
        return  ResponseEntity.ok(carsDataInfo.getAllListCarsInDir());
    }

    @DeleteMapping("/delete-file/{fileName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteFileCarInfo(@PathVariable String fileName) {
        boolean isDeleted = carsDataInfo.deleteCarDataFile(fileName);

        if (isDeleted) {
            return ResponseEntity.ok("File " + fileName + " was deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Failed to delete file " + fileName + ". File not found.");
        }
    }

    @GetMapping("/download-car-info/{fileName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public ResponseEntity<Resource> downloadFileCar(@PathVariable String fileName) {
        try {
            // Используем сервис для загрузки ресурса
            Resource resource = carsDataInfo.loadFileAsResource(fileName);

            // Возвращаем файл с заголовками для скачивания
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/data-car-date")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public List<Map<String, Object>> getCarDataWithDate(@RequestParam String carBrand,
                                                        @RequestParam String carModel,
                                                        @RequestParam int dateStart,
                                                        @RequestParam int dateMax,
                                                        @RequestParam int countPages) {
        return carsDataInfo.fetchCarData(carBrand, carModel, dateStart, dateMax, countPages);
    }

    @GetMapping("/save-car-data")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Map<String, Object>> saveCarData(
            @RequestParam String carBrand,
            @RequestParam String carModel,
            @RequestParam int dateStart,
            @RequestParam int dateMax,
            @RequestParam int countPages
    ) {
        List<Map<String, Object>> carData = getCarDataWithDate(carBrand, carModel, dateStart, dateMax, countPages);
        return carsDataInfo.saveCars(carData);
    }

    @GetMapping("/get-file/{fileName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public List<Map<String, Object>> getJsonCarInfo(@PathVariable String fileName) {
        try {
            return carsDataInfo.parseJsonFile(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

    @GetMapping("/perform-analysis")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public Map<String, Object> performAnalysis(
            @RequestParam String type,
            @RequestParam String filename) {
        return carsDataInfo.performAnalysis(type, filename);
    }

    @GetMapping("/get-car-image")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public Map<String, Object> getCarImage(@RequestParam String fileName) {
        try {
            return carsDataInfo.getCarImage(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error processing file: " + fileName, e);
        }
    }

}
