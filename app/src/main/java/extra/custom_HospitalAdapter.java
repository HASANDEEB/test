package extra;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.scit.tabibihon.MapsActivity;
import com.apps.scit.tabibihon.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.Serializable;
import java.util.ArrayList;

import entities.hospital;
import entities.hospital_wrapper;
import entities.user;
import entities.work_time;

public class custom_HospitalAdapter extends ArrayAdapter<hospital_wrapper> implements Serializable {


	Context context;

	ArrayList<hospital_wrapper> list;


	user me;


	public custom_HospitalAdapter(Context context, ArrayList<hospital_wrapper> objects) {
		super(context, R.layout.work_item, objects);
		list=objects;
		this.context=context;



	}

	
	


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//Log.d("CustomArrayAdapter", String.valueOf(position));
		LayoutInflater inflater = (LayoutInflater)  context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		final View rowView= inflater.inflate(R.layout.hospital_item, null, true);



		final hospital_wrapper dl=list.get(position);

		((TextView)rowView.findViewById(R.id.name)).setText(dl.getHos().getName());

			((TextView) rowView.findViewById(R.id.phone)).setText("ارضي : " + dl.getHos().getPhone());
			((TextView) rowView.findViewById(R.id.mobile)).setText("موبايل : " + dl.getHos().getMobile());
			((TextView) rowView.findViewById(R.id.info)).setText(dl.getHos().getDescription());
			((TextView) rowView.findViewById(R.id.type)).setText("نوع المشفى : "+dl.getHos().getType());


		((ImageButton)rowView.findViewById(R.id.loc)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if( dl.getHos().getLocation().equals(""))
					Toast.makeText(getContext(), "لم يتم تحديد موقع المشفى", Toast.LENGTH_SHORT).show();
				else
				if (isGooglePlayServicesAvailable()) {
					Intent it = new Intent(getContext(), MapsActivity.class);
					it.putExtra("location", dl.getHos().getLocation());
					it.putExtra("type", "2");
					getContext().startActivity(it);
				} else {
					Snackbar.make(v, "من فضلك قم بتحديث خدمات   Google Play",
							Snackbar.LENGTH_LONG).show();
				}

			}
		});


		return rowView;
		
	}



	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			return false;
		}
	}
}
