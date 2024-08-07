package stocks_api.stocks_api.utils;

import java.util.List;

public class DateListResponse <T> {
    private String msg;
    private List<T> value;
    private String minDate;
    private String maxDate;

    public DateListResponse(String msg, List<T> value, String minDate, String maxDate) {
        this.msg = msg;
        this.value = value;
        this.minDate = minDate;
        this.maxDate = maxDate;
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

    public String getMinDate() {
        return minDate;
    }

    public void setMinDate(String minDate) {
        this.minDate = minDate;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }
}