package org.sip;

import org.sipdroid.sipua.RegisterAgent;
import org.sipdroid.sipua.RegisterAgentListener;
import org.sipdroid.sipua.UserAgent;
import org.sipdroid.sipua.UserAgentProfile;
import org.zoolu.net.IpAddress;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.call.Call;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipStack;

import android.content.Context;
import android.os.Build;

public class SIP implements RegisterAgentListener {
	public static Context mContext;

	CallListenner callListenner = null;
	public SIP(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		SipStack.init(null);

		SipStack.debug_level = 0;
		//SipStack.log_path = "/data/data/" + context.getPackageName();
		SipStack.max_retransmission_timeout = 4000;
		SipStack.default_transport_protocols = new String[1];
		SipStack.default_transport_protocols[0] = "udp";

		if (SipStack.default_transport_protocols[0].equals("tls"))
			SipStack.default_transport_protocols[0] = "tcp";
		String version = "android-siplib" + "v1.0"  + "/"
				+ Build.MODEL;
		SipStack.ua_info = version;
		SipStack.server_info = version;
	}

	public UserAgent register(String username, String domain, String password, RegisterAgentListener listenner) {
		
		SipProvider provider = new SipProvider(domain, 5060);
		UserAgentProfile profile = new UserAgentProfile();
		profile.username = username;
		profile.passwd = password;
		profile.from_url = profile.username;
		profile.contact_url = getContactURL(profile.username,provider);
		//UserAgent ua = new UserAgent(provider, profile);
		profile.from_url += "@" + domain;
		
		RegisterAgent ra = new RegisterAgent(provider, profile.from_url, // modified
				profile.contact_url, profile.username,
				profile.realm, profile.passwd, listenner, profile,
				"1.00", null, profile.pub); // added by mandrajg
		
		ra.register();
		return new UserAgent(provider, profile)
		{
			@Override
			public void onCallAccepted(Call call, String sdp, Message resp) {
				// TODO Auto-generated method stub
				super.onCallAccepted(call, sdp, resp);
				if(callListenner != null) callListenner.onCallAccepted(call, sdp, resp);
			}
			
			@Override
			public void onCallClosed(Call call, Message resp) {
				// TODO Auto-generated method stub
				super.onCallClosed(call, resp);
				if(callListenner != null) callListenner.onCallClosed(call, resp);
			}
			
			@Override
			public void onCallRinging(Call call, Message resp) {
				// TODO Auto-generated method stub
				super.onCallRinging(call, resp);
				if(callListenner != null) callListenner.onCallRinging(call, resp);
			}
			
			@Override
			public void onCallClosing(Call call, Message bye) {
				// TODO Auto-generated method stub
				super.onCallClosing(call, bye);
				if(callListenner != null) callListenner.onCallClosed(call, bye);
			}
			
			@Override
			public void onCallRefused(Call call, String reason, Message resp) {
				// TODO Auto-generated method stub
				super.onCallRefused(call, reason, resp);
				if(callListenner != null) callListenner.onCallClosed(call, resp);
			}
			
			@Override
			public void onCallCanceling(Call call, Message cancel) {
				// TODO Auto-generated method stub
				super.onCallCanceling(call, cancel);
			}
			
			
		
		};
		//RegisterAgent ra = new RegisterAgent(sip_provider, target_url, contact_url, username, realm, passwd, listener, user_profile, qvalue, icsi, pub)

	}
	
	public void setCallListenner(CallListenner listenner) {
		this.callListenner = listenner;
	}
	
	
	

	private String getContactURL(String username,SipProvider sip_provider) {
		IpAddress.setLocalIpAddress();
		int i = username.indexOf("@");
		if (i != -1) {
			// if the username already contains a @ 
			//strip it and everthing following it
			username = username.substring(0, i);
		}

		return username + "@" + IpAddress.localIpAddress
		+ (sip_provider.getPort() != 0?":"+sip_provider.getPort():"")
		+ ";transport=" + sip_provider.getDefaultTransport();		
	}

	@Override
	public void onUaRegistrationSuccess(RegisterAgent ra, NameAddress target,
			NameAddress contact, String result) {
		// TODO Auto-generated method stub
		
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
		
	}

}
