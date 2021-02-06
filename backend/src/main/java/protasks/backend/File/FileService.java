package protasks.backend.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;


    public void save(File t) {
        this.fileRepository.save(t);
    }
    public void delete(File t) {
        this.fileRepository.delete(t);
    }
    public File findById(long id){return this.fileRepository.findById(id);}
}

