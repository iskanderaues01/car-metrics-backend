package com.energo.car_metrics.dto;

public class FileInfo {
    private String fileName;
    private String creationDate;
    private long fileSize;

    public FileInfo(String fileName, String creationDate, long fileSize) {
        this.fileName = fileName;
        this.creationDate = creationDate;
        this.fileSize = fileSize;
    }

    // Геттеры и сеттеры
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
