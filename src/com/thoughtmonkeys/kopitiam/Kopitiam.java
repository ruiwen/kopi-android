package com.thoughtmonkeys.kopitiam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.json.JSONObject;

import com.thoughtmonkeys.kopitiam.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.admob.android.ads.*;

public class Kopitiam extends Activity {
	
	private HashMap<String, Button> selections;
	//private HashMap<String, String[]> drinks;
	private JSONObject drinks;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Initialise our selection array
        selections = new HashMap<String, Button>();
        
        // Initialise our drink values
        //drinks = new HashMap<String, String[]>();
        try {        	
	        InputStream is = this.getResources().openRawResource(R.raw.drinks);
	        byte [] buffer = new byte[is.available()];
			while (is.read(buffer) != -1);
			String json = new String(buffer);
			drinks = new JSONObject(json);
			
			Log.d("JSON", drinks.toString());
        }
        catch(Exception e) {
        	// Whoops
        	e.printStackTrace();
        }
        
        
        // Set up event handlers
        
        /** Milk **/
        Button milkNone = (Button)findViewById(R.id.milknone);
        milkNone.setOnClickListener(setMilk);
        
        Button milkCondensed = (Button)findViewById(R.id.milkcondensed);
        milkCondensed.setOnClickListener(setMilk);        
        
        Button milkEvaporated = (Button)findViewById(R.id.milkevaporated);
        milkEvaporated.setOnClickListener(setMilk);
        
        /** Strength **/
        Button strengthWeak = (Button)findViewById(R.id.strengthweak);
        strengthWeak.setOnClickListener(setStrength);
        
        Button strengthNormal = (Button)findViewById(R.id.strengthnormal);
        strengthNormal.setOnClickListener(setStrength);
        
        Button strengthStrong = (Button)findViewById(R.id.strengthstrong);
        strengthStrong.setOnClickListener(setStrength);
        
        /** Sweetness **/
        Button sweetnessNone = (Button)findViewById(R.id.sweetnessnone);
        sweetnessNone.setOnClickListener(setSweetness);
        
        Button sweetnessLess = (Button)findViewById(R.id.sweetnessless);
        sweetnessLess.setOnClickListener(setSweetness);
        
        Button sweetnessNormal = (Button)findViewById(R.id.sweetnessnormal);
        sweetnessNormal.setOnClickListener(setSweetness);
        
        Button sweetnessMore = (Button)findViewById(R.id.sweetnessmore);
        sweetnessMore.setOnClickListener(setSweetness);
        
        
        /** Ice **/
        Button iceYes = (Button)findViewById(R.id.iceyes);
        iceYes.setOnClickListener(setIce);
        
        Button iceNo = (Button)findViewById(R.id.iceno);
        iceNo.setOnClickListener(setIce);
        
        
        /** Set the defaults **/
        selections.put("milk", milkCondensed);
        selections.put("strength", strengthNormal);
        selections.put("sweetness", sweetnessNormal);
        selections.put("ice", iceNo);
    }
    
    private void setCaption() {
    	
    	StringBuilder caption = new StringBuilder("kopi");
    	TextView kopiCaption = (TextView)findViewById(R.id.kopicaption);
    	
    	String[] order = {"milk", "strength", "sweetness", "ice"}; // The order in which we want to access the selections
    	
    	
    	for(String key : order) {
    		try {
    			String option = drinks.getJSONObject(key).getString(selections.get(key).getText().toString().toLowerCase());
    			
    			if(option.length() > 0) {
    				caption.append(" " + option);    				
    			}
    		}
    		catch(Exception e) {
    			// Oopsie.    			
    		}
    	}
    	
    	kopiCaption.setText(caption.toString().toUpperCase());    	
    }
    
    private void addCaption(String key, Button b) {
    	selections.put(key,b);
    	
    	setCaption();
    }
    
    private OnClickListener setMilk = new OnClickListener() {    	
        public void onClick(View v) {
        	Button b = (Button)v;
   		 	//Toast.makeText(Kopitiam.this, b.getText(),
            //     Toast.LENGTH_SHORT).show();  
   		 	
   		 	addCaption("milk", b);
        }
    };
    
    private OnClickListener setStrength = new OnClickListener() {
    	public void onClick(View v) {
    		Button b = (Button)v;
   		 	addCaption("strength", b);
    	}    	
    };
    
    private OnClickListener setSweetness = new OnClickListener() {
    	public void onClick(View v) {
    		Button b = (Button)v;
   		 	addCaption("sweetness", b);
    	}    	
    };
    
    private OnClickListener setIce = new OnClickListener() {
    	public void onClick(View v) {
    		Button b = (Button)v;
   		 	addCaption("ice", b);
    	}    	
    };    
}