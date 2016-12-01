package com.apps.scit.tabibihon;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;



import java.util.ArrayList;

import entities.base;
import entities.doctor;
import entities.result;
import entities.search_param;
import entities.user;
import extra.TochLoc;
import extra.TochView;


public class searchResults extends AppCompatActivity {

    ViewPager pager;
    private static ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    public static  int Numboftabs =1;
    private static  CharSequence Titles[]={""};
    private static  int current =0;
    private static  ArrayList<result>doctors;
    private static LinearLayout result_tab;
    private static RelativeLayout search_no_result;
    public static TextView statuso,search_page;
   //public static int total=0;



    private static DisplayImageOptions options;
    private static DisplayImageOptions options2;

    private static doctor doc=null;
    private static user us=null;
    public static RequestParams requset_search;
    //int tabnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_res);




        search_param sea =(search_param)getIntent().getSerializableExtra("search");

        requset_search=new RequestParams();

        requset_search.put("token",sea.token);
        requset_search.put("specialization_ID", sea.spec);
        requset_search.put("name", sea.name);
        requset_search.put("area_ID", sea.ar);
        requset_search.put("city_ID", sea.ci);
        requset_search.put("type", sea.type);


        if(sea.type.equals("1"))
        {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ph_profile_icon)
                    .showImageOnFail(R.drawable.ph_profile_icon)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(40))
                    .build();
            options2 = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ph_profile_icon)
                    .showImageOnFail(R.drawable.ph_profile_icon)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(0))
                    .build();


        }
        else {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.profile_icon2)
                    .showImageOnFail(R.drawable.profile_icon2)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(40))
                    .build();
            options2 = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.profile_icon2)
                    .showImageOnFail(R.drawable.profile_icon2)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(new RoundedBitmapDisplayer(0))
                    .build();
        }



        Numboftabs=(int)getIntent().getSerializableExtra("count");
        String s=" عدد النتائج "+Numboftabs+" نتيجة ";

        ((TextView) findViewById(R.id.search_num)).setText(s);

        result_tab=(LinearLayout)findViewById(R.id.result_tab);
        statuso=(TextView)findViewById(R.id.statuso);
        search_page=(TextView)findViewById(R.id.search_page);
        search_no_result=(RelativeLayout)findViewById(R.id.search_no_result);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        int num=0;
        num=Numboftabs/10;
        if(Numboftabs%10!=0)
            num++;
        if (Numboftabs==0)
        {
            search_no_result.setVisibility(View.VISIBLE);
            search_page.setVisibility(View.GONE);
        }
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),new String[num],num);

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
                return getResources().getColor(R.color.mylightgray);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        String ss=" الصفحة "+(1)+" من "+adapter.getCount();
        search_page.setText(ss);

        //PageListener pageListener = new PageListener();
        //pager.setOnPageChangeListener(pageListener);

        if(!ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));





        if(getIntent().hasExtra("doctor"))
        {

            doc=(doctor)getIntent().getSerializableExtra("doctor");


        }
        else
        if(getIntent().hasExtra("user")){
            us=(user)getIntent().getSerializableExtra("user");

        }






    }




    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
        public  int pos;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {
            pos=position;


            //ImageLoader.getInstance().stop();
            current = position*10;
            searchFragment tab1 = new searchFragment();
            tab1.current=current;



        return  tab1;
        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }



    public static  class searchFragment extends Fragment {
        View[]img;
        View[]name;
        View[]spec;
        View[]date;
        View[]view;
        View[]loc;
        View[]sh;
        RelativeLayout loadingPanel_stats,error_loding;
        RelativeLayout search_view;
        public int current ;

        public searchFragment(  ) {

        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_search_results, container, false);



            img=new View[]{rootView.findViewById(R.id.img1),rootView.findViewById(R.id.img2),rootView.findViewById(R.id.img3),
                    rootView.findViewById(R.id.img4),rootView.findViewById(R.id.img5),rootView.findViewById(R.id.img6),
                    rootView.findViewById(R.id.img7),rootView.findViewById(R.id.img8),rootView.findViewById(R.id.img9)
                    ,rootView.findViewById(R.id.img10)};
            date=new View[]{rootView.findViewById(R.id.date1),rootView.findViewById(R.id.date2),rootView.findViewById(R.id.date3),
                    rootView.findViewById(R.id.date4),rootView.findViewById(R.id.date5),rootView.findViewById(R.id.date6),
                    rootView.findViewById(R.id.date7),rootView.findViewById(R.id.date8),rootView.findViewById(R.id.date9)
                    ,rootView.findViewById(R.id.date10)};
            spec=new View[]{rootView.findViewById(R.id.spec1),rootView.findViewById(R.id.spec2),rootView.findViewById(R.id.spec3),
                    rootView.findViewById(R.id.spec4),rootView.findViewById(R.id.spec5),rootView.findViewById(R.id.spec6),
                    rootView.findViewById(R.id.spec7),rootView.findViewById(R.id.spec8),rootView.findViewById(R.id.spec9)
                    ,rootView.findViewById(R.id.spec10)};
            name=new View[]{rootView.findViewById(R.id.name1),rootView.findViewById(R.id.name2),rootView.findViewById(R.id.name3),
                    rootView.findViewById(R.id.name4),rootView.findViewById(R.id.name5),rootView.findViewById(R.id.name6),
                    rootView.findViewById(R.id.name7),rootView.findViewById(R.id.name8),rootView.findViewById(R.id.name9)
                    ,rootView.findViewById(R.id.name10)};

            view=new View[]{rootView.findViewById(R.id.view1),rootView.findViewById(R.id.view2),rootView.findViewById(R.id.view3),
                    rootView.findViewById(R.id.view4),rootView.findViewById(R.id.view5),rootView.findViewById(R.id.view6),
                    rootView.findViewById(R.id.view7),rootView.findViewById(R.id.view8),rootView.findViewById(R.id.view9)
                    ,rootView.findViewById(R.id.view10)};

            loc=new View[]{rootView.findViewById(R.id.loc1),rootView.findViewById(R.id.loc2),rootView.findViewById(R.id.loc3),
                    rootView.findViewById(R.id.loc4),rootView.findViewById(R.id.loc5),rootView.findViewById(R.id.loc6),
                    rootView.findViewById(R.id.loc7),rootView.findViewById(R.id.loc8),rootView.findViewById(R.id.loc9)
                    ,rootView.findViewById(R.id.loc10)};
            sh=new View[]{rootView.findViewById(R.id.sh1),rootView.findViewById(R.id.sh2),rootView.findViewById(R.id.sh3),
                    rootView.findViewById(R.id.sh4),rootView.findViewById(R.id.sh5),rootView.findViewById(R.id.sh6),
                    rootView.findViewById(R.id.sh7),rootView.findViewById(R.id.sh8),rootView.findViewById(R.id.sh9)
                    ,rootView.findViewById(R.id.sh10)};

            for(int i=0;i<10;i++) {
                ((ImageView)view[i]).setOnTouchListener(new TochView((ImageView)view[i]));
                ((ImageView)loc[i]).setOnTouchListener(new TochLoc((ImageView)loc[i]));

            }
            //search_no_result=(RelativeLayout)rootView.findViewById(R.id.search_no_result);
            loadingPanel_stats=(RelativeLayout)rootView.findViewById(R.id.loadingPanel_stats);
            error_loding=(RelativeLayout)rootView.findViewById(R.id.error_loding);
            search_view=(RelativeLayout)rootView.findViewById(R.id.search_view);
            loadingPanel_stats.setVisibility(View.GONE);

            statuso.setVisibility(View.GONE);
            clearImg();
            do_search(requset_search,0);
            return rootView;
        }


        void getSearch()
        {






            for(int i=0;i<doctors.size();i++) {
                int j = i ;
                ((ImageView)view[j]).setVisibility(View.VISIBLE);
                //((ImageView) img[j]).setBackgroundResource(R.drawable.profile_icon);



                final result item = doctors.get(i);
                if(item.type.equals("0"))
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.profile_icon2, ((ImageView) img[j]), options2);
                else
                    ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ph_profile_icon, ((ImageView) img[j]), options2);
                ((ImageView)loc[j]).setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(item.image)) {
                    // Toast.makeText(this,item.doc,Toast.LENGTH_SHORT).show();
                    if(item.image.startsWith("https"))
                        ImageLoader.getInstance().displayImage(item.image, (ImageView) img[j], options);
                    else
                    ImageLoader.getInstance().displayImage(base.BASE_URL + "125/" + item.image, (ImageView) img[j], options);
                    // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }

                ((TextView) name[j]).setText(item.doc);
                if(item.type.equals("0"))
                ((TextView) spec[j]).setText(item.spec);
                else
                    ((TextView) spec[j]).setText("صيدلية "+item.spec);
                ((TextView) date[j]).setText(item.city + " " + item.area);

                if(item.authorized.equals("1"))
                    ((ImageView)sh[j]).setVisibility(View.VISIBLE);

                ((ImageView) loc[j]).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(item.location)) {
                            Intent ii = new Intent(getContext(), MapsActivity.class);
                            ii.putExtra("location", item.location);
                            if (doc != null) {
                                if (doc.getID() == item.id)
                                    ii.putExtra("me", "1");

                            }
                            ii.putExtra("type",item.type);
                            startActivity(ii);
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.invailed_loc), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                clickView click=new clickView();
                click.item=item;
                ((ImageView) view[j]).setOnClickListener(click);
                ((TextView)name[j]).setOnClickListener(click);
                ((ImageView)img[j]).setOnClickListener(click);

            }

        }


        void do_search(final RequestParams request,final int count) {



            request.put("current", current + "");

            (loadingPanel_stats).setVisibility(View.VISIBLE);
            (error_loding).setVisibility(View.GONE);
            (search_view).setVisibility(View.GONE);


            AsyncHttpClient client = new AsyncHttpClient();
            client.post(base.BASE_URL + "search", request, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(String response) {

                    doctors = result.readOBJArray(response);

                    getSearch();



                    //   prgDialog.hide();
                    //findViewById(R.id.result_tab).setVisibility(View.VISIBLE);
                    loadingPanel_stats.setVisibility(View.GONE);
                    (search_view).setVisibility(View.VISIBLE);
                   // statuso.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                   // Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    //   prgDialog.hide();
                  //  result_tab.setVisibility(View.VISIBLE);
                    if(count!=5)
                    {
                     do_search(request, count+1);
                    }
                    else {

                        loadingPanel_stats.setVisibility(View.GONE);
                        error_loding.setVisibility(View.VISIBLE);
                    }
                   // statuso.setVisibility(View.GONE);

                }
            });
        }



        void clearImg()
        {

            for(int i=0;i<10;i++) {
                //ImageLoader.getInstance().cancelDisplayTask(((ImageView) img[i]));
                ((ImageView)view[i]).setVisibility(View.INVISIBLE);
                ((ImageView)sh[i]).setVisibility(View.INVISIBLE);
                ((ImageView)img[i]).setImageBitmap(null);
                ((ImageView) loc[i]).setVisibility(View.INVISIBLE);
                ((TextView) name[i]).setText("");
                ((TextView) date[i]).setText("");
                ((TextView) spec[i]).setText("");
            }

        }

        class clickView implements View.OnClickListener {
            result item;

            @Override
            public void onClick(View v) {

                Intent ii;
                if (doc!=null) {
                    if (doc.getID() == item.id) {
                        ii = new Intent(getContext(), my_profile.class);
                        ii.putExtra("search", "1");
                    }
                    else {
                        ii = new Intent(getContext(), profile.class);
                        ii.putExtra("id", item.id);
                    }
                    ii.putExtra("doctor", doc);
                    ii.putExtra("type", item.type);
                } else {
                    ii = new Intent(getContext(), profile.class);
                    if(us!=null)
                    ii.putExtra("user", us);

                    ii.putExtra("id", item.id);
                    ii.putExtra("type", item.type);
                }


                startActivity(ii);
            }
        }

    }




}