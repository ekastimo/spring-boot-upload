package com.ginomai.demo.filemanager;

import com.ginomai.demo.filemanager.models.XFile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * My Template
 * Created by Timothy on 27-Sep-17.
 */
@Component
public class DBFileService {

    private final ArrayList<XFile> files = new ArrayList<>();

    public DBFileService() {
    }

    XFile create(XFile xFile) {
        files.add(xFile);
        xFile.setId(files.size());
        return xFile;
    }

    public ArrayList<XFile> getFiles() {
        return files;
    }

}
