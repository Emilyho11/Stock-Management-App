package cs.toronto.edu.src;

public class StockData extends Table<StockData> {
    
    public static final String TABLE_NAME = "stock_data";
    private String f_symbol;
    private String f_timestamp;
    private double f_open;
    private double f_close;
    private double f_high;
    private double f_low;
    private double f_volume;

    public StockData() {}

    // @Override
    // public String getTableName() {
    //     return tableName;
    // }

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

    // Calculate the future value of a stock
    // Use the formula for compound interest: FV = PV * (1 + r)^t
    // Where:
    // FV = Future Value
    // PV = Present Value
    // r = rate of interest
    // t = time in years
    // For example, if the present value of a stock is $1000, the rate of interest is 10% and the time is 5 years,
    // the future value of the stock would be:
    // FV = 1000 * (1 + 0.1)^5 = 1000 * 1.61 = 1610
    public static double calculateFutureValue(double presentValue, double rate, int time) {
        return presentValue * Math.pow(1 + rate, time);
    }

    /*
     * One simplification is to just consider the correlation of each stock with the market as a whole (this is called the “Beta coefficient”) in building a portfolio
     * The Beta of the whole portfolio can thus be made larger or smaller than the market as a whole by choosing the right stocks.
     * To build a portfolio like this, we would need to know the future values of volatility, covariance, Beta, etc, or of the stock prices themselves. We only have the past ones. So, a very important discipline is prediction, determining how a stock is likely to move in the future based on how it and all other stocks have moved in the past, as well as predicting what the future values of the other statistical measures will be. Since the statistics are almost certainly nonstationary, we will occasionally fail completely in predicting the future. The best we can do is muddle through, but there is a huge range of possibilities, and a big part of any serious trading enterprise is data mining historical data to develop better and better predictors
     * Another important consideration is automation. We would like to have a computer program that continuously adapts the portfolio holdings in pursuit of maximizing return while controlling risk
     */
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

    public static double calculateVolatility(double[] returns) {
        return Math.sqrt(calculateVariance(returns));
    }

    public static double calculateReturn(double presentValue, double futureValue) {
        return (futureValue - presentValue) / presentValue;
    }
}
