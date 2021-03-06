package com.example.peter.workcomplain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.peter.workcomplain.Utils.ApiUtils;
import com.example.peter.workcomplain.adapters.RecyclerAdapter;
import com.example.peter.workcomplain.retrofit.model.ComplainResponseModel;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplainListActivity extends AppCompatActivity {

    @BindView(R.id.complainList)
    RecyclerView mRecyclerView;
    @BindView(R.id.complainsLoading)
    ProgressBar mProgressBar;
    RecyclerAdapter mRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_list);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager =new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getData();
    }

    private void getData(){
        ApiUtils.getApiService().getComplain(Prefs.getString("email","")).enqueue(new Callback<List<ComplainResponseModel>>() {
            @Override
            public void onResponse(Call<List<ComplainResponseModel>> call, Response<List<ComplainResponseModel>> response) {
                if (response.isSuccessful()){
                    List <ComplainResponseModel> list = response.body();
                    if (response.body().size()!=0){
                        mProgressBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mRecyclerAdapter = new RecyclerAdapter(response.body(),ComplainListActivity.this);
                        mRecyclerView.setAdapter(mRecyclerAdapter);
                    }else {
                        Toast.makeText(ComplainListActivity.this, "No Complains", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ComplainResponseModel>> call, Throwable t) {

            }
        });
    }
}
