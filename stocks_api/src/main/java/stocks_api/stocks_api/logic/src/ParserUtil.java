package stocks_api.stocks_api.logic.src;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.simple.JSONArray;

// Function that converts a ResultSet to a JSON string
public class ParserUtil {
    public static String resultSetToJson(ResultSet rs) {
        try {
            JSONArray json = new JSONArray();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            while (rs.next()) {
                JSONArray row = new JSONArray();
                for (int i = 1; i <= numColumns; i++) {
                    row.add(rs.getObject(i));
                }
                json.add(row);
            }
            return json.toJSONString().toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error converting ResultSet to JSON";
        }
    }
}