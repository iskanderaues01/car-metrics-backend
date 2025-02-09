package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.dto.FileInfo;
import com.energo.car_metrics.models.Car;
import com.energo.car_metrics.repositories.CarRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.client.RestTemplate;

@Service
public class CarsDataInfo {

    @Value("${app.path_dir_save_csv}")
    private String getPathDirSaveFilesCars;

    @Value("${app.format_route_parse_json_date}")
    private String getRouteParseJsonDate;

    @Value("${app.analysis_linear_by_year}")
    private String analysisLinearByYear;

    @Value("${app.analysis_linear_by_mileage}")
    private String analysisLinearByMileage;

    @Value("${app.analysis_linear_by_engine_volume}")
    private String analysisLinearByEngineVolume;

    @Value("${app.analysis_multiple_linear}")
    private String analysisMultipleLinear;

    @Value("${app.analysis_multiple_dummies}")
    private String analysisMultipleDummies;

    @Value("${app.get_image_car}")
    private String imageCarUrlTemplate;

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private CarRepository carRepository;

    public CarsDataInfo(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> getCarImage(String fileName) throws IOException {
        // Путь к JSON файлу
        File jsonFile = new File(getPathDirSaveFilesCars + fileName);

        if (!jsonFile.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        // Читаем массив JSON объектов из файла
        List<Map<String, Object>> carDataList = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});

        // Проверяем, что список не пустой
        if (carDataList.isEmpty()) {
            throw new IOException("No data found in file: " + fileName);
        }

        // Извлекаем данные из первой записи
        Map<String, Object> firstCar = carDataList.get(0);
        String title = ((String) firstCar.get("Title")).toLowerCase().replace(" ", "_");
        String link = (String) firstCar.get("Link");

        // Формируем название изображения с текущей датой
        String photoName = title + "_" + getCurrentDate();

        // Формируем URL запроса
        String requestUrl = String.format(imageCarUrlTemplate, link, photoName);

        // Отправляем GET-запрос на внешний сервис и получаем JSON-ответ
        return restTemplate.getForObject(requestUrl, Map.class);
    }

    // Метод для получения текущей даты в формате yyyyMMdd
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public List<FileInfo> getAllListCarsInDir() {
        List<FileInfo> fileDetails = new ArrayList<>();
        Path directoryPath = Paths.get(getPathDirSaveFilesCars);

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
            System.err.println("Error: Directory not found - " + getPathDirSaveFilesCars);
        } catch (NotDirectoryException e) {
            System.err.println("Error: Path is not a directory - " + getPathDirSaveFilesCars);
        } catch (IOException e) {
            System.err.println("Error: Unable to read directory - " + e.getMessage());
        }

        return fileDetails;
    }

    public boolean deleteCarDataFile(String fileName) {
        Path filePath = Paths.get(getPathDirSaveFilesCars, fileName); // Полный путь к файлу

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
        Path filePath = Paths.get(getPathDirSaveFilesCars).resolve(fileName).normalize();

        // Проверяем существование файла
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new Exception("File not found or is not readable: " + fileName);
        }

        return resource;
    }

    public List<Map<String, Object>> fetchCarData(
            String carBrand,
            String carModel,
            int dateStart,
            int dateMax,
            int countPages
    ) {
        String url = String.format(getRouteParseJsonDate,
                carBrand, carModel, dateStart, dateMax, countPages);
        return restTemplate.getForObject(url, List.class);
    }

    public List<Map<String, Object>> saveCars(List<Map<String, Object>> carData) {

        // Регулярное выражение для чисел (позволяет пробелы внутри числа)
        Pattern mileagePattern = Pattern.compile("^\\d+(\\s\\d+)*$");

        // Список для хранения новых объектов в формате Map
        List<Map<String, Object>> savedCars = new ArrayList<>();

        for (Map<String, Object> car : carData) {
            try {
                String mileageValue = (String) car.get("Mileage");

                // Проверяем, соответствует ли значение шаблону числа
                if (mileageValue != null && mileagePattern.matcher(mileageValue).matches()) {
                    // Убираем пробелы и преобразуем строку в число
                    double mileage = Double.parseDouble(mileageValue.replace(" ", ""));

                    Car carEntity = new Car();
                    carEntity.setTitle((String) car.get("Title"));
                    carEntity.setPrice((String) car.get("Price"));
                    carEntity.setYear(Integer.parseInt((String) car.get("Year")));
                    carEntity.setLink((String) car.get("Link"));
                    carEntity.setConditionBody((String) car.get("ConditionBody"));
                    carEntity.setEngineVolume(Double.parseDouble((String) car.get("EngineVolume")));
                    carEntity.setFuel((String) car.get("Fuel"));
                    carEntity.setTransmission((String) car.get("Transmission"));
                    carEntity.setMileage(mileage);  // Сохраняем числовое значение пробега
                    carEntity.setRawDescription((String) car.get("RawDescription"));

                    // Сохраняем объект в базе данных
                    carRepository.save(carEntity);

                    // Преобразуем сохранённый объект Car обратно в Map
                    Map<String, Object> savedCarMap = new HashMap<>();
                    savedCarMap.put("Title", carEntity.getTitle());
                    savedCarMap.put("Price", carEntity.getPrice());
                    savedCarMap.put("Year", carEntity.getYear());
                    savedCarMap.put("Link", carEntity.getLink());
                    savedCarMap.put("ConditionBody", carEntity.getConditionBody());
                    savedCarMap.put("EngineVolume", carEntity.getEngineVolume());
                    savedCarMap.put("Fuel", carEntity.getFuel());
                    savedCarMap.put("Transmission", carEntity.getTransmission());
                    savedCarMap.put("Mileage", carEntity.getMileage());
                    savedCarMap.put("RawDescription", carEntity.getRawDescription());

                    // Добавляем Map в список сохранённых объектов
                    savedCars.add(savedCarMap);
                } else {
                    System.err.println("Skipping record due to invalid Mileage value: " + mileageValue);
                }
            } catch (NumberFormatException e) {
                // Пропускаем запись, если нельзя преобразовать в число
                System.err.println("Skipping record due to invalid numeric conversion: " + car.get("Mileage"));
            }
        }
        return savedCars;
    }


    public List<Map<String, Object>> parseJsonFile(String fileName) throws IOException {
        // Путь к файлу (можно адаптировать в зависимости от вашего пути)
        File file = new File(getPathDirSaveFilesCars + fileName);

        // Проверка, существует ли файл
        if (!file.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        // Чтение файла и преобразование JSON в List<Map<String, Object>>
        return objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
    }


    public Map<String, Object> performAnalysis(String type, String filename) {
        String url = determineUrl(type, filename);
        if (url == null) {
            throw new IllegalArgumentException("Invalid analysis type: " + type);
        }

        // Отправляем GET-запрос и получаем ответ в формате Map<String, Object>
        return restTemplate.getForObject(url, Map.class);
    }

    private String determineUrl(String type, String filename) {
        switch (type) {
            case "year":
                return String.format(analysisLinearByYear, filename);
            case "mileage":
                return String.format(analysisLinearByMileage, filename);
            case "engine_volume":
                return String.format(analysisLinearByEngineVolume, filename);
            case "multiple_linear":
                return String.format(analysisMultipleLinear, filename);
            case "multiple_dummies":
                return String.format(analysisMultipleDummies, filename);
            default:
                return null;
        }
    }
}
