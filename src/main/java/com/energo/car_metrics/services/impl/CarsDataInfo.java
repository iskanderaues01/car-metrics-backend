package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.dto.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class CarsDataInfo {

    @Value("${app.path_dir_save_csv}")
    private String getPathDirSaveCSV;

    public List<FileInfo> getAllListCarsInDir() {
        List<FileInfo> fileDetails = new ArrayList<>();
        Path directoryPath = Paths.get(getPathDirSaveCSV);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            for (Path file : directoryStream) {
                if (Files.isRegularFile(file)) { // Проверяем, что это файл
                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

                    FileInfo fileInfo = new FileInfo(
                            file.getFileName().toString(),         // Название файла
                            attrs.creationTime().toString(),      // Дата создания
                            Files.size(file)                      // Размер файла
                    );

                    fileDetails.add(fileInfo);
                }
            }
        } catch (NoSuchFileException e) {
            System.err.println("Error: Directory not found - " + getPathDirSaveCSV);
        } catch (NotDirectoryException e) {
            System.err.println("Error: Path is not a directory - " + getPathDirSaveCSV);
        } catch (IOException e) {
            System.err.println("Error: Unable to read directory - " + e.getMessage());
        }

        return fileDetails;
    }

    public boolean deleteCarDataFile(String fileName) {
        Path filePath = Paths.get(getPathDirSaveCSV, fileName); // Полный путь к файлу

        try {
            if (Files.exists(filePath)) { // Проверка, существует ли файл
                Files.delete(filePath); // Удаление файла
                System.out.println("File deleted successfully: " + filePath);
                return true; // Возвращаем true, если файл успешно удалён
            } else {
                System.err.println("File not found: " + filePath);
                return false; // Файл не найден
            }
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false; // Ошибка при удалении
        }
    }

    public Resource loadFileAsResource(String fileName) throws Exception {
        Path filePath = Paths.get(getPathDirSaveCSV).resolve(fileName).normalize();

        // Проверяем существование файла
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new Exception("File not found or is not readable: " + fileName);
        }

        return resource;
    }
}
