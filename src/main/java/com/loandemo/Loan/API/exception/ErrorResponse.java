package com.loandemo.Loan.API.exception;

import java.time.LocalDateTime;

/**
 * Standard error response structure for all API exceptions.
 *
 * <p>This class ensures consistency in error responses across the application.
 *
 * @since 1.0
 */
public class ErrorResponse {

    private String message;
    private String status;
    private LocalDateTime timestamp;

    public ErrorResponse(String message, String status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
