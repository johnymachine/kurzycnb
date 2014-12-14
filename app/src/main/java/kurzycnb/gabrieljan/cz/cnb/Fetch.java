package kurzycnb.gabrieljan.cz.cnb;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.text.ParseException;
        import java.util.ArrayList;
        import java.util.Date;
        import android.app.Activity;
        import android.os.AsyncTask;
        import android.util.Log;
        import kurzycnb.gabrieljan.cz.kurzycnb.MainActivity;

public class Fetch extends AsyncTask<Date, Void, ArrayList<CurrencyItem>> {
    private Activity mainactivity;

    public Fetch(Activity activity) {
        super();
        // asociate activity
        this.mainactivity = activity;
    }

    @Override
    protected void onPostExecute(ArrayList<CurrencyItem> result) {
        ((MainActivity)this.mainactivity).dataLoaded(result);
    }

    @Override
    protected ArrayList<CurrencyItem> doInBackground(Date... date) {
        try {
            URL url = new URL(Parser.getUrlString(date[0]));
            String content = downloadUrl(url);
            return Parser.parseAll(date[0], content);
        } catch (MalformedURLException e) {
            Log.d("Error", "Error while parsing URL: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Error", "Problem with connection: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            Log.d("Error", "Error while parsing data: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<CurrencyItem>();
    }

    private String downloadUrl(URL myurl) throws IOException {
        BufferedReader buffin = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            if(conn.getResponseCode() != 200){
                Log.d("Error", "Responded with: " + conn.getResponseCode());
                throw new IOException("Response: " + conn.getResponseCode());
            }
            Log.d("Succes", "Connected with response: " + conn.getResponseCode());
            buffin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // Convert the InputStream into a string
            String contentAsString = null;
            String inputLine = "";
            while ((inputLine = buffin.readLine()) != null){
                contentAsString = contentAsString + inputLine + System.getProperty("line.separator");
                Log.d("Succes", "Outputing line: " + inputLine);
            }
            return contentAsString;
        } finally {
            if (buffin != null) {
                buffin.close();
            }
        }
    }
}

