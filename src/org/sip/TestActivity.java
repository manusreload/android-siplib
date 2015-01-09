package org.sip;

import org.sipdroid.sipua.RegisterAgent;
import org.sipdroid.sipua.RegisterAgentListener;
import org.sipdroid.sipua.UserAgent;
import org.sipdroid.sipua.UserAgentProfile;
import org.zoolu.sip.address.NameAddress;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends Activity implements RegisterAgentListener {

	protected UserAgent userAgent;

	protected String SIP_USERNAME = "prueba_app2";
	protected String SIP_DOMAIN = "sbc.innovasur.es";
	protected String SIP_PASSWORD = "Sip_app23";
	protected String SIP_CALL_URL = "sip:664137867@" + SIP_DOMAIN;
	SIP sip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sip = new SIP(TestActivity.this);
				userAgent = sip.register(SIP_USERNAME, SIP_DOMAIN, SIP_PASSWORD, TestActivity.this);
			}
		}).start();
	}

	@Override
	public void onUaRegistrationSuccess(RegisterAgent ra, NameAddress target,
			NameAddress contact, String result) {
		// TODO Auto-generated method stub
		Log.d("", "Done");
		if(userAgent != null)
		{
			
			userAgent.setSendToneMode(true);
			userAgent.call(SIP_CALL_URL, false);
		}
	}

	@Override
	public void onMWIUpdate(RegisterAgent ra, boolean voicemail, int number,
			String vmacc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUaRegistrationFailure(RegisterAgent ra, NameAddress target,
			NameAddress contact, String result) {
		// TODO Auto-generated method stub
		Log.d("", "error " + result);
		
	}
	
	
	
}
