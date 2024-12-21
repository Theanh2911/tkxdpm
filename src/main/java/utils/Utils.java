package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

public class Utils {

    public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static Logger LOGGER = getLogger(Utils.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-4s] [%1$tF %1$tT] [%2$-7s] %5$s %n");
    }

    public static Logger getLogger(String className) {
        return Logger.getLogger(className);
    }


    public static String getCurrencyFormat(int num) {
        Locale vietname = new Locale("vi", "VN");
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(vietname);
        return defaultFormat.format(num * 1000);
    }

    public static String getToday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String formatDateTime(Date date, String format) {
        if(date == null) return "";
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static void navigate(Stage currentStage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.show();
    }

}