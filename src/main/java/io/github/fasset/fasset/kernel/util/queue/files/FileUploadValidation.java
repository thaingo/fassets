package io.github.fasset.fasset.kernel.util.queue.files;

import io.github.fasset.fasset.model.files.FileUpload;

public class FileUploadValidation implements FileValidationService<FileUpload> {

    private final FileUploadService fileUploadService;

    FileUploadValidation(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Override
    public FileUpload validate(FileUpload fileUpload) throws InvalidFileException {

        if(fileUpload == null){
            throw new InvalidFileException("Sorry, the file provided for upload into the system is null");
        }

        if( fileUploadService.theFileIsAlreadyUploaded(fileUpload)) {
            throw new UploadedFileException(fileUpload);
        } else if (fileUpload.isDeserialized()) {
            throw new DeserializedFileException(fileUpload);
        }

        return fileUpload;
    }
}