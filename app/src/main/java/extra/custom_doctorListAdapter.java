package extra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.apps.scit.tabibihon.R;
import com.apps.scit.tabibihon.profile;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import costumize.ArabEditText;
import costumize.ArabTextView;
import entities.advice_wrapper;
import entities.base;
import entities.doctor;
import entities.doctor_list;
import entities.doctor_list_wrapper;
import entities.save_advice;
import entities.user;

public class custom_doctorListAdapter extends ArrayAdapter<doctor_list_wrapper> implements Serializable {

	private final ProgressDialog prgDialog;
	Context context;
	DisplayImageOptions options;

	ArrayList<doctor_list_wrapper> list;


	user me;


	public custom_doctorListAdapter(Context context, ArrayList<doctor_list_wrapper> objects, user current) {
		super(context, R.layout.doctor_list_item, objects);
		list=objects;
		this.context=context;
		this.me=current;


		prgDialog = new ProgressDialog(context);
		// Set Progress Dialog Text
		prgDialog.setMessage("Please wait...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);

//		options = new DisplayImageOptions.Builder()
//				.cacheInMemory(false)
//				.cacheOnDisk(false)
//				.considerExifParams(true)
//				.bitmapConfig(Bitmap.Config.RGB_565)
//				.displayer(new RoundedBitmapDisplayer(40))
//				.build();
//		if(!ImageLoader.getInstance().isInited())
//			ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
	}

	
	


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//Log.d("CustomArrayAdapter", String.valueOf(position));
		LayoutInflater inflater = (LayoutInflater)  context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		final View rowView= inflater.inflate(R.layout.doctor_list_item, null, true);



		final doctor_list_wrapper dl=list.get(position);

		final TextView name = (TextView) rowView.findViewById(R.id.doc);

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
				ii.putExtra("user", me);
				ii.putExtra("id", Integer.parseInt(dl.getDoc_li().getDoctor_ID()));
				context.startActivity(ii);


			}
		});


		name.setText(" د. " + dl.getDoc());
		//ImageView img=(ImageView)rowView.findViewById(R.id.doc_list_img);

//		if(!TextUtils.isEmpty(dl.getImg())) {
//			ImageLoader.getInstance().displayImage(base.BASE_URL + "125/" + dl.getImg(),  img, options);
//		}


		
		return rowView;
		
	}



	public  void delete(final doctor_list_wrapper adv){
		prgDialog.show();


		RequestParams request=new RequestParams();
		request.put("token", me.getAccessToken());
		request.put("table", "doctor_list");
		request.put("ID", adv.getDoc_li().getID()+"");

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
