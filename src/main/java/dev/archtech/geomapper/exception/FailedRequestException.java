package dev.archtech.geomapper.exception;

public class FailedRequestException extends RuntimeException{
    int statusCode;

    public FailedRequestException(int statusCode, int rowNumber) {
        super(String.format("Request Failed with Code %s at row: %s", statusCode, rowNumber));
        this.statusCode = statusCode;
    }

    public FailedRequestException(String message) {
        super(message);
    }
}
