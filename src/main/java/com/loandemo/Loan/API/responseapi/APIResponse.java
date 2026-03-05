package com.loandemo.Loan.API.responseapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper
 * @param <T> type of data being returned
 */
@Data
public class APIResponse<T> {

    private int code;       // HTTP or custom status code
    private String message; // Success or error message
    private T data;         // Payload (generic type)

    private APIResponse(){}

    private APIResponse(int status, String message){
        this.code = status;
        this.message = message;
    }
    private APIResponse(int status,String message,T data){
        this.code = status;
        this.message = message;
        this.data = data;
    }
    // Convenience static methods for success/error

    public static <T> APIResponse<T> success(T data, String message) {
        return new APIResponse<>(200, message, data);
    }

    public static <T> APIResponse<T> success(String message) {
        return new APIResponse<>(200, message);
    }

    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(200, "Success", data);
    }

    public static <T> APIResponse<T> error(String message, int code) {
        return new APIResponse<>(code, message);
    }

    public static <T> APIResponse<T> error(String message) {
        return new APIResponse<>(500, message);
    }
}
