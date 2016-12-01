package extra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.scit.tabibihon.R;
import com.apps.scit.tabibihon.profile;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import entities.base;
import entities.notification_wrapper;
import entities.save_advice;
import entities.user;

public class custom_notificationListAdapter extends ArrayAdapter<notification_wrapper> implements Serializable {

	private final ProgressDialog prgDialog;
	Context context;

	ArrayList<notification_wrapper> list;


	user me;


	public custom_notificationListAdapter(Context context, ArrayList<notification_wrapper> objects, user current) {
		super(context, R.layout.notification_item, objects);
		list=objects;
		this.context=context;
		this.me=current;


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
		final View rowView= inflater.inflate(R.layout.notification_item, null, true);



		final notification_wrapper dl=list.get(position);

		final TextView name = (TextView) rowView.findViewById(R.id.name);
		final TextView advice = (TextView) rowView.findViewById(R.id.adv);
		final TextView date = (TextView) rowView.findViewById(R.id.date);

		final ImageButton save=(ImageButton)rowView.findViewById(R.id.save_btn);
		ImageButton view=(ImageButton)rowView.findViewById(R.id.view_btn);

		if(dl.getFlag()!=1)
		{

			((LinearLayout)rowView.findViewById(R.id.notif_icon)).setBackgroundColor(Color.rgb(255,255,255));
		}


		if(dl.getSaved()>0){
			save.setEnabled(false);

		}


		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				if(dl.getSaved()>0){
					Toast.makeText(getContext(), "هذه النصيحة محفوظة مسبقا", Toast.LENGTH_SHORT).show();
				}
				else {
					save(dl, position, save);
					if (dl.getFlag() == 1) {
						setAsRead(dl);
					}
				}
			}
		});
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent ii = new Intent(context, profile.class);
				ii.putExtra("user", me);
				ii.putExtra("id", Integer.parseInt(dl.getAdv().getDoctor_ID()));
				context.startActivity(ii);

				if(dl.getFlag()==1)
				{
					setAsRead(dl);
				}

			}
		});


		name.setText(" د. " + dl.getDoc());
		date.setText(dl.getAdv().getA_date());
		advice.setText(dl.getAdv().getContent());




		return rowView;
		
	}



	public  void save(final notification_wrapper adv,final int pos,final ImageButton img){
		prgDialog.show();
		user us=me;
		save_advice dl=new save_advice();
		dl.setAdvice_ID(adv.getAdv().getID() + "");
		dl.setUser_ID(us.getID() + "");

		RequestParams request= dl.getEntity();
		request.put("token", us.getAccessToken());
		request.put("table", "save_advice");

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(base.BASE_URL + "add", request, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {


				try {
					JSONObject obj = new JSONObject(response);
					img.setEnabled(false);
					Toast.makeText(getContext(), "تمث عملية اضافة النصيحة الى القائمة بنجاح", Toast.LENGTH_SHORT).show();


				} catch (Exception e) {
					Toast.makeText(getContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
				}

				prgDialog.hide();
			}

			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {
				Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
				prgDialog.hide();

			}
		});


	}




	void setAsRead(final notification_wrapper adv){

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams req= new RequestParams();
		req.put("table","notification");
		req.put("flag","0");
		req.put("ID",adv.getId()+"");
		req.put("token",me.getAccessToken());

		client.post(base.BASE_URL + "edit",req, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {


			}

			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {

				Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();

			}
		});



	}


}
