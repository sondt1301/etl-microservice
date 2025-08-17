package usth.m1.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ULakeFileInfo(
        String cid,
        long creationTime,
        Long id,
        String mime,
        String name,
        Long ownerId,
        ULakeFolderInfo parent,
        long size
) {}
