package com.apps.scit.tabibihon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import costumize.ArabButton;
import costumize.ArabEditText;
import costumize.ArabTextView;
import entities.advice_wrapper;
import entities.area;
import entities.base;
import entities.city;
import entities.clinic;
import entities.clinic_type;
import entities.country;
import entities.doctor;
import entities.hospital;
import entities.hospital_wrapper;
import entities.insurance_company;
import entities.service;
import entities.specialization;
import entities.work_time;
import extra.MyArrayAdapter;
import extra.TextInit;
import extra.custom_WorkAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class edit extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"المعلومات الشخصية","الحساب","العيادة","أوقات الدوام","شركات التأمين","المشافي"};
    int Numboftabs =6;


    private  static doctor doc=null;
    private  static area ar=null;
    private  static clinic cl=null;
    static boolean first =true,sec=true;

    private static ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

try {

    if(getIntent().hasExtra("doctor"))
    {

        doc=(doctor)getIntent().getSerializableExtra("doctor");
        ar=(area)getIntent().getSerializableExtra("area");

        if(doc.getType().equals("1")) {
            Titles[2] = "الصيدلية";
            Numboftabs=5;
        }

    }
    else finish();

    prgDialog = new ProgressDialog(this);
    // Set Progress Dialog Text
    prgDialog.setMessage("Please wait...");
    // Set Cancelable as False
    prgDialog.setCancelable(false);





    // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


}
catch (Exception c)
{
    Toast.makeText(this,c.getMessage(),Toast.LENGTH_LONG).show();
}

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public  static class doctorFragment extends Fragment {

        EditText university,info,email,phone,mobile,fname,mname,lname;
        Spinner special;
        ArrayList<specialization> list=new ArrayList<specialization>();
        ArrayAdapter<specialization> adapter;
        private ProgressDialog prgDialog;
        public doctorFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate( R.layout.edit_doctor_info, container, false);

            prgDialog = new ProgressDialog(getContext());
            // Set Progress Dialog Text
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);

            university=(EditText)rootView.findViewById(R.id.university);
            info=(EditText)rootView.findViewById(R.id.additional_info);
            email=(EditText)rootView.findViewById(R.id.email);
            phone=(EditText)rootView.findViewById(R.id.phone);
            mobile=(EditText)rootView.findViewById(R.id.mobile);
            fname=(EditText)rootView.findViewById(R.id.fname);
            mname=(EditText)rootView.findViewById(R.id.mname);
            lname=(EditText)rootView.findViewById(R.id.lname);
            special=(Spinner)rootView.findViewById(R.id.doc_spec);


            adapter = new MyArrayAdapter<specialization>(getContext(),R.layout.spinner_item, list);
            special.setAdapter(adapter);

            if(doc.getType().equals("0"))
            getSpeciality(0);
            else {
                rootView.findViewById(R.id.speciality).setVisibility(View.GONE);
                setDoctorData(-1);

            }



            ((Button)rootView.findViewById(R.id.doc_edit)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editDoc();

                        }
                    }
            );



            return rootView;
        }

        private void getSpeciality(final int count) {
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();


            client.post(base.BASE_URL + "getSpecialization", new RequestParams(), new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {


                    try {
                        ArrayList<specialization> resAR = specialization.readOBJArray(response);
                        list.clear();
                        int i=-1,j=0;
                        for (specialization help : resAR) {
                            list.add(help);
                            if((help.getID()+"").equals(doc.getSpecialization_ID()))
                                i=j;
                            j++;
                        }
                        adapter.notifyDataSetChanged();
                        setDoctorData(i);

                    } catch (Exception e) {
                        Toast.makeText(getContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                    }

                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count !=2){
                        getSpeciality(count+1);
                    }
                    else {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        prgDialog.hide();
                    }

                }
            });
        }


        void editDoc(){


            university.setError(null);
            info.setError(null);
            email.setError(null);
            phone.setError(null);
            mobile.setError(null);
            fname.setError(null);
            mname.setError(null);
            lname.setError(null);



            String uni = university.getText().toString();
            String inf = info.getText().toString();
            String em = email.getText().toString();
            String ph = phone.getText().toString();
            String mob = mobile.getText().toString();
            String fn = fname.getText().toString();
            String mn = mname.getText().toString();
            String ln = lname.getText().toString();



            boolean cancel = false;
            View focusView = null;



            if (TextUtils.isEmpty(fn)) {
                fname.setError(getString(R.string.error_field_required));
                focusView = fname;
                cancel = true;
            }


            if (TextUtils.isEmpty(ln)) {
                lname.setError(getString(R.string.error_field_required));
                focusView = lname;
                cancel = true;
            }


            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {

                if(doc.getType().equals("0")&&special.getSelectedItemPosition()==-1)
                    Toast.makeText(getContext(),"من فضلك اختر تختصاص",Toast.LENGTH_SHORT).show();
                else {
                    doc.setUniversity(TextInit.check(uni));
                    doc.setAdditional_info(TextInit.check(inf));
                    doc.setEmail(em);
                    doc.setMobile(mob);
                    doc.setPhone(ph);
                    doc.setFname(TextInit.check(fn));
                    doc.setMname(TextInit.check(mn));
                    doc.setLname(TextInit.check(ln));
                    if(doc.getType().equals("0"))
                        doc.setSpecialization_ID(list.get(special.getSelectedItemPosition()).getID()+"");

                    updateAccount(0);
                }
            }



        }




        void updateAccount(final int count){
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams req= doc.getEntity();
            req.put("ID",doc.getID()+"");
            req.put("table","doctor");
            req.put("token",doc.getAccessToken());
            client.post(base.BASE_URL + "edit",req, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {
                        JSONObject arg0 = new JSONObject(response);

                        if(arg0.has("statue")){
                            ((Activity)getContext()).finish();
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
                    if(count!=5)
                    {
                        updateAccount(count+1);
                    }
                    else {
                        prgDialog.hide();
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        void setDoctorData( int i){

            fname.setText(doc.getFname());
            mname.setText(doc.getMname());
            lname.setText(doc.getLname());
            university.setText(doc.getUniversity());
            info.setText(doc.getAdditional_info());
            mobile.setText(doc.getMobile());
            phone.setText(doc.getPhone());
            email.setText(doc.getEmail());
            if(i!=-1)
                special.setSelection(i);

        }


    }



    public  static class passwordFragment extends Fragment {

        EditText password,repassword;
        public passwordFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.edit_doctor_password, container, false);


            password=(EditText)rootView.findViewById(R.id.password);
            repassword=(EditText)rootView.findViewById(R.id.repassword);

            ((Button)rootView.findViewById(R.id.doc_pass_edit)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editDoc();

                        }
                    }
            );



            return rootView;
        }

        void editDoc(){


            password.setError(null);
            repassword.setError(null);




            String pass = password.getText().toString();
            String repass = repassword.getText().toString();




            boolean cancel = false;
            View focusView = null;



            if (TextUtils.isEmpty(pass)) {
                password.setError(getString(R.string.error_field_required));
                focusView = password;
                cancel = true;
            }
            else


            if (TextUtils.isEmpty(repass)) {
                repassword.setError(getString(R.string.error_field_required));
                focusView = repassword;
                cancel = true;
            }
            else

            if (!pass.equals(repass)) {
                repassword.setError("كلمة السر غير متطابقة");
                focusView = repassword;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {



                updateAccount(pass,0);
            }



        }




        void updateAccount(final String pass,final int count){
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams req= doc.getEntity();
            req.put("doctor_ID", doc.getID() + "");
            req.put("password", pass);
            req.put("token", doc.getAccessToken());
            client.post(base.BASE_URL + "editDoctorPassword", req, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {
                        JSONObject arg0 = new JSONObject(response);

                        if (arg0.has("statue")) {

                            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                            if (prefs.getBoolean("account", false)) {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("PassWord", pass);
                                editor.commit();

                            }


                            ((Activity) getContext()).finish();
                        } else
                            Toast.makeText(getContext(), "حصل خطأ في عملية التعديل", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count!=5){
                        updateAccount(pass,count+1);
                    }else {
                        prgDialog.hide();
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }




    }




    public  static  class clinicFragment extends Fragment {


        private static final int PLACE_PICKER_REQUEST = 187;
        EditText name,street,disc,phone,mobile,email,website;
        Spinner citys,countries,areas;
        private Button bLocation;
        private Place place;
        ArrayList<country> list_country=new ArrayList<country>();
        ArrayAdapter<country> adapter_country;

        ArrayList<city> list_city=new ArrayList<city>();
        ArrayAdapter<city> adapter_city;

        boolean ready_city=false;
        boolean ready_area=false;


        ArrayList<entities.area> list_area=new ArrayList<entities.area>();
        ArrayList<area> all_area=new ArrayList<area>();
        ArrayAdapter<entities.area> adapter_area;

        ArrayList<clinic_type> list_type=new ArrayList<clinic_type>();
        ArrayAdapter<clinic_type> adapter_type;



        public clinicFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate( R.layout.edit_clinic_info, container, false);


            name=(EditText)rootView.findViewById(R.id.c_name);
            street=(EditText)rootView.findViewById(R.id.c_street);
            disc=(EditText)rootView.findViewById(R.id.c_disc);
            email=(EditText)rootView.findViewById(R.id.c_email);
            phone=(EditText)rootView.findViewById(R.id.c_phone);
            mobile=(EditText)rootView.findViewById(R.id.c_mobile);
            website=(EditText)rootView.findViewById(R.id.c_website);
            citys=(Spinner)rootView.findViewById(R.id.clinic_city);
           // countries=(Spinner)rootView.findViewById(R.id.clinic_country);
            areas=(Spinner)rootView.findViewById(R.id.clinic_area);

           // View v=getContext.get(android.R.layout.simple_spinner_dropdown_item);

            adapter_city = new MyArrayAdapter<city>(getContext(),R.layout.spinner_item, list_city);

            citys.setAdapter(adapter_city);

          //  adapter_area = new ArrayAdapter<entities.area>(getContext(),android.R.layout.simple_spinner_dropdown_item, list_area);
            adapter_area = new MyArrayAdapter<entities.area>(getContext(),R.layout.spinner_item, list_area);
            areas.setAdapter(adapter_area);


            if(doc.getType().equals("0"))
                rootView.findViewById(R.id.clinic_name).setVisibility(View.GONE);




            ((Button)rootView.findViewById(R.id.clinic_edit)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editClinic();
                        }
                    }
            );


            bLocation = (Button) rootView.findViewById(R.id.b_location);
            bLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        Snackbar.make(v, "من فضلك قم بتحديث خدمات   Google Play",
                                Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        Snackbar.make(v, "من فضلك قم بتحديث خدمات   Google Play",
                                Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
            getClinic(doc.getID() + "",0);

            //getCity();


            return rootView;
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    place = PlacePicker.getPlace(data, getContext());
                    bLocation.setText(place.getName());


                }
            }
        }



//        private void getArea(final int selectedCity) {
//
//            AsyncHttpClient client = new AsyncHttpClient();
//
//
//            RequestParams req=new RequestParams();
//            client.post(base.BASE_URL + "getAllArea", req, new AsyncHttpResponseHandler() {
//
//                @Override
//                public void onSuccess(String response) {
//
//
//                    try {
//                        ArrayList<area> resAR = area.readOBJArray(response);
//                       all_area.clear();
//
//                        for (area help : resAR) {
//                            all_area.add(help);
//                        }
//
//                        adapter_city.notifyDataSetChanged();
//                        ready_city=true;
//                        citys.setSelection(selectedCity);
//
//
//                        String city = list_city.get(citys.getSelectedItemPosition()).getID() + "";
//                        list_area.clear();
//                        int i = 0, j = 0;
//                        for (area ars : all_area) {
//                            if (ars.getCity_ID().equals(city)) {
//                                list_area.add(ars);
//                                if (sec) {
//                                    if (ar.getID() == ars.getID())
//                                        i = j;
//                                    j++;
//                                }
//                            }
//
//                        }
//
//                        adapter_area.notifyDataSetChanged();
//
//                        areas.setSelection(i);
//
//                        setsSpinnerListener();
//
//
//
//
//
//                    } catch (Exception e) {
//                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Throwable error,
//                                      String content) {
//                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
//
//
//                }
//            });
//
//
//
//        }

        private void setsSpinnerListener(){

            citys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    String city = list_city.get(position).getID() + "";
                    list_area.clear();
                    int i = 0, j = 0;
                    for (area ars : all_area) {
                        if (ars.getCity_ID().equals(city)) {
                            list_area.add(ars);

                        }

                    }

                    adapter_area.notifyDataSetChanged();

              ;


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });

        }
