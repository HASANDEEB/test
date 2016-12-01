package com.apps.scit.tabibihon;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.net.IDN;
import java.util.ArrayList;

import entities.advice_wrapper;
import entities.base;
import entities.city;
import entities.doctor;
import entities.specialization;
import entities.user;
import entities.voting;
import extra.custom_AdviceListAdapter;


public class Voting extends ActionBarActivity {

    ArrayList<voting>votes=new ArrayList<voting>();
    ArrayAdapter<voting> adapter_voting;
    ListView li;
    private ProgressDialog prgDialog;
    private doctor doc;
    private user us;
    boolean[] select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        if(getIntent().hasExtra("doctor"))
        {

            doc=(doctor)getIntent().getSerializableExtra("doctor");


        }
        else
        if(getIntent().hasExtra("user")){
            us=(user)getIntent().getSerializableExtra("user");

        }


        li=(ListView)findViewById(R.id.vote_list);
        adapter_voting = new ArrayAdapter<voting>(this,R.layout.listview_item, votes);
        li.setAdapter(adapter_voting);
        getVote();


li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (select[position])
            select[position] = false;
        else
            select[position] = true;
    }
});


        ((Button)findViewById(R.id.b_vote)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String IDs = "";
                ArrayList<Integer> vv = new ArrayList<Integer>();
                for (int i = 0; i < select.length; i++) {
                    if (select[i]) {
                        vv.add(votes.get(i).getID());

                    }

                }

                if(vv.size()==0)
                    finish();
                else {

                    for (int i = 0; i < vv.size() - 1; i++) {
                        IDs = IDs + vv.get(i) + ",";
                    }
                    IDs = IDs + vv.get(vv.size() - 1);

                    makeVote(IDs);
                }
            }
        });


        li.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voting, menu);
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



    private void getVote() {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();


        client.post(base.BASE_URL+"getVoting", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    ArrayList<voting> resAR = voting.readOBJArray(response);
                    votes.clear();
                    for (voting help : resAR) {
                        votes.add(help);
                        //
                    }

                    adapter_voting.notifyDataSetChanged();
                    select=new boolean[votes.size()];

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                    prgDialog.hide();
                }

                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                prgDialog.hide();

            }
        });



    }


    private void makeVote(String ids){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams req=new RequestParams();
        req.put("IDs",ids);
        if(doc==null)
            req.put("token",us.getAccessToken());
        else
            req.put("token", doc.getAccessToken());


        client.post(base.BASE_URL + "editVoteing", req, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {


                try {
                    prgDialog.hide();

                    finish();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.fail, Toast.LENGTH_SHORT).show();
                }
                prgDialog.hide();


            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                prgDialog.hide();


            }
        });
    }

}
