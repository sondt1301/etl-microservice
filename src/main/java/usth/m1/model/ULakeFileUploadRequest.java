package usth.m1.model;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;

import java.io.File;

public class ULakeFileUploadRequest {

    @FormParam("fileInfo")
    @PartType(MediaType.APPLICATION_JSON)
    public ULakeFileInfo fileInfo;

    @FormParam("file")
    @PartType("application/octet-stream")
    public File file;
}