//        private void getCity() {
//
//            AsyncHttpClient client = new AsyncHttpClient();
//            RequestParams req=new RequestParams();
//
//
//            client.post(base.BASE_URL+"getCity", req, new AsyncHttpResponseHandler() {
//
//                @Override
//                public void onSuccess(String response) {
//
//
//                    try {
//                        ArrayList<city> resAR = city.readOBJArray(response);
//                        list_city.clear();
//                        int i=-1;
//                        int j=0;
//                        for (city help : resAR) {
//                            list_city.add(help);
//                            if(ar.getCity_ID().equals(help.getID()+""))
//                                i=j;
//
//                            j++;
//                        }
//
//                        getArea(i);
//
//
//
//                    } catch (Exception e) {
//                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Throwable error,
//                                      String content) {
//                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
//
//
//                }
//            });
//
//
//
//        }



        private void getClinic(final String id,final int count) {
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams req=new RequestParams();
            req.put("token",doc.getAccessToken());
            req.put("ID",id);

            client.post(base.BASE_URL + "get_full_clinic_info", req, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {



                    try {
                        JSONObject obj=new JSONObject(response);
                        cl = clinic.readSingleOBJ(obj.getJSONObject("clinic"));
                        setClinicDate();



                        ArrayList<city> resAR = city.readOBJArray(obj.getJSONArray("city").toString());
                        list_city.clear();
                        int i=-1;
                        int j=0;
                        for (city help : resAR) {
                            list_city.add(help);
                            if(ar.getCity_ID().equals(help.getID()+"")) {
                                i = j;

                            }

                            j++;
                        }



                        ArrayList<area> resARea = area.readOBJArray(obj.getJSONArray("area").toString());
                        all_area.clear();

                        for (area help : resARea) {
                            all_area.add(help);
                        }

                        adapter_city.notifyDataSetChanged();
                        ready_city=true;
                        citys.setSelection(i);


                        String city = list_city.get(citys.getSelectedItemPosition()).getID() + "";
                        list_area.clear();
                        int ii = 0, jj = 0;
                        for (area ars : all_area) {
                            if (ars.getCity_ID().equals(city)) {
                                list_area.add(ars);

                                    if (ar.getID() == ars.getID())
                                        ii = jj;
                                    jj++;

                            }

                        }

                        adapter_area.notifyDataSetChanged();

                        areas.setSelection(ii);

                        setsSpinnerListener();



                    } catch (Exception e) {
                        Toast.makeText(getContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                        prgDialog.hide();
                    }


                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count!=5)
                    {
                        getClinic(id,count+1);
                    }
                    else {
                        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
                        prgDialog.hide();
                    }

                }
            });



        }

        void editClinic(){
            street.setError(null);

            String str = street.getText().toString();
            String nam = name.getText().toString();
            String dis = disc.getText().toString();
            String ph = phone.getText().toString();
            String mob = mobile.getText().toString();
            String em = email.getText().toString();
            String web = website.getText().toString();



            boolean cancel = false;
            View focusView = null;



            // Check for a valid mobile address.
            if (TextUtils.isEmpty(str)) {
                street.setError(getString(R.string.error_field_required));
                focusView = street;
                cancel = true;
            }
            if (doc.getType().equals("1")&& TextUtils.isEmpty(nam)) {
                name.setError(getString(R.string.error_field_required));
                focusView = name;
                cancel = true;
            }



            if (cancel) {
                // There was an error; don't attempt signup and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {


                if(areas.getSelectedItemPosition()==-1){
                    Toast.makeText(getContext()," من فضلك اختر المنطقة",Toast.LENGTH_SHORT).show();
                }

                else {



                    cl.setPhone(ph);
                    cl.setMobile(mob);
                    cl.setWebsite(TextInit.check(web));
                    cl.setEmail(em);
                    cl.setName(nam);

                    cl.setDescription(TextInit.check(dis));
                    cl.setStreet(TextInit.check(str));
                  //  cl.setCity_id(list_city.get(citys.getSelectedItemPosition()).getID() + "");

                    if(place!=null)
                       cl.setLocation(place.getLatLng().toString());
                    cl.setArea_ID(list_area.get(areas.getSelectedItemPosition()).getID()+"");


                    updateClinic(0);

                }
            }




        }

        void updateClinic(final int count){
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams req= cl.getEntity();
            req.remove("type_id");
            req.put("ID",cl.getID()+"");
            req.put("table","clinic");
            req.put("token",doc.getAccessToken());
            client.post(base.BASE_URL + "edit",req, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {
                        JSONObject arg0 = new JSONObject(response);

                        if(arg0.has("statue")){
                            ((Activity)getContext()).finish();
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
                    if(count!=5)
                    {
                        updateClinic(count+1);
                    }else {
                        prgDialog.hide();
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        void setClinicDate(){

            street.setText(cl.getStreet());
            name.setText(cl.getName());
            disc.setText(cl.getDescription());
            phone.setText(cl.getPhone());
            mobile.setText(cl.getMobile());
            email.setText(cl.getEmail());
            website.setText(cl.getWebsite());
            if(!TextUtils.isEmpty(cl.getLocation())){
                bLocation.setText(cl.getLocation());
            }
            prgDialog.hide();
        }

    }





    public  static class companyFragment extends Fragment {

        private Spinner company_spinner,company_type_spinner;
        private ListView company_list;
        ArrayList<insurance_company> list_company=new ArrayList<insurance_company>();
        ArrayList<insurance_company> list_all_company=new ArrayList<insurance_company>();

        ArrayList<String> list_company_type=new ArrayList<String>();
        ArrayList<doctor_company> list_company2=new ArrayList<doctor_company>();
        ArrayAdapter<insurance_company> adapter_company;
        ArrayAdapter<doctor_company> adapter_list_company;
        ArrayAdapter<String> adapter_list_company_type;
        LinearLayout result;
        public companyFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.edit_doctor_company, container, false);

            result=(LinearLayout)rootView.findViewById(R.id.res);


            company_list=(ListView)rootView.findViewById(R.id.listView);
           // company_type_list=(ListView)rootView.findViewById(R.id.listView);
            adapter_company = new ArrayAdapter<insurance_company>(getContext(),R.layout.spinner_item, list_company);
            adapter_list_company = new ArrayAdapter<doctor_company>(getContext(),R.layout.spinner_item, list_company2);
            adapter_list_company_type = new ArrayAdapter<String>(getContext(),R.layout.spinner_item, list_company_type);

            company_list.setAdapter(adapter_list_company);

            getCompany( 0);

            ((Button)rootView.findViewById(R.id.company_add)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LayoutInflater inflater2 = (LayoutInflater)  getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                            final View rowView= inflater2.inflate(R.layout.add_company, null, true);
                            company_spinner=(Spinner)rowView.findViewById(R.id.spinner);
                            company_type_spinner=(Spinner)rowView.findViewById(R.id.spinnertype);
                            company_spinner.setAdapter(adapter_company);
                            company_type_spinner.setAdapter(adapter_list_company_type);

                            company_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    list_company.clear();
                                    String ty = position+"";
                                    for (insurance_company c : list_all_company) {
                                        if (c.getType().equals(ty))
                                            list_company.add(c);
                                    }
                                    adapter_company.notifyDataSetChanged();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            list_company_type.clear();
                            list_company_type.add("شركات القطاع العام");
                            list_company_type.add("شركات التأمين ");
                            adapter_list_company_type.notifyDataSetChanged();

                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                            // builder.setTitle();
                            TextView t=new ArabTextView(getContext());
                            t.setTextSize(15);
                            t.setGravity(Gravity.CENTER);
                            t.setPadding(0, 5, 0, 9);
                            t.setTextColor(getContext().getResources().getColor(R.color.myred));
                            t.setText("اضافة شركة تأمين ");
                            builder.setCustomTitle(t);
                            builder.setView(rowView);
                            builder.setPositiveButton("اضافة", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (company_spinner.getSelectedItemPosition() != -1) {
                                        insurance_company cmp = list_company.get(company_spinner.getSelectedItemPosition());

                                        boolean f = false;
                                        for (doctor_company dc : list_company2) {
                                            if (dc.name.compareTo(cmp.getName()) == 0) {
                                                Toast.makeText(getContext(), "شركة التأمين مضافة مسبقا", Toast.LENGTH_LONG).show();
                                                f = true;
                                                break;
                                            }
                                        }

                                        if (!f)
                                            addCmp(cmp.getID(), cmp.getName(),0);
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
                    }
            );


            company_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                    //   view.setBackgroundColor(Color.argb(200, 255, 10, 10));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("حذف شركة");
                    builder.setMessage("هل انت متأكد انك تريد حذف هذه الشركة "); // Want to enable?
                    builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            deleteCmp(list_company2.get(position),0);
                            //  view.setBackgroundColor(Color.argb(255, 255, 255, 255));

                        }
                    });
                    builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //  view.setBackgroundColor(Color.argb(255, 255, 255, 255));

                        }
                    });
                    builder.create().show();


                }
            });


            return rootView;
        }




        void getCompany(final int count){
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams  req=new RequestParams();
            req.put("ID", doc.getID() + "");
            client.post(base.BASE_URL + "getCompany", req, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);
                        ArrayList<insurance_company> resAR3 = insurance_company.readOBJArray(obj.getJSONArray("company").toString());
                        list_all_company.clear();

                        for (insurance_company help : resAR3) {
                            list_all_company.add(help);

                        }


                        list_company2.clear();
                        JSONArray array = obj.getJSONArray("link");



                        for (int i = 0; i < array.length(); i++) {
                            list_company2.add(new doctor_company(array.getJSONObject(i).getInt("ID"), array.getJSONObject(i).getString("name")));
                        }
                        if(list_company2.size()==0)
                            result.setVisibility(View.GONE);
                        else
                            result.setVisibility(View.VISIBLE);

                       // adapter_company.notifyDataSetChanged();
                        adapter_list_company.notifyDataSetChanged();




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {

                    if(count!=5){
                     getCompany(count+1);
                    }
                    else
                    {
                        prgDialog.hide();
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }


        void addCmp(final int id, final String name,final int count)
        {
            prgDialog.show();
            RequestParams request= new RequestParams();
            request.put("token", doc.getAccessToken());
            request.put("table", "doctor_company");
            request.put("doctor_ID", doc.getID()+"");
            request.put("insurance_company_ID", id+"");


            AsyncHttpClient client = new AsyncHttpClient();
            client.post(base.BASE_URL + "add", request, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {


                    try {
                        JSONObject obj=new JSONObject(response);
                        if(obj.has("id"))
                        {
                            list_company2.add(new doctor_company(obj.getInt("id"),name));

                            if(list_company2.size()==0)
                                result.setVisibility(View.GONE);
                            else
                                result.setVisibility(View.VISIBLE);

                            adapter_list_company.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "خطأ في عملية الاضافة", Toast.LENGTH_SHORT).show();
                        }



                    } catch (Exception e) {
                        Toast.makeText(getContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                    }

                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count !=2){
                        addCmp(id,name,count+1);
                    }
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();

                }
            });

        }


        void deleteCmp(final doctor_company id,final int count){


            prgDialog.show();


            RequestParams request=new RequestParams();
            request.put("token", doc.getAccessToken());
            request.put("table", "doctor_company");
            request.put("ID", id.ID+"");

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(base.BASE_URL + "delete" , request, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {


                    try {
                        list_company2.remove(id);
                        if(list_company2.size()==0)
                            result.setVisibility(View.GONE);
                        else
                            result.setVisibility(View.VISIBLE);
                        adapter_list_company.notifyDataSetChanged();



                    } catch (Exception e) {
                        Toast.makeText(getContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                    }

                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count!=2){
                        deleteCmp(id,count+1);
                    }
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();

                }
            });
        }

        class doctor_company{

            int ID;
            String name;


            public doctor_company(int ID, String name) {
                this.ID = ID;
                this.name = name;
            }


            @Override
            public String toString() {
                return name ;
            }
        }


    }





    public  static class workFragment extends Fragment {

        //Spinner start,end,shift;
        ArrayList<work_time> worklist=new ArrayList<work_time>();
        custom_WorkAdapter workadapter;
        ListView works;
        LinearLayout result;
        public workFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.edit_work_time, container, false);


