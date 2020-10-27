package com.sicmatr1x.vo;

/**
 * @author sicmatr1x
 */
public class CommonVo {
    private Boolean success;
    private Object data;
    private String errorMessage;

    public CommonVo() {
    }

    public CommonVo(Boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public CommonVo(Boolean success) {
        this.success = success;
    }

    public CommonVo(Boolean success, Object data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "CommonVo{" +
                "success='" + success + '\'' +
                ", data='" + data + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
