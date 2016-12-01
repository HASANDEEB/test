package extra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.scit.tabibihon.R;
import com.apps.scit.tabibihon.profile;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;

import entities.base;
import entities.saveAdviceWrepper;
import entities.user;

public class custom_save_adviceListAdapter extends ArrayAdapter<saveAdviceWrepper> implements Serializable {

	private final ProgressDialog prgDialog;
	Context context;

	ArrayList<saveAdviceWrepper> list;


	user me;


	public custom_save_adviceListAdapter(Context context, ArrayList<saveAdviceWrepper> objects, user current) {
		super(context, R.layout.user_advics_item, objects);
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
		final View rowView= inflater.inflate(R.layout.user_advics_item, null, true);



		final saveAdviceWrepper dl=list.get(position);

		final TextView name = (TextView) rowView.findViewById(R.id.doc);
		final TextView advice = (TextView) rowView.findViewById(R.id.adv);
		final TextView date = (TextView) rowView.findViewById(R.id.date);

		ImageButton del=(ImageButton)rowView.findViewById(R.id.delete_btn);
		ImageButton view=(ImageButton)rowView.findViewById(R.id.view_btn);


		del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delete(dl);
			}
		});
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent ii = new Intent(context, profile.class);
				ii.putExtra("user",me);
				ii.putExtra("id",Integer.parseInt(dl.getAdv().getDoctor_ID()));
				context.startActivity(ii);


			}
		});


		name.setText(" د. "+ dl.getDoc());
		date.setText( dl.getAdv().getA_date());
		advice.setText( dl.getAdv().getContent());



		
		return rowView;
		
	}



	public  void delete(final saveAdviceWrepper adv){
		prgDialog.show();


		RequestParams request=new RequestParams();
		request.put("token", me.getAccessToken());
		request.put("table", "save_advice");
		request.put("ID", adv.getSa_id()+"");

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(base.BASE_URL + "delete" , request, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {


				try {
					list.remove(adv);
					notifyDataSetChanged();
					if(list.size()==0)
					{
						((Activity)context).findViewById(R.id.search_no_result).setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					Toast.makeText(context, "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
				}

				prgDialog.hide();
			}

			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {
				Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
				prgDialog.hide();

			}
		});


	}





}
