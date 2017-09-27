package com.ginomai.demo.filemanager;

import com.ginomai.demo.filemanager.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class FileSystemStorageService implements IFileService {

    private final Path rootLocation;
    private final DBFileService dbFileService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, DBFileService dbFileService) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.dbFileService = dbFileService;
    }

    @Override
    public XFile store(MultipartFile file, String name) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            String filename = getNewFilename(file.getOriginalFilename());
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
            XFile xFile = new XFile();
            xFile.setName(name);
            xFile.setContentType(file.getContentType());
            xFile.setFilename(filename);
            xFile.setOriginalFilename(file.getOriginalFilename());
            xFile.setSize(file.getSize());
            xFile.setCreatedBy("");
            xFile.setCreatedOn(new Date());
            return dbFileService.create(xFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    private String getNewFilename(String filename) {
        String[] parts = filename.split("\\.");
        String ext = parts.length >= 2 ? parts[parts.length - 1] : "";
        String time = new java.sql.Timestamp(new Date().getTime()).toString().replaceAll("[^0-9]", "");
        return time + "-" + UUID.uuid(5, 16) + "." + ext;
    }

    @Override
    public List<XFile> loadAll() {
        return dbFileService.getFiles();
    }


    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