//            start=(Spinner)rootView.findViewById(R.id.spinner_start);
//            end=(Spinner)rootView.findViewById(R.id.spinner_end);
//            shift=(Spinner)rootView.findViewById(R.id.spinner_shift);
            works=(ListView)rootView.findViewById(R.id.listView);
            result=(LinearLayout)rootView.findViewById(R.id.res);

            workadapter=new custom_WorkAdapter(getContext(),worklist);
            works.setAdapter(workadapter);



            rootView.findViewById(R.id.work_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                    // builder.setTitle();
                    TextView t=new ArabTextView(getContext());
                    t.setTextSize(15);
                    t.setGravity(Gravity.CENTER);
                    t.setPadding(0, 5, 0, 9);
                    t.setTextColor(getContext().getResources().getColor(R.color.myred));
                    t.setText("اضافة توقيت دوام");
                    builder.setCustomTitle(t);

                    LayoutInflater inflater = (LayoutInflater)  getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    final View rowView= inflater.inflate(R.layout.add_work_time, null, true);
                    builder.setView(rowView);


                    final NumberPicker start1=(NumberPicker)rowView.findViewById(R.id.hfrom);
                    final NumberPicker start2=(NumberPicker)rowView.findViewById(R.id.mfrom);
                    final NumberPicker start3=(NumberPicker)rowView.findViewById(R.id.sshift);
                    final NumberPicker end1=(NumberPicker)rowView.findViewById(R.id.hto);
                    final NumberPicker end2=(NumberPicker)rowView.findViewById(R.id.mto);
                    final NumberPicker end3=(NumberPicker)rowView.findViewById(R.id.eshift);
                    start2.setDisplayedValues(null);
                    end2.setDisplayedValues(null);
                    start1.setDisplayedValues(null);
                    start3.setDisplayedValues(null);
                    end1.setDisplayedValues(null);
                    end3.setDisplayedValues(null);

                    final Spinner shifts=(Spinner)rowView.findViewById(R.id.spinner_shift);



                    // Set up the buttons
                    builder.setPositiveButton("اضافة", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String[]display1=start1.getDisplayedValues();
                            String[]display2=start2.getDisplayedValues();


                            work_time w = new work_time();
                            w.setS_shift_time(start3.getValue()+"");
                            w.setE_shift_time(end3.getValue()+"");
                            w.setStart_time(display1[ start1.getValue()]+ ":"+display2[start2.getValue()]);
                            w.setEnd_time( display1[end1.getValue()]+ ":"+ display2[end2.getValue()]);
                            w.setClinic_ID(doc.getClinic_ID());
                            w.setDescription("");
                            addWork(w, 0);

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
            });


            works.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                    //   view.setBackgroundColor(Color.argb(200, 255, 10, 10));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("حذف توقيت ");
                    builder.setMessage("هل انت متأكد انك تريد حذف هذا التوقيت "); // Want to enable?
                    builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            deleteWork(worklist.get(position), 0);
                            //      view.setBackgroundColor(Color.argb(255, 255, 255, 255));

                        }
                    });
                    builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //       view.setBackgroundColor(Color.argb(255, 255, 255, 255));

                        }
                    });
                    builder.create().show();


                }
            });

            getWork(0);

            return rootView;
        }




        void getWork(final int count){
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams  req=new RequestParams();
            req.put("ID", doc.getClinic_ID() + "");
            client.post(base.BASE_URL + "get_work_time", req, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {


                        ArrayList<work_time> arr=work_time.readOBJArray(response);
                        for (work_time wr : arr){
                            worklist.add(wr);
                        }

                        if(worklist.size()==0)
                            result.setVisibility(View.GONE);
                        else
                            result.setVisibility(View.VISIBLE);

                        workadapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count!=5){
                        getWork(count + 1);
                    }
                    else
                    {
                        prgDialog.hide();
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


        void addWork(final work_time w,final int count)
        {
            prgDialog.show();
            RequestParams request= w.getEntity();
            request.put("token", doc.getAccessToken());
            request.put("table", "work_time");



            AsyncHttpClient client = new AsyncHttpClient();
            client.post(base.BASE_URL + "add", request, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {


                    try {
                        JSONObject obj=new JSONObject(response);
                        if(obj.has("id"))
                        {
                            w.setID(obj.getInt("id"));
                            worklist.add(w);
                            if(worklist.size()==0)
                                result.setVisibility(View.GONE);
                            else
                                result.setVisibility(View.VISIBLE);
                            workadapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "خطأ في عملية الاضافة", Toast.LENGTH_SHORT).show();
                        }



                    } catch (Exception e) {
                        Toast.makeText(getContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                    }

                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count!=2){
                        addWork(w,count+1);
                    }else {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        prgDialog.hide();
                    }

                }
            });

        }


        void deleteWork(final work_time id,final int count){


            prgDialog.show();


            RequestParams request=new RequestParams();
            request.put("token", doc.getAccessToken());
            request.put("table", "work_time");
            request.put("ID", id.getID() + "");

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(base.BASE_URL + "delete", request, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {


                    try {
                        worklist.remove(id);
                        if(worklist.size()==0)
                            result.setVisibility(View.GONE);
                        else
                            result.setVisibility(View.VISIBLE);
                        workadapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        Toast.makeText(getContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                    }

                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count!=2){
                        deleteWork(id,count+1);
                    }
                    else {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        prgDialog.hide();
                    }

                }
            });
        }


    }






    public  static class hospitalFragment extends Fragment {

        private Spinner hospital_spinner,city_spinner;
        private ListView hospital_list_view;
        ArrayList<hospital> list_hospital=new ArrayList<hospital>();
        ArrayList<hospital> list_all_hospital=new ArrayList<hospital>();
        ArrayList<doctor_hospital> list_hospital2=new ArrayList<doctor_hospital>();
        ArrayAdapter<hospital> adapter_hospital;
        ArrayAdapter<doctor_hospital> adapter_list_hospital;
        ArrayList<city> list_city=new ArrayList<city>();
        ArrayAdapter<city> adapter_city;
        LinearLayout result;
        public hospitalFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.edit_doctor_hospital, container, false);

            result=(LinearLayout)rootView.findViewById(R.id.res);


            hospital_list_view=(ListView)rootView.findViewById(R.id.listView);

            adapter_hospital = new ArrayAdapter<hospital>(getContext(),R.layout.spinner_item, list_hospital);
            adapter_list_hospital = new ArrayAdapter<doctor_hospital>(getContext(),R.layout.spinner_item, list_hospital2);
            adapter_city=new ArrayAdapter<city>(getContext(),R.layout.spinner_item, list_city);


            hospital_list_view.setAdapter(adapter_list_hospital);

            getHospital(0);

            ((Button)rootView.findViewById(R.id.hospital_add)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            LayoutInflater inflater2 = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                            final View rowView = inflater2.inflate(R.layout.add_hospital, null, true);
                            hospital_spinner = (Spinner) rowView.findViewById(R.id.hospital_spinner);
                            city_spinner = (Spinner) rowView.findViewById(R.id.city_spinner);
                            city_spinner.setAdapter(adapter_city);
                            hospital_spinner.setAdapter(adapter_hospital);


                            /////////////////////////////
                            city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    list_hospital.clear();
                                    for (hospital h : list_all_hospital) {

                                        if (h.getCity_ID().equals(list_city.get(position).getID() + "")) {
                                            list_hospital.add(h);
                                        }

                                    }
                                    adapter_hospital.notifyDataSetChanged();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                            ////////////////////////////

                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                            // builder.setTitle();
                            TextView t = new ArabTextView(getContext());
                            t.setTextSize(15);
                            t.setGravity(Gravity.CENTER);
                            t.setPadding(0, 5, 0, 9);
                            t.setTextColor(getContext().getResources().getColor(R.color.myred));
                            t.setText("اضافة مشفى  ");
                            builder.setCustomTitle(t);
                            builder.setView(rowView);
                            builder.setPositiveButton("اضافة", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (hospital_spinner.getSelectedItemPosition() != -1) {
                                        hospital cmp = list_hospital.get(hospital_spinner.getSelectedItemPosition());

                                        boolean f = false;
                                        for (doctor_hospital dc : list_hospital2) {
                                            if (dc.name.compareTo(cmp.getName()) == 0) {
                                                Toast.makeText(getContext(), " المشفى  مضافة مسبقا", Toast.LENGTH_LONG).show();
                                                f = true;
                                                break;
                                            }
                                        }

                                        if (!f)
                                            addHost(cmp.getID(), cmp.getName(), 0);
                                    } else {
                                        Toast.makeText(getContext(), "من فضلك اختر مشفى", Toast.LENGTH_SHORT).show();
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
////////


                        }
                    }
            );


            hospital_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                    //   view.setBackgroundColor(Color.argb(200, 255, 10, 10));

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("حذف المشفى");
                    builder.setMessage("هل انت متأكد انك تريد حذف هذه المشفى "); // Want to enable?
                    builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            deleteHos(list_hospital2.get(position), 0);
                            //  view.setBackgroundColor(Color.argb(255, 255, 255, 255));

                        }
                    });
                    builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //  view.setBackgroundColor(Color.argb(255, 255, 255, 255));

                        }
                    });
                    builder.create().show();


                }
            });





            return rootView;
        }




        void getHospital(final int count){
            prgDialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams  req=new RequestParams();
            req.put("ID", doc.getID() + "");
            client.post(base.BASE_URL + "get_hospital", req, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);
                        ArrayList<hospital> resAR3 = hospital.readOBJArray(obj.getJSONArray("hospitals").toString());
                        list_all_hospital.clear();

                        for (hospital help : resAR3) {
                            list_all_hospital.add(help);

                        }
                        ArrayList<city> resAR = city.readOBJArray(obj.getJSONArray("cities").toString());
                        list_city.clear();
                        for (city help : resAR) {
                            list_city.add(help);
                            //
                        }


                        list_hospital2.clear();
                        JSONArray array = obj.getJSONArray("link");
                        for (int i = 0; i < array.length(); i++) {
                            list_hospital2.add(new doctor_hospital(array.getJSONObject(i).getInt("ID"), array.getJSONObject(i).getString("name")));
                        }
                        if(list_hospital2.size()==0)
                            result.setVisibility(View.GONE);
                        else
                            result.setVisibility(View.VISIBLE);

                        adapter_city.notifyDataSetChanged();
                        adapter_list_hospital.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {

                    if(count!=5){
                        getHospital(count + 1);
                    }
                    else
                    {
                        prgDialog.hide();
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }


        void addHost(final int id, final String name,final int count)
        {
            prgDialog.show();
            RequestParams request= new RequestParams();
            request.put("token", doc.getAccessToken());
            request.put("table", "doctor_hospital");
            request.put("doctor_ID", doc.getID()+"");
            request.put("hospital_ID", id+"");


            AsyncHttpClient client = new AsyncHttpClient();
            client.post(base.BASE_URL + "add", request, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {


                    try {
                        JSONObject obj=new JSONObject(response);
                        if(obj.has("id"))
                        {
                            list_hospital2.add(new doctor_hospital(obj.getInt("id"),name));

                            if(list_hospital2.size()==0)
                                result.setVisibility(View.GONE);
                            else
                                result.setVisibility(View.VISIBLE);

                            adapter_list_hospital.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "خطأ في عملية الاضافة", Toast.LENGTH_SHORT).show();
                        }



                    } catch (Exception e) {
                        Toast.makeText(getContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                    }

                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count!=2){
                        addHost(id,name,count+1);
                    }else {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        prgDialog.hide();
                    }

                }
            });

        }


        void deleteHos(final doctor_hospital id,final int count){


            prgDialog.show();


            RequestParams request=new RequestParams();
            request.put("token", doc.getAccessToken());
            request.put("table", "doctor_company");
            request.put("ID", id.ID+"");

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(base.BASE_URL + "delete" , request, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {


                    try {
                        list_hospital2.remove(id);
                        if(list_hospital2.size()==0)
                            result.setVisibility(View.GONE);
                        else
                            result.setVisibility(View.VISIBLE);
                        adapter_list_hospital.notifyDataSetChanged();



                    } catch (Exception e) {
                        Toast.makeText(getContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                    }

                    prgDialog.hide();
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if(count!=2){
                        deleteHos(id,count+1);
                    }else {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        prgDialog.hide();
                    }

                }
            });
        }

        class doctor_hospital{

            int ID;
            String name;

            public doctor_hospital(int ID, String name) {
                this.ID = ID;
                this.name = name;
            }


            @Override
            public String toString() {
                return name ;
            }
        }


    }



    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

            if(position == 0) // if the position is 0 we are returning the First tab
            {
                doctorFragment tab1 = new doctorFragment();
                return tab1;



            }
            else if(position == 1){
                passwordFragment tab1 = new passwordFragment();
                return tab1;
            }
            else if(position == 2)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                clinicFragment tab2 = new clinicFragment();
                return tab2;
            }
            else if(position == 3)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                workFragment tab2 = new workFragment();
                return tab2;
            }
            else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
                if(position == 4)
                {
                companyFragment tab1 = new companyFragment();
                return tab1;
                }
            else
                {
                    hospitalFragment tab1 = new hospitalFragment();
                    return tab1;
                }


        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }












}