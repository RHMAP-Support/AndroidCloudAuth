/**
 * Copyright 2015 Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feedhenry.apps.BlankNativeAndroidApp;


import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.fh.JSONObject;

import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHHttpClient;
import com.feedhenry.sdk.FHResponse;
import com.feedhenry.sdk.api.FHAuthRequest;
import com.feedhenry.sdk.api.FHCloudRequest;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public class FHStarterActivity extends Activity {
	
	String TAG = "fh";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fhstarter);

        //call FH.init to initialize FH service
        FH.init(this, new FHActCallback() {

            @Override
            public void success(FHResponse resp) {
                //NOTE: other FH methods can only be called after FH.init succeeds
            	Log.i("init", "Hi.  I have just successfully done an init");
            	try {
					callCloud();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            	try {
            		loginWithFh();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            
            
            }

            @Override
            public void fail(FHResponse arg0) {
               	Log.i("init", "YOU HAVE FAIL");
            }
        });
    }

	public void callCloud() throws Exception {
    	
    	//build the request object with request path, method, headers and data
    	Header[] headers = new Header[1];
    	headers[0] = new BasicHeader("contentType", "application/json");
    	//The request should have a timeout of 25 seconds, 10 is the default
    	FHHttpClient.setTimeout(25000);
    	FHCloudRequest request;
		request = FH.buildCloudRequest("/hello", "POST",  headers, new JSONObject().put("username", "testuser2"));
    	
		//the request will be executed asynchronously
    	request.executeAsync(new FHActCallback() {
    	  @Override
    	  public void success(FHResponse res) {
    	    //the function to execute if the request is successful
             	Log.i("init", "Successful cloud call");
             	
    	  }

    	  @Override
    	  public void fail(FHResponse res) {
    	    //the function to execute if the request is failed
           	Log.i("init", "YOUR CLOUD CALL HAS FAILED");    	  
    	  }
    	});
    }
	
	public void loginWithFh(){
		  try{
		    FHAuthRequest authRequest = FH.buildAuthRequest("InstantSupportAuth", "testuser", "Testpass1");
		    authRequest.executeAsync(new FHActCallback() {

		      @Override
		      public void success(FHResponse resp) {
		    	  Log.i("init", "Authentication Successful");  
		      }

		      @Override
		      public void fail(FHResponse resp) {
		    	  Log.i("init", "AUTHENTICATION FAILED");  
		      }
		    });
		  }catch(Exception e){
		    e.printStackTrace();
		  }
		}	
}