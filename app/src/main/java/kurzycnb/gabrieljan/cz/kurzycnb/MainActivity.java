package kurzycnb.gabrieljan.cz.kurzycnb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import kurzycnb.gabrieljan.cz.cnb.CurrencyItem;
import kurzycnb.gabrieljan.cz.cnb.Fetch;


public class MainActivity extends ActionBarActivity {
    private Spinner lspinner;
    private Spinner rspinner;
    private EditText ledit;
    private EditText redit;

    private ArrayList<CurrencyItem> currencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lspinner = (Spinner) findViewById(R.id.spinner1);
        rspinner = (Spinner) findViewById(R.id.spinner2);
        ledit = (EditText)findViewById(R.id.editText1);
        redit = (EditText)findViewById(R.id.editText2);

        if (savedInstanceState != null) {
            this.currencies = (ArrayList<CurrencyItem>) savedInstanceState.getSerializable("currencies");
        }

        if(!isNetworkAvailable(this) && this.currencies == null) {
            Toast.makeText(this, "Aplikaci nelze používat bez připojení k internetu.", Toast.LENGTH_LONG).show();
            finish(); //Calling this method to close this activity when internet is not available.
        }

        else if(isNetworkAvailable(this) && this.currencies == null){
            this.enableLayout(false);
            new Fetch(this).execute(new Date());
        }

        else{
            this.dataLoaded(this.currencies);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("currencies", (ArrayList<CurrencyItem>) this.currencies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void dataLoaded(ArrayList<CurrencyItem> currencies){
        if(this.currencies == null){
            this.currencies = currencies;
            this.enableLayout(true);
        }

        //populate spinners
        ArrayAdapter<CurrencyItem> dataAdapter = new ArrayAdapter<CurrencyItem>(this,
                android.R.layout.simple_spinner_item, this.currencies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lspinner.setAdapter(dataAdapter);
        rspinner.setAdapter(dataAdapter);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    public void enableLayout(boolean value){
        RelativeLayout app = (RelativeLayout)this.findViewById(R.id.applayout);

        if(!value){
            app.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"Načítám data...",Toast.LENGTH_SHORT).show();
        }
        else{
            app.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Data načtena...",Toast.LENGTH_SHORT).show();
        }
    }

    public void onLeftClick(View view) {
        Double result = this.convertCurrency((CurrencyItem)lspinner.getSelectedItem(), (CurrencyItem)rspinner.getSelectedItem(), Double.parseDouble(ledit.getText().toString()));
        result = Math.floor(result * 1000) / 1000;
        redit.setText(result.toString());
    }

    public void onRightClick(View view) {
        Double result = this.convertCurrency((CurrencyItem)rspinner.getSelectedItem(), (CurrencyItem)lspinner.getSelectedItem(), Double.parseDouble(redit.getText().toString()));
        result = Math.floor(result * 1000) / 1000;
        ledit.setText(result.toString());
    }

    public Double convertCurrency(CurrencyItem from, CurrencyItem to, Double volume){
        Double inczk = (volume * from.getRatio()) / from.getVolume();
        return (inczk / to.getRatio()) * to.getVolume();
    }
}
