package kurzycnb.gabrieljan.cz.cnb;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Date;
        import android.annotation.SuppressLint;

public class Parser {
    public static final String BASE_URL = "http://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt";
    public static final String DATE_PARAMETR = "?date=";
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public static ArrayList<CurrencyItem> parseAll(Date date, String input) throws ParseException{
        ArrayList<CurrencyItem> items;
        try {
            items = new ArrayList<CurrencyItem>();
            String[] lines = input.split(System.getProperty("line.separator"));
            lines[0] = lines[0].substring(4, 4 + DATE_FORMAT.length());
            for (int i = 2; i < lines.length; i++) {
                items.add(Parser.parseLine(lines[0] + "|" + lines[i]));
            }
            items.add(Parser.parseLine(lines[0] + "|" + "Česká republika|koruna|1|CZK|1"));
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), 0);
        }
        Collections.sort(items);
        return items;
    }

    @SuppressLint("SimpleDateFormat")
    private static CurrencyItem parseLine(String line) throws NumberFormatException, ParseException{
        String[] splits = line.split("\\|");
        return new CurrencyItem(
                new SimpleDateFormat(Parser.DATE_FORMAT).parse(splits[0]),
                splits[1],
                splits[2],
                Double.parseDouble(splits[3].replaceAll(",",".")),
                splits[4],
                Double.parseDouble(splits[5].replaceAll(",","."))
        );
    }

    @SuppressLint("SimpleDateFormat")
    public static String getUrlString(Date date){
        String datestring = new SimpleDateFormat(Parser.DATE_FORMAT).format(date);
        return Parser.BASE_URL + Parser.DATE_PARAMETR + datestring;
    }
}

