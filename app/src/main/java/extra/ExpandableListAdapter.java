package extra;

import java.util.HashMap;
        import java.util.List;

        import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.scit.tabibihon.MapsActivity;
import com.apps.scit.tabibihon.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import entities.chemistry_wrapper;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<chemistry_wrapper>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<chemistry_wrapper>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public chemistry_wrapper getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition, childPosition);
         View rowView=convertView;
        if (rowView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = infalInflater.inflate(R.layout.chimests_item, null);
        }


        final chemistry_wrapper dl=getChild(groupPosition, childPosition);

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

                if (dl.getLocation().equals(""))
                    Toast.makeText(_context, "لم يتم تحديد موقع الصيدلية", Toast.LENGTH_SHORT).show();
                else if (isGooglePlayServicesAvailable()) {
                    Intent it = new Intent(_context, MapsActivity.class);
                    it.putExtra("location", dl.getLocation());
                    it.putExtra("type", "1");
                    _context.startActivity(it);
                } else {
                    Snackbar.make(v, "من فضلك قم بتحديث خدمات   Google Play",
                            Snackbar.LENGTH_LONG).show();
                }

            }
        });
        return rowView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.chimests_group_item, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        //lblListHeader.setTypeface(null, Typeface.BOLD);

        if(headerTitle.equals("0"))
            headerTitle="الفترة النهارية";
        else
        headerTitle="الفترة الليلية";
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(_context);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            return false;
        }
    }


}