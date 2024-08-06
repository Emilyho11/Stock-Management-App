package stocks_api.stocks_api.utils;

import java.util.List;

public class ListResponse<T> {
    private String msg;
    private List<T> value;

    public ListResponse(String msg, List<T> value) {
        this.msg = msg;
        this.value = value;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public List<T> getValue() {
        return value;
    }

    public void setValue(List<T> value) {
        this.value = value;
    }
}
