package extra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.scit.tabibihon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import costumize.ArabEditText;
import costumize.ArabTextView;
import entities.admin_notification;
import entities.advice_wrapper;
import entities.base;
import entities.doctor;
import entities.save_advice;
import entities.user;

public class custom_adminNotificationeListAdapter extends ArrayAdapter<admin_notification> implements Serializable {

	private final ProgressDialog prgDialog;
	Context context;

	ArrayList<admin_notification> list;


	public custom_adminNotificationeListAdapter(Context context, ArrayList<admin_notification> objects) {
		super(context, R.layout.admin_notification_item, objects);
		list=objects;
		this.context=context;


		prgDialog = new ProgressDialog(context);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);

		
	}

	
	


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//Log.d("CustomArrayAdapter", String.valueOf(position));
		LayoutInflater inflater = (LayoutInflater)  context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		final View rowView= inflater.inflate(R.layout.admin_notification_item, null, true);



		final admin_notification notif=list.get(position);

		final TextView n_date = (TextView) rowView.findViewById(R.id.n_date);
		final TextView notification = (TextView) rowView.findViewById(R.id.notification);
		final LinearLayout ly = (LinearLayout) rowView.findViewById(R.id.notif_icon);


		notification.setText(notif.getNotification());
		n_date.setText(notif.getN_time().substring(0,10));

		if(notif.getFlag().equals("0"))
		ly.setBackgroundColor(Color.rgb(255,255,255));
		
		return rowView;
		
	}






}
