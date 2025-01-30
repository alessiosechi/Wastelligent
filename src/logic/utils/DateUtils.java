package logic.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class DateUtils {

    private static final Logger logger = Logger.getLogger(DateUtils.class.getName());
    
    private DateUtils() {
    }

    public static boolean isOggi(String dataRiscatto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date data = sdf.parse(dataRiscatto);
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            Calendar oggi = Calendar.getInstance();

            return cal.get(Calendar.YEAR) == oggi.get(Calendar.YEAR)
                    && cal.get(Calendar.DAY_OF_YEAR) == oggi.get(Calendar.DAY_OF_YEAR);
        } catch (ParseException e) {
            logger.severe("Errore nella conversione della data: " + e.getMessage());
            return false;
        }
    }
}

