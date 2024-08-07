package stocks_api.stocks_api.logic.src;

public class StockData extends Table<StockData> {
    
    public static final String TABLE_NAME = "stock_data";
    public String f_symbol;
    public String f_timestamp;
    public double f_open;
    public double f_close;
    public double f_high;
    public double f_low;
    public double f_volume;

    public StockData() {}

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getWhereIdentifier() {
        return "symbol = '" + f_symbol + "' AND timestamp = '" + f_timestamp + "'";
    }

    @Override
    public String getKey() {
        return f_symbol + ", " + f_timestamp;
    }

    @Override
    public void setKey(String key) {
        String[] keys = key.split(", ");
        f_symbol = keys[0];
        f_timestamp = keys[1];
    }

    public String getSymbol() {
        return f_symbol;
    }

    public String getTimestamp() {
        return f_timestamp;
    }

    public double getOpen() {
        return f_open;
    }

    public double getClose() {
        return f_close;
    }

    public double getHigh() {
        return f_high;
    }

    public double getLow() {
        return f_low;
    }

    public double getVolume() {
        return f_volume;
    }

    public void setSymbol(String symbol) {
        f_symbol = symbol;
    }

    public void setTimestamp(String timestamp) {
        f_timestamp = timestamp;
    }

    public void setOpen(double open) {
        f_open = open;
    }

    public void setClose(double close) {
        f_close = close;
    }

    public void setHigh(double high) {
        f_high = high;
    }

    public void setLow(double low) {
        f_low = low;
    }

    public void setVolume(double volume) {
        f_volume = volume;
    }

    // Calculate the beta value of a stock
    public static double calculateBeta(double covariance, double variance) {
        return covariance / variance;
    }

    public static double calculateCovariance(double[] stockReturns, double[] marketReturns) {
        double covariance = 0;
        double stockMean = calculateMean(stockReturns);
        double marketMean = calculateMean(marketReturns);
        for (int i = 0; i < stockReturns.length; i++) {
            covariance += (stockReturns[i] - stockMean) * (marketReturns[i] - marketMean);
        }
        return covariance / stockReturns.length;
    }

    public static double calculateVariance(double[] returns) {
        double variance = 0;
        double mean = calculateMean(returns);
        for (double ret : returns) {
            variance += Math.pow(ret - mean, 2);
        }
        return variance / returns.length;
    }

    public static double calculateMean(double[] returns) {
        double sum = 0;
        for (double ret : returns) {
            sum += ret;
        }
        return sum / returns.length;
    }
}
