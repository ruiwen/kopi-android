package com.thoughtmonkeys.kopitiam;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONObject;

import com.thoughtmonkeys.kopitiam.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.admob.android.ads.*;

public class Kopitiam extends Activity {
	
	// Dialogs
	private static final int DIALOG_CONDENSED_WHEN_KOSONG = 1;
	private static final int DIALOG_KOSONG_WHEN_CONDENSED = 2;
	
	
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
			
        }
        catch(Exception e) {
        	// Whoops
        	e.printStackTrace();
        }
        
        
        // Set up event handlers
        
        /** Milk **/
        Button milkNone = (Button)findViewById(R.id.milknone);
        milkNone.setOnClickListener(addCaption);
        
        Button milkCondensed = (Button)findViewById(R.id.milkcondensed);
        milkCondensed.setOnClickListener(addCaption);        
        
        Button milkEvaporated = (Button)findViewById(R.id.milkevaporated);
        milkEvaporated.setOnClickListener(addCaption);
        
        /** Strength **/
        Button strengthWeak = (Button)findViewById(R.id.strengthweak);
        strengthWeak.setOnClickListener(addCaption);
        
        Button strengthNormal = (Button)findViewById(R.id.strengthnormal);
        strengthNormal.setOnClickListener(addCaption);
        
        Button strengthStrong = (Button)findViewById(R.id.strengthstrong);
        strengthStrong.setOnClickListener(addCaption);
        
        /** Sweetness **/
        Button sweetnessNone = (Button)findViewById(R.id.sweetnessnone);
        sweetnessNone.setOnClickListener(addCaption);
        
        Button sweetnessLess = (Button)findViewById(R.id.sweetnessless);
        sweetnessLess.setOnClickListener(addCaption);
        
        Button sweetnessNormal = (Button)findViewById(R.id.sweetnessnormal);
        sweetnessNormal.setOnClickListener(addCaption);
        
        Button sweetnessMore = (Button)findViewById(R.id.sweetnessmore);
        sweetnessMore.setOnClickListener(addCaption);
        
        
        /** Ice **/
        Button iceYes = (Button)findViewById(R.id.iceyes);
        iceYes.setOnClickListener(addCaption);
        
        Button iceNo = (Button)findViewById(R.id.iceno);
        iceNo.setOnClickListener(addCaption);
        
        
        /** Set the defaults **/
        selections.put("milk", milkCondensed);
        milkCondensed.setBackgroundResource(R.drawable.roundbuttonselected);
        
        selections.put("strength", strengthNormal);
        strengthNormal.setBackgroundResource(R.drawable.roundbuttonselected);
        
        selections.put("sweetness", sweetnessNormal);
        sweetnessNormal.setBackgroundResource(R.drawable.roundbuttonselected);
        
        selections.put("ice", iceNo);
        iceNo.setBackgroundResource(R.drawable.roundbuttonselected);
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
    
    private OnClickListener addCaption = new OnClickListener() {
    	public void onClick(View v) {
    		Button b = (Button)v;
    		RelativeLayout r = (RelativeLayout)b.getParent();
    		String tag = r.getTag().toString();
    		
    		// Check if we have a CondensedMilk / Kosong situation
    		if(b.getText().toString().equalsIgnoreCase("condensed") && selections.get("sweetness").getText().toString().equalsIgnoreCase("none")) {
    			showDialog(DIALOG_CONDENSED_WHEN_KOSONG);
    			return;
    		}
    		else if(b.getText().toString().equalsIgnoreCase("none") && tag.equalsIgnoreCase("sweetness") && selections.get("milk").getText().toString().equalsIgnoreCase("condensed")) {
    			showDialog(DIALOG_KOSONG_WHEN_CONDENSED);
    			return;
    		}
    		
    		Button ob = selections.get(r.getTag().toString());
    		ob.setBackgroundResource(R.drawable.roundbutton);
    		b.setBackgroundResource(R.drawable.roundbuttonselected);
    		
    		selections.put(r.getTag().toString(), b);
    		
    		setCaption();
    	}    	
    };
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
    	case DIALOG_CONDENSED_WHEN_KOSONG: 
    		return new AlertDialog.Builder(Kopitiam.this)
    					.setTitle("Unable to select condensed milk.")
    					.setMessage("You have indicated that you'd like your coffee unsweetened. Choosing Condensed milk now will make it sweet. Please make another milk selection.")
    					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    				           public void onClick(DialogInterface dialog, int id) {
    				                // End
    				        	   Kopitiam.this.removeDialog(id);
    				           }
    				       })
    				       .create();    		
    	
    	case DIALOG_KOSONG_WHEN_CONDENSED: 
    		return new AlertDialog.Builder(Kopitiam.this)
    					.setTitle("Unable to select unsweetened coffee.")
    					.setMessage("You have indicated that you'd like Condensed milk in your coffee, which makes it sweet. Please choose another level of sweetness, like Less.")
    					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    				           public void onClick(DialogInterface dialog, int id) {
    				                // End
    				        	   Kopitiam.this.removeDialog(id);
    				           }
    				       })
    				       .create();
    		
    	} 
    	
    	return null;
    }
}