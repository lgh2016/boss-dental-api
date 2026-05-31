package mx.com.bossdental.api.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Response<T> {

    private T data;
    private String message;
    private Boolean success;
}
