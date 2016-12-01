package com.apps.scit.tabibihon;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import costumize.ArabEditText;
import costumize.ArabTextView;
import extra.custom_AdviceListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entities.advice;
import entities.advice_wrapper;
import entities.base;
import entities.doctor;
import entities.user;

/**
 * Created by Rock on 5/26/2016.
 */
public class doctorAdvice extends Activity {

    private ListView gridView;
    private custom_AdviceListAdapter gridAdapter;

    ArrayList<advice_wrapper>advices=new ArrayList<advice_wrapper>();


    doctor doc=null,target=null;
    user us=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actvity_doctor_advice);



        if(getIntent().hasExtra("doctor"))
        {

            doc=(doctor)getIntent().getSerializableExtra("doctor");


        }
        else
        if(getIntent().hasExtra("user")){
            us=(user)getIntent().getSerializableExtra("user");

        }


        target=(doctor)getIntent().getSerializableExtra("target");

        gridView = (ListView) findViewById(R.id.advice_list);

        ((Button)findViewById(R.id.advice_add2)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Add_adviceDialog();

                    }
                }
        );
        ((Button)findViewById(R.id.advice_add)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Add_adviceDialog();

                    }
                }
        );

        getAdvice(0);



    }





    @Override
    protected void onPause() {
        super.onPause();

    }


    private void getAdvice(final int count) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("doctor_ID", target.getID()+"");
        req.put("user_ID", (us==null)?"-1":us.getID()+"");

        if(us!=null)
        req.put("token",us.getAccessToken());
        else
            req.put("token", doc.getAccessToken());


        client.post(base.BASE_URL + "get_doctor_advice", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    ArrayList<advice_wrapper> resAR = advice_wrapper.readOBJArray(response);
                    advices.clear();
                    for (advice_wrapper help : resAR) {
                        advices.add(help);

                    }
                    gridAdapter = new custom_AdviceListAdapter(doctorAdvice.this, advices, (doc == null) ? us : doc, target);
                    gridView.setAdapter(gridAdapter);

                    if(advices.size()==0){
                        if(doc!=null&&doc.getID()==target.getID()) {
                            ((Button) findViewById(R.id.advice_add2)).setVisibility(View.VISIBLE);
                            ((Button)findViewById(R.id.advice_add)).setVisibility(View.VISIBLE);
                        }



                        findViewById(R.id.search_no_result).setVisibility(View.VISIBLE);
                    }
                    else{
                        if(doc!=null&&doc.getID()==target.getID())
                        {
                            ((Button) findViewById(R.id.advice_add2)).setVisibility(View.VISIBLE);
                            ((Button)findViewById(R.id.advice_add)).setVisibility(View.VISIBLE);
                        }


                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                }

                findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.statuso)).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count!=5){
                    getAdvice(count+1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();

                 //   findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.statuso)).setVisibility(View.GONE);
                }

            }
        });



    }


    void Add_adviceDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle();
        TextView t=new ArabTextView(this);
        t.setTextSize(18);
        t.setGravity(Gravity.CENTER);
        t.setPadding(0, 5, 0, 9);
        t.setTextColor(getResources().getColor(R.color.myred));
        t.setText(" اضافة نصيحة");
        builder.setCustomTitle(t);

        // Set up the input
        final EditText input = new ArabEditText(this);
        input.setHint("النصيحة");


        input.setTextColor(getResources().getColor(R.color.myblue));

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("اضافة", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(TextUtils.isEmpty(input.getText().toString()))
                    input.setError(getString(R.string.error_field_required));
                else
                    add_advice(input.getText().toString(),0);
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


    private void add_advice(final String s,final int count) {

        findViewById(R.id.loadingPanel_stats).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.statuso)).setVisibility(View.VISIBLE);

       final advice  dl=new advice();
        dl.setContent(s);
        SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd ");
        dl.setA_date(df.format(new Date()) + "");
        dl.setDoctor_ID(doc.getID() + "");

        RequestParams request= dl.getEntity();
        request.put("token", doc.getAccessToken());
        request.put("table", "advice");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(base.BASE_URL + "add", request, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject obj=new JSONObject(response);
                    if(obj.has("id"))
                    {
                        dl.setID(obj.getInt("id"));
                        Timestamp timestamp =  Timestamp.valueOf(obj.getString("date"));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dl.setA_date(simpleDateFormat.format(timestamp));

                       // dl.setA_date(obj.getString("date").split(" ")[0]);
                        Toast.makeText(getApplicationContext(), "تمت اضافة النصيحة بنجاح", Toast.LENGTH_SHORT).show();
                        advices.add(new advice_wrapper(dl, 0));
                        gridAdapter.notifyDataSetChanged();
                        if(advices.size()==1)
                        {
                            findViewById(R.id.search_no_result).setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "خطأ في عملية الاضافة", Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "خطأ في جلب البيانات", Toast.LENGTH_SHORT).show();
                }

                findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.statuso)).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                if(count !=5){
                    add_advice(s,count+1);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    findViewById(R.id.loadingPanel_stats).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.statuso)).setVisibility(View.GONE);
                }

            }
        });




    }



}