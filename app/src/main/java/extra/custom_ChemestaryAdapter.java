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

import com.apps.scit.tabibihon.HospitalMap;
import com.apps.scit.tabibihon.MapsActivity;
import com.apps.scit.tabibihon.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.Serializable;
import java.util.ArrayList;

import entities.chemistry_wrapper;
import entities.user;

public class custom_ChemestaryAdapter extends ArrayAdapter<chemistry_wrapper> implements Serializable {


	Context context;

	ArrayList<chemistry_wrapper> list;


	user me;


	public custom_ChemestaryAdapter(Context context, ArrayList<chemistry_wrapper> objects) {
		super(context, R.layout.chimests_item, objects);
		list=objects;
		this.context=context;



	}

	
	


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//Log.d("CustomArrayAdapter", String.valueOf(position));
		LayoutInflater inflater = (LayoutInflater)  context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		final View rowView= inflater.inflate(R.layout.chimests_item, null, true);



		final chemistry_wrapper dl=list.get(position);

		((TextView)rowView.findViewById(R.id.ph)).setText(dl.getName());
		((TextView)rowView.findViewById(R.id.da)).setText(dl.getC_date());
		((TextView)rowView.findViewById(R.id.type)).setText(dl.getType().equals("0")?"نهارية":"ليلية");
		((TextView)rowView.findViewById(R.id.disc)).setText(dl.getLoc()+" : "+dl.getDescription());
		((TextView)rowView.findViewById(R.id.s_time)).setText("من : " + dl.getS_time()+
				((dl.getS_shift_time().equals("0"))?"صباحا":"مساءً"));
		((TextView)rowView.findViewById(R.id.e_time)).setText("الى : "+dl.getE_time()+
					((dl.getE_shift_time().equals("0"))?"صباحا":"مساءً"));


		((ImageButton)rowView.findViewById(R.id.loc)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(dl.getLocation().equals(""))
					Toast.makeText(getContext(),"لم يتم تحديد موقع الصيدلية",Toast.LENGTH_SHORT).show();
				else
				if(isGooglePlayServicesAvailable()) {
					Intent it=new Intent(getContext(), MapsActivity.class);
					it.putExtra("location",dl.getLocation());
					it.putExtra("type","1");
					getContext().startActivity(it);
				}
				else
				{
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
