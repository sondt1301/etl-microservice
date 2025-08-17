package usth.m1.model;

public record ULakeFolderResponse(
        int code,
        String msg,
        ULakeFolderInfo resp
) {}
