package extra;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.apps.scit.tabibihon.AdviceActivity;
import com.apps.scit.tabibihon.R;
import com.apps.scit.tabibihon.my_profile;
import com.apps.scit.tabibihon.profile;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginFragment;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import costumize.ArabEditText;
import costumize.ArabTextView;
import entities.advice;
import entities.advice_wrapper;
import entities.all_advice_wrapper;
import entities.base;
import entities.doctor;
import entities.save_advice;
import entities.user;

public class custom_AllAdviceListAdapter extends ArrayAdapter<all_advice_wrapper> implements Serializable {


	private final DisplayImageOptions options;
	private Activity activity;
	base use=null;






	public custom_AllAdviceListAdapter(Activity activity, int resource,
							  List<all_advice_wrapper> countries,base us) {
			super(activity, resource, countries);
			this.activity = activity;
			this.use = us;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.profile_icon2)
					.showImageOnFail(R.drawable.profile_icon2)
					.cacheInMemory(false)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(40))
					.build();

			if(!ImageLoader.getInstance().isInited())
				ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));




		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			LayoutInflater inflater =
					(LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			// If holder not exist then locate all view from UI file.

			// inflate UI from XML file
			convertView = inflater.inflate(R.layout.all_advice_item, parent, false);
			// get all UI view
			holder = new ViewHolder(convertView);
			// set tag for holder
			convertView.setTag(holder);


			final all_advice_wrapper item = getItem(position);

			holder.lin1.setText((item.getDoc_type().equals("0")?"د." :"ص.")+ item.getDoc());
			holder.lin2.setText(item.getAdv().getContent());
			holder.lin3.setText(item.getDoc_spec());
			final TextView ll = holder.likes;
			final Button lb = holder.like;
			if (item.getChecked().equals("1")) {
				holder.like.setEnabled(false);
				holder.like.setTextColor(Color.WHITE);
				holder.like.setBackgroundResource(R.drawable.card_state_pressed);
			} else {
				holder.like.setEnabled(true);
				//holder.like.setBackgroundResource(R.drawable.card_background);
			}

			if (use == null) {
				holder.like.setVisibility(View.GONE);
				holder.shareFacebookButton.setVisibility(View.GONE);
			}

			holder.like.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (use != null) {
						lb.setEnabled(false);
						lb.setBackgroundResource(R.drawable.card_state_pressed);
						lb.setTextColor(Color.WHITE);
						ll.setText((Integer.parseInt(item.getLike()) + 1) + "");
						ll.setTextColor(getContext().getResources().getColor(R.color.myblue));
						if (use instanceof doctor)
							like_adv(item.getAdv().getID());
						else if (use instanceof user) {
							save(item.getAdv().getID(), 0);
						}

					}

				}
			});






			holder.shareFacebookButton.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {

				  try {
					  String url = "";

					  if(item.getImg().equals(""))
						  url="";
					  else
					   if (item.getImg().startsWith("https"))
						  url = item.getImg();
					  else
						  url = base.BASE_URL + "125/" + item.getImg();


					  final String finalUrl = url;


					  ShareFeedContent feed=new ShareFeedContent.Builder()
							  .setLink("http://tabebakhoon.com/ar/#/advice/"+item.getAdv().getID())
							  .setMediaSource(finalUrl)
							  .setPicture(finalUrl)
							  .setLinkDescription(item.getAdv().getContent())
							  .setLinkName("د."+item.getDoc())
							  .setLinkCaption("طبيبك هوون")
							  .build();





					  AdviceActivity.shareDialog.show(feed);


				  }
				  catch (Exception c){

				  }

			  }
		  }
			);


			holder.ly.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent ii;
					if (use!=null && use instanceof doctor) {
						if (((doctor)use).getID() == item.getDoc_ID()) {
							ii = new Intent(getContext(), my_profile.class);
							ii.putExtra("search", "1");
						}
						else {
							ii = new Intent(getContext(), profile.class);
							ii.putExtra("id",item.getDoc_ID());
						}
						ii.putExtra("doctor", (doctor)use);
						ii.putExtra("type", item.getDoc_type());
					} else {
						ii = new Intent(getContext(), profile.class);
						if(use!=null)
							ii.putExtra("user", use);

						ii.putExtra("id", item.getDoc_ID());
						ii.putExtra("type", item.getDoc_type());
					}


					getContext().startActivity(ii);

				}
			});



				holder.likes.setText(item.getLike());
				ImageView iv = holder.img;
				String url = "";
				if(item.getImg().startsWith("https"))
				url=item.getImg();
				else
				url=base.BASE_URL+"125/"+item.getImg();
				if(!item.getImg().equals(""))
						ImageLoader.getInstance().displayImage(url, iv, options);

				return convertView;
			}





	private static class ViewHolder {
			private TextView lin1;
			private TextView lin2;
			private TextView lin3;
			private TextView likes;
			private Button like;
			private Button shareFacebookButton;
			private ImageView img;
			private LinearLayout ly;

			public ViewHolder(View v) {
				lin1 = (TextView) v.findViewById(R.id.line1);
				lin2 = (TextView) v.findViewById(R.id.line2);
				lin3 = (TextView) v.findViewById(R.id.line3);
				likes = (TextView) v.findViewById(R.id.likes);
				like = (Button) v.findViewById(R.id.lile_btn);
				shareFacebookButton = (Button) v.findViewById(R.id.shareFacebookButton);
				img = (ImageView) v.findViewById(R.id.doc_img);
				ly = (LinearLayout) v.findViewById(R.id.advice_item);
			}
		}


	public void like_adv(final int id){


		doctor us=(doctor)use;
		RequestParams request= new RequestParams();
		request.put("ID",id+"");
		request.put("doctor_ID",us.getID()+"");
		request.put("token",us.getAccessToken());
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(base.BASE_URL + "editAdviceLike" , request, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {



			}

			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {
				Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();


			}
		});


	}



	public  void save(final int id,final int count){

		user us=(user)use;
		save_advice dl=new save_advice();
		dl.setAdvice_ID(id + "");
		dl.setUser_ID(us.getID() + "");

		RequestParams request= dl.getEntity();
		request.put("token",us.getAccessToken());
		request.put("table", "save_advice");

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(base.BASE_URL + "add" , request, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {



					Toast.makeText(getContext(), "تمث  اضافة النصيحة الى القائمة بنجاح", Toast.LENGTH_SHORT).show();



			}

			@Override
			public void onFailure(int statusCode, Throwable error,
								  String content) {

				if(count!=3)
				{
					save(id,count+1);
				}
				else
				Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();


			}
		});


	}


	}