package extra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import entities.save_advice;
import entities.user;

public class custom_AdviceListAdapter extends ArrayAdapter<advice_wrapper> implements Serializable {

	private final ProgressDialog prgDialog;
	Context context;

	ArrayList<advice_wrapper> list;

	doctor target;
	base me;


	public custom_AdviceListAdapter(Context context, ArrayList<advice_wrapper> objects, base current,doctor target) {
		super(context, R.layout.advice_item, objects);
		list=objects;
		this.context=context;
		this.me=current;
		this.target=target;

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
		final View rowView= inflater.inflate(R.layout.advice_item, null, true);



		final advice_wrapper advic=list.get(position);

		final TextView adv = (TextView) rowView.findViewById(R.id.advice);
		final TextView date = (TextView) rowView.findViewById(R.id.a_date);

		final ImageButton img=(ImageButton)rowView.findViewById(R.id.save_img);
		final Button edit=(Button)rowView.findViewById(R.id.edit);
		final Button delete=(Button)rowView.findViewById(R.id.delete);
		final Button save=(Button)rowView.findViewById(R.id.save);



		if(me instanceof user)
		{
			delete.setVisibility(View.GONE);
			edit.setVisibility(View.GONE);
			//check(advic,img,save,position);

			if(advic.getUpdated()==-1)
			{
				img.setImageResource(R.drawable.ic_menu_save);
				save.setText("اضافة");

			}
			else{
				img.setImageResource(R.drawable.ic_menu_delete);
				save.setText("ازالة ");


			}


		}
		else
		{
			save.setVisibility(View.GONE);
			img.setVisibility(View.INVISIBLE);

			if(target.getID()!=((doctor)me).getID())
			{
				delete.setVisibility(View.GONE);
				edit.setVisibility(View.GONE);
			}

		}

		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delete(advic);
			}
		});
		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				update_adviceDialog(advic);
			}
		});


		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(advic.getUpdated()!=0)
				{
					if(advic.getUpdated()>0)
						removeFromList(advic,position,img,save);
					else
						save(advic,position,img,save);
				}
				else
				{
					Toast.makeText(context,"فشل في اتمام العملية",Toast.LENGTH_SHORT).show();
				}


			}
		});


		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(advic.getUpdated()!=0)
				{
					if(advic.getUpdated()>0)
						removeFromList(advic,position,img,save);
					else
						save(advic,position,img,save);
				}
				else
				{
					Toast.makeText(context,"فشل في اتمام العملية",Toast.LENGTH_SHORT).show();
				}
			}
		});



		adv.setText(advic.getAdv().getContent());
		date.setText(advic.getAdv().getA_date());


		
		return rowView;
		
	}




	void update_adviceDialog( final advice_wrapper adv){

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setTitle();
		TextView t=new ArabTextView(context);
		t.setTextSize(20);
		t.setGravity(Gravity.CENTER);
		t.setPadding(0, 5, 0, 9);
		t.setTextColor(context.getResources().getColor(R.color.myred));
		t.setText(" تعديل نصيحة");
		builder.setCustomTitle(t);

		// Set up the input
		final EditText input = new ArabEditText(context);
		input.setHint("النصيحة");


		input.setTextColor(context.getResources().getColor(R.color.myblue));

		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		input.setText(adv.getAdv().getContent());
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("تعديل", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(TextUtils.isEmpty(input.getText().toString()))
					input.setError(context.getString(R.string.error_field_required));
				else{

					adv.getAdv().setContent(input.getText().toString());
					//SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd ");
					//adv.getAdv().setA_date(df.format(new Date()).toString());
					edit(adv);
				}

			}
		});
		builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();

	}


	public  void edit(advice_wrapper adv){
		doctor d=((doctor)me);
		prgDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams req= adv.getAdv().getFullEntity();

		req.put("table","advice");
		req.put("token",d.getAccessToken());
		client.post(base.BASE_URL + "edit",req, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {

				try {
					JSONObject arg0 = new JSONObject(response);

					if(arg0.has("statue")){
						notifyDataSetChanged();
					}
					else
						Toast.makeText(getContext(),"حصل خطأ في عملية التعديل",Toast.LENGTH_SHORT).show();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				prgDialog.hide();
			}

			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {
				prgDialog.hide();
				Toast.makeText(getContext(), R.string.error,Toast.LENGTH_SHORT).show();

			}
		});


	}

	public  void delete(final advice_wrapper adv){
		prgDialog.show();

		doctor d=((doctor)me);

		RequestParams request=new RequestParams();
		request.put("token",d.getAccessToken());
		request.put("table", "advice");
		request.put("ID", adv.getAdv().getID()+"");

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


	public  void save(final advice_wrapper adv,final int pos,final ImageView img,final Button btn){
		prgDialog.show();
		user us=(user)me;
		save_advice dl=new save_advice();
		dl.setAdvice_ID(adv.getAdv().getID() + "");
		dl.setUser_ID(us.getID() + "");

		RequestParams request= dl.getEntity();
		request.put("token",us.getAccessToken());
		request.put("table", "save_advice");

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(base.BASE_URL + "add" , request, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {


				try {
					JSONObject obj=new JSONObject(response);
					img.setImageResource(android.R.drawable.ic_menu_delete);
					btn.setText("ازالة");
					adv.setUpdated(obj.getInt("id"));
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
	public  void removeFromList(final advice_wrapper adv,final int pos,final ImageView img,final Button btn){

		user us=(user)me;
		prgDialog.show();
		RequestParams request=new RequestParams();
		request.put("token",us.getAccessToken());
		request.put("table", "save_advice");
		request.put("ID", adv.getUpdated()+"");

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(base.BASE_URL + "delete" , request, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {


				try {
					img.setImageResource(android.R.drawable.ic_menu_save);
					btn.setText("حفظ");

					adv.setUpdated(-1);
					Toast.makeText(getContext(), "تمث عملية ازالة النصيحة من القائمة بنجاح", Toast.LENGTH_SHORT).show();


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




}
