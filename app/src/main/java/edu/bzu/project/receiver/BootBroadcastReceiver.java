package edu.bzu.project.receiver;

import edu.bzu.project.utils.ConstantValues;
import edu.bzu.project.utils.SPUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
			SPUtils.put(context, ConstantValues.is_Alarm, false);
	}

}
