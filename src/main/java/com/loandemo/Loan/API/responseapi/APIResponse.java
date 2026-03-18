package com.loandemo.Loan.API.responseapi;

import lombok.Data;

/**
 * Generic API response wrapper used to standardize all API responses.
 *
 * <p>This class provides a consistent structure for both success and error responses
 * across the application. It wraps the response data along with status code
 * and message.</p>
 *
 * <p>Structure of API Response:</p>
 * <pre>
 * {
 *   "code": 200,
 *   "message": "Success",
 *   "data": { ... }
 * }
 * </pre>
 *
 * <p>Main Features:</p>
 * <ul>
 *     <li>Supports generic response type using {@code <T>}</li>
 *     <li>Provides static factory methods for success and error responses</li>
 *     <li>Encapsulates response code, message, and data</li>
 * </ul>
 *
 * <p>Usage Examples:</p>
 *
 * <pre>
 * // Success with data
 * return APIResponse.success(user, "User fetched successfully");
 *
 * // Success without data
 * return APIResponse.success("Operation completed");
 *
 * // Error response
 * return APIResponse.error("Something went wrong", 500);
 * </pre>
 *
 * @param <T> the type of the response payload
 *
 * @author Abhishek
 */
@Data
public class APIResponse<T> {

    /**
     * HTTP status code or custom application code.
     */
    private int code;

    /**
     * Message indicating success or error details.
     */
    private String message;

    /**
     * Generic payload containing response data.
     */
    private T data;

    /**
     * Private default constructor to restrict direct instantiation.
     */
    private APIResponse(){}

    /**
     * Private constructor for responses without data.
     *
     * @param status response status code
     * @param message response message
     */
    private APIResponse(int status, String message){
        this.code = status;
        this.message = message;
    }

    /**
     * Private constructor for responses with data.
     *
     * @param status response status code
     * @param message response message
     * @param data response payload
     */
    private APIResponse(int status, String message, T data){
        this.code = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Creates a success response with data and custom message.
     *
     * @param data response payload
     * @param message success message
     * @param <T> type of data
     * @return success {@link APIResponse}
     */
    public static <T> APIResponse<T> success(T data, String message) {
        return new APIResponse<>(200, message, data);
    }

    /**
     * Creates a success response with message only.
     *
     * @param message success message
     * @param <T> type of data
     * @return success {@link APIResponse}
     */
    public static <T> APIResponse<T> success(String message) {
        return new APIResponse<>(200, message);
    }

    /**
     * Creates a success response with data and default message.
     *
     * @param data response payload
     * @param <T> type of data
     * @return success {@link APIResponse}
     */
    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(200, "Success", data);
    }

    /**
     * Creates an error response with custom message and status code.
     *
     * @param message error message
     * @param code error status code
     * @param <T> type of data
     * @return error {@link APIResponse}
     */
    public static <T> APIResponse<T> error(String message, int code) {
        return new APIResponse<>(code, message);
    }

    /**
     * Creates an error response with default status code (500).
     *
     * @param message error message
     * @param <T> type of data
     * @return error {@link APIResponse}
     */
    public static <T> APIResponse<T> error(String message) {
        return new APIResponse<>(500, message);
    }
}
