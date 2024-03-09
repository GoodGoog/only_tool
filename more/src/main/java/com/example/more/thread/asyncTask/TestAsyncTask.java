package com.example.more.thread.asyncTask;

import android.os.AsyncTask;
import android.os.Process;
import android.text.PrecomputedText;

import javax.xml.transform.Result;

/**
 * Created by zhangqy
 * Data : 2024/3/7
 */
public class TestAsyncTask extends AsyncTask<PrecomputedText.Params, Process, Result> {


    @Override
    protected Result doInBackground(PrecomputedText.Params... params) {
        return null;
    }

    public TestAsyncTask() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Process... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
