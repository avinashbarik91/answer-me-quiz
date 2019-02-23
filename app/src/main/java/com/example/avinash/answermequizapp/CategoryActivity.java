package com.example.avinash.answermequizapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

/**
 * Updated by Avinash Barik on 23/02/2019.
 */

public class CategoryActivity extends AppCompatActivity {
    private static final int MAX_QUESTIONS = 10;
    ListView listview;
    ArrayList<Category> categoryList;
    RequestQueue rq;
    ProgressDialog progressDialog ;
    private static final String PROGRESS_MSG = "Loading Quiz...";
    String url = "Not Set";
    ArrayList<QuestionModel> questionSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        rq = Volley.newRequestQueue(this);
        questionSet = new ArrayList<>();
        categoryList = new ArrayList<>();
        listview = (ListView) findViewById(R.id.listCategory);
        listview.setAdapter(new CategoryRowAdapter(getCategoryData(), this));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(CategoryActivity.this, categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                switch (categoryList.get(position).getName()){
                    case "Sports":
                        url = "https://opentdb.com/api.php?amount="+MAX_QUESTIONS+"&category=21&difficulty=easy&type=multiple";
                        break;
                    case "Cinema":
                        url = "https://opentdb.com/api.php?amount="+MAX_QUESTIONS+"&category=11&difficulty=easy&type=multiple";
                        break;
                    case "Science":
                        url = "https://opentdb.com/api.php?amount="+MAX_QUESTIONS+"&category=18&difficulty=easy&type=multiple";
                        break;
                    case "History":
                        url = "https://opentdb.com/api.php?amount="+MAX_QUESTIONS+"&category=23&difficulty=easy&type=multiple";
                        break;
                    default:
                        url = "https://opentdb.com/api.php?amount="+MAX_QUESTIONS+"&category=21&difficulty=easy&type=multiple";
                }
                sendJsonRequest();

            }
        });
    }

    private ArrayList<Category> getCategoryData()
    {
        categoryList.add(new Category("Sports", getResources().getDrawable(R.drawable.sports)));
        categoryList.add(new Category("Cinema", getResources().getDrawable(R.drawable.cinema)));
        categoryList.add(new Category("History", getResources().getDrawable(R.drawable.history)));
        categoryList.add(new Category("Science", getResources().getDrawable(R.drawable.science)));

        return categoryList;
    }

    public void sendJsonRequest()
    {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jArray = response.getJSONArray("results");
                    for (int i=0; i<jArray.length(); i++)
                    {
                        String question = jArray.getJSONObject(i).getString("question");
                        String formattedQuestion = Jsoup.parse(question).text();

                        String correctAnswer = jArray.getJSONObject(i).getString("correct_answer");
                        JSONArray incAnsArray = jArray.getJSONObject(i).getJSONArray("incorrect_answers");

                        String[] incorrectAnswers = new String[incAnsArray.length()];
                        for(int j=0; j<incAnsArray.length(); j++)
                        {
                            incorrectAnswers[j] = incAnsArray.getString(j);
                        }

                        QuestionModel qModel = new QuestionModel(formattedQuestion, correctAnswer, incorrectAnswers);
                        questionSet.add(qModel);
                        Log.i("JSON", qModel.toString());

                        Log.d("JSON", question);
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(CategoryActivity.this, QuizActivity.class);
                    intent.putExtra("CurrentPlayer", getIntent().getParcelableExtra("CurrentPlayer"));
                    intent.putExtra("QuestionSet", questionSet);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSON", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CategoryActivity.this, "Check internet connection!", Toast.LENGTH_SHORT).show();
            }
        });

        rq.add(jsonObjReq);

        progressDialog = new ProgressDialog(CategoryActivity.this);
        progressDialog.setMessage(PROGRESS_MSG);
        progressDialog.show();
    }


}
