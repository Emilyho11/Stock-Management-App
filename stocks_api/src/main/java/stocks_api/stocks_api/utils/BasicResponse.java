package stocks_api.stocks_api.utils;

import stocks_api.stocks_api.logic.src.Reviews;

public class BasicResponse {
    private String msg;
    private String value;
  
    public BasicResponse(String msg) {
        this.msg = msg;
    }

    public static BasicResponse ok(String msg) {
        return new BasicResponse(msg);
    }

    public static BasicResponse value(String msg, String value) {
        BasicResponse response = new BasicResponse(msg);
        response.setValue(value);
        return response;
    }
    
    public static BasicResponse error(String msg) {
        return new BasicResponse(msg);
    }
    
    public String getMessage() {
        return msg;
    }
  
    public void setMessage(String msg) {
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
