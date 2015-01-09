package org.sip;

import org.zoolu.sip.call.Call;
import org.zoolu.sip.message.Message;

public interface CallListenner {

	public void onCallAccepted(Call call, String sdp, Message resp);
	public void onCallClosed(Call call, Message resp);
	public void onCallRinging(Call call, Message resp);
}
