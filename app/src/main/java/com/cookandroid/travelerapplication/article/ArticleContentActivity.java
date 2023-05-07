package com.cookandroid.travelerapplication.article;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.travelerapplication.MainActivity;
import com.cookandroid.travelerapplication.comment.Comment;
import com.cookandroid.travelerapplication.comment.CommentAdapter;
import com.cookandroid.travelerapplication.comment.CommentListActivity;
import com.cookandroid.travelerapplication.helper.FileHelper;
import com.cookandroid.travelerapplication.R;
import com.cookandroid.travelerapplication.task.DeleteData_Article;
import com.cookandroid.travelerapplication.task.InsertData_Comment;
import com.cookandroid.travelerapplication.task.SelectData_Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ArticleContentActivity extends AppCompatActivity {

    TextView textview_content;
    TextView textview_title;
    private EditText edittext_content;
    private Button button_delete;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static String IP_ADDRESS; //본인 IP주소를 넣으세요.
    private String article_id;
    private TextView textview_name;
    private TextView textView_date;
    private ImageView profilePhoto;
    private TextView textview_count_view;
    private TextView board_comment;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content2);

        Intent intent_article = getIntent();
        textview_content = findViewById(R.id.textview_content);
        textview_content.setText(intent_article.getStringExtra("content"));
        textview_title = findViewById(R.id.textview_title);
        textview_title.setText(intent_article.getStringExtra("title"));
        textview_name = findViewById(R.id.textview_name);
        textview_name.setText(intent_article.getStringExtra("name"));
        textView_date = findViewById(R.id.textView_date);
        textView_date.setText(intent_article.getStringExtra("created_date"));
        profilePhoto = findViewById(R.id.profilePhoto);
        //Glide.with(this).load(intent_article.getStringExtra("image_url")).load(profilePhoto);
        textview_count_view = findViewById(R.id.textview_count_view);
        textview_count_view.setText(" " + intent_article.getStringExtra("hit"));
        board_comment = findViewById(R.id.board_comment);
        board_comment.setText(" " + intent_article.getStringExtra("like_count"));


        ArrayList<Article> articleArrayList = new ArrayList<>();
        SelectData_Article task = new SelectData_Article(articleArrayList);
        task.execute("http://" + IP_ADDRESS + "/0422/selectdata_article.php");



        FileHelper fileHelper = new FileHelper(this);
        String IP_ADDRESS = fileHelper.readFromFile("IP_ADDRESS");
        String user_id = fileHelper.readFromFile("user_id").trim();

        if (user_id.equals(intent_article.getStringExtra("user_id"))) {
            findViewById(R.id.button_update).setVisibility(View.VISIBLE);
            findViewById(R.id.deleteBtn).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.backBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleListActivity.class);
            startActivity(intent);
        });


        //댓글 관련
        edittext_content = findViewById(R.id.edittext_content);

        recyclerView = findViewById(R.id.recyclerView_comment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.button_add).setOnClickListener(v -> {
            String content = edittext_content.getText().toString().trim();

            if (content.equals("")){
                Toast.makeText(this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                String currentTime = getCurrentTime();
                String created_date = currentTime;
                String modified_date = currentTime;
                String parent_comment_id = getIntent().getStringExtra("comment_id");
                if (parent_comment_id == null){
                    parent_comment_id = "0";
                }
                InsertData_Comment task2 = new InsertData_Comment();
                task2.execute("http://" + IP_ADDRESS + "/0422/InsertData_Comment.php", "0", created_date, modified_date, content, article_id, "0", parent_comment_id, user_id);
                Refresh();
            }


            ArrayList<Comment> commentArrayList = new ArrayList<>();
            SelectData_Article task2 = new SelectData_Article(commentArrayList);
            String parent_comment_id = getIntent().getStringExtra("comment_id");
            if (parent_comment_id == null){
                parent_comment_id = "0";
            }
            task2.execute("http://" + IP_ADDRESS + "/0422/selectdata_comment.php", article_id, parent_comment_id);

            try {
                new Handler().postDelayed(() -> {
                    adapter = new CommentAdapter(commentArrayList, this);
                    recyclerView.setAdapter(adapter);
                }, 1000); // 0.5초 지연 시간
            }catch (Exception e){
                e.printStackTrace();
            }

        });



        findViewById(R.id.deleteBtn).setOnClickListener(v -> {
            String article_id = intent_article.getStringExtra("article_id").trim();
            DeleteData_Article task2 = new DeleteData_Article();
            task2.execute("http://"+IP_ADDRESS+"/0411/deletedata_article.php",article_id);
            finish();
        });

        //게시글 수정버튼
        findViewById(R.id.button_update).setOnClickListener(v -> {
            Intent intent = new Intent(this, ArticleCreateActivity.class);
            intent.putExtra("sign", "1");
            intent.putExtra("title", intent_article.getStringExtra("title"));
            intent.putExtra("content", intent_article.getStringExtra("content"));
            intent.putExtra("article_id", intent_article.getStringExtra("article_id"));
            startActivity(intent);
            finish();
        });

    }

    private String getCurrentTime() {
        // 현재 시간 가져오기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public void Refresh() {
        ArrayList<Comment> commentArrayList = new ArrayList<>();
        SelectData_Article task = new SelectData_Article(commentArrayList);
        String parent_comment_id = getIntent().getStringExtra("comment_id");
        if (parent_comment_id == null){
            parent_comment_id = "0";
        }
        task.execute("http://" + IP_ADDRESS + "/0422/selectdata_comment.php", article_id, parent_comment_id);

        try {
            new Handler().postDelayed(() -> {
                adapter = new CommentAdapter(commentArrayList, this);
                recyclerView.setAdapter(adapter);
            }, 1000); // 0.5초 지연 시간
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}