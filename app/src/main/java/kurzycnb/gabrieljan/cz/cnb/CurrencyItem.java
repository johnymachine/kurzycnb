package kurzycnb.gabrieljan.cz.cnb;

import java.io.Serializable;
import java.util.Date;

public class CurrencyItem implements Serializable, Comparable<CurrencyItem>{

    private static final long serialVersionUID = 3853972535174864417L;
    public static final String COUNTRY_LABEL = "země";
    public static final String CURRENCY_LABEL = "měna";
    public static final String VOLUME_LABEL = "množství";
    public static final String CODE_LABEL = "kód";
    public static final String RATIO_LABEL = "kurz";

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private Date date;
    private String country;
    private String currency;
    private Double volume;
    private String code;
    private Double ratio;

    public CurrencyItem(Date date, String country, String currency,
                        Double volume, String code, Double ratio) {
        super();
        this.date = date;
        this.country = country;
        this.currency = currency;
        this.volume = volume;
        this.code = code;
        this.ratio = ratio;
    }

    public CurrencyItem(String country, String currency, Double volume,
                        String code, Double ratio) {
        super();
        this.date = new Date();
        this.country = country;
        this.currency = currency;
        this.volume = volume;
        this.code = code;
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return this.getCode() + " | " + this.getCountry() + " | " + this.getCurrency();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Override
    public int compareTo(CurrencyItem another) {
        return this.code.compareTo(another.code);
    }
}
