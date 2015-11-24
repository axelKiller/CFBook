package ru.startandroid.mybook;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

import ru.startandroid.mybook.db.DatabaseHandler;

public class LoadingActivity extends Activity {
    TextView state;
    DatabaseHandler db;
    private static final int LAYOUT = R.layout.activity_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        db = new DatabaseHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        state = (TextView)findViewById(R.id.loading_state);
        LoadingAsyncTask splashScreenAsyncTask = new LoadingAsyncTask(this);
        splashScreenAsyncTask.execute();
    }



    private class LoadingAsyncTask extends AsyncTask <Void, Void, Void>{

        LoadingActivity activity;
        ApiConnector connector = new ApiConnector(db);
        public LoadingAsyncTask(LoadingActivity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {
            boolean checkTrain = false;
            connector.getAllTraining(checkTrain);
            return null;
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            state.setText("Интернет соединение отсутствует");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent myIntent = new Intent(activity, MainActivity.class);
                    activity.startActivity(myIntent);
                    activity.finish();
                }
            }, 100);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            state.setText("Загрузка тренировок...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            state.setText("Загрузка завершена");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent myIntent = new Intent(activity, MainActivity.class);
                    activity.startActivity(myIntent);
                    activity.finish();
                }
            }, 100);
        }
    }

}
