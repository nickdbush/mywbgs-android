package com.nickdbush.mywbgs.tasks;

import android.os.AsyncTask;
import android.os.Bundle;

import com.nickdbush.mywbgs.models.Lesson;
import com.nickdbush.mywbgs.models.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetHomeworkTask extends AsyncTask<Bundle, Void, GetHomeworkTask.Result> {

    private OkHttpClient client;
    private OnHomeworkReturned onHomeworkReturned;

    public GetHomeworkTask(OnHomeworkReturned onHomeworkReturned) {
        this.onHomeworkReturned = onHomeworkReturned;
    }

    @Override
    protected Result doInBackground(Bundle... bundles) {
        CookieJar cookieJar = new CookieJar() {
            HashMap<String, Cookie> cookies = new HashMap<String, Cookie>();

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                for (Cookie cookie : list) {
                    cookies.put(cookie.name(), cookie);
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                return new ArrayList<Cookie>(cookies.values());
            }
        };

        client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .cookieJar(cookieJar)
                .build();

        Bundle credentials = bundles[0];
        String username = credentials.getString("username", "");
        String password = credentials.getString("password", "");

        RequestBody loginBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build();

        Request loginRequest = new Request.Builder()
                .url("https://learning.watfordboys.org/login/index.php")
                .post(loginBody)
                .build();

        Response loginResponse;
        try {
            loginResponse = client.newCall(loginRequest).execute();
            if (!loginResponse.request().url().toString().equals("https://learning.watfordboys.org/blocks/mis_portal/index.php"))
                return new Result(null, null, null);
            loginResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(null, null, e);
        }

        Request timetableRequest = new Request.Builder()
                .url("https://learning.watfordboys.org/blocks/mis_portal/index.php?tab=timetable")
                .get()
                .build();

        Response timetableResponse;
        Document timetableDocument;
        try {
            timetableResponse = client.newCall(timetableRequest).execute();
            timetableDocument = Jsoup.parse(timetableResponse.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(null, null, e);
        }
        timetableResponse.close();

        List<Lesson> lessons = new ArrayList<Lesson>();
        Element table = timetableDocument.getElementById("portlet_timetable_full_1_table");
        Elements rows = table.select("tr");
        for (int rowi = 0; rowi < rows.size(); rowi++) {
            Element row = rows.get(rowi);
            Elements cells = row.select("td.period");
            int celli = 0;
            for (Element cell : cells) {
                String subject = cell.select(".tt_subject").get(0).html();
                if (subject == null || subject.trim().length() == 0 || subject.trim().toLowerCase().equals("registration"))
                    continue;
                Lesson lesson = new Lesson();
                lesson.generateId();
                lesson.setDay(rowi - 1);
                lesson.setPeriod(celli++);
                lesson.setSubject(subject);
                lesson.setRoom(Utils.formatRoom(cell.select(".tt_group_room .tt_room").get(0).html()));
                lessons.add(lesson);
            }
        }

        RequestBody metaBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("portletid", "portlet_personal_details_1")
                .addFormDataPart("tab", "learner_welcome")
                .build();

        Request metaRequest = new Request.Builder()
                .url("https://learning.watfordboys.org/blocks/mis_portal/ajax/portlet_html.php")
                .post(metaBody)
                .build();

        Response metaResponse;
        Document metaDocument;
        try {
            metaResponse = client.newCall(metaRequest).execute();
            metaDocument = Jsoup.parse(metaResponse.body().string());
            metaResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(null, null, e);
        }
        Elements metaValues = metaDocument.select("td.value");

        Bundle metaBundle = new Bundle();
        metaBundle.putString("username", username);
        metaBundle.putString("name", metaValues.get(0).html());
        metaBundle.putInt("year", Integer.parseInt(metaValues.get(3).html()));
        String metaForm = metaValues.get(4).html();
        metaForm = metaForm.replaceAll("\\(.*?\\)", "").trim();
        metaForm = metaForm.replaceAll("\\d", "").trim().toLowerCase();
        metaBundle.putChar("house", metaForm.charAt(0));
        metaBundle.putString("email", metaValues.get(7).select("a").get(0).html());

        return new Result(lessons, metaBundle, null);
    }

    @Override
    protected void onPostExecute(Result result) {
        onHomeworkReturned.onHomeworkReturned(result);
    }

    public interface OnHomeworkReturned {
        void onHomeworkReturned(Result result);
    }

    public static class Result {
        public List<Lesson> lessons;
        public Exception exception;
        public Bundle meta;

        public Result(List<Lesson> lessons, Bundle meta, Exception exception) {
            this.lessons = lessons;
            this.meta = meta;
            this.exception = exception;
        }
    }
}
