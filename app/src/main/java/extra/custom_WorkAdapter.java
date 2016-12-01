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
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.Serializable;
import java.util.ArrayList;

import entities.base;
import entities.doctor_list_wrapper;
import entities.user;
import entities.work_time;

public class custom_WorkAdapter extends ArrayAdapter<work_time> implements Serializable {


	Context context;

	ArrayList<work_time> list;


	user me;


	public custom_WorkAdapter(Context context, ArrayList<work_time> objects) {
		super(context, R.layout.work_item, objects);
		list=objects;
		this.context=context;



	}

	
	


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//Log.d("CustomArrayAdapter", String.valueOf(position));
		LayoutInflater inflater = (LayoutInflater)  context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		final View rowView= inflater.inflate(R.layout.work_item, null, true);



		final work_time dl=list.get(position);

		final TextView work = (TextView) rowView.findViewById(R.id.work_time);

		String s=" من الساعة "+
				dl.getStart_time()+" "+(dl.getS_shift_time().equals("0")?"صباحاً":"مساءً")+
				" الى الساعة "+
				dl.getEnd_time()+" "+(dl.getE_shift_time().equals("0")?"صباحاً":"مساءً");

		work.setText(s);
		return rowView;
		
	}




}
