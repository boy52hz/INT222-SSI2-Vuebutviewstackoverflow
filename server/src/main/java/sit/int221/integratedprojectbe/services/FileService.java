package sit.int221.integratedprojectbe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.entities.File;
import sit.int221.integratedprojectbe.repositories.FileRepository;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public File store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File newFile = new File();
        newFile.setName(fileName);
        newFile.setData(file.getBytes());
        newFile.setType(file.getContentType());

        return fileRepository.save(newFile);
    }

    public File getFile(String id) {
        return fileRepository.findById(id).get();
    }

    public Stream<File> getAllFiles() {
        return fileRepository.findAll().stream();
    }
}
