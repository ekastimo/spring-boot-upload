package com.ginomai.demo.filemanager;

import com.ginomai.demo.filemanager.models.XFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface IFileService {

    void init();

    XFile store(MultipartFile file, String name);

    List<XFile> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
