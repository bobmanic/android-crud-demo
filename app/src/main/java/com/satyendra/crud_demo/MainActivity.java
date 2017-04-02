package com.satyendra.crud_demo;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.satyendra.bean.Student;
import com.satyendra.constants.Constants;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditDialogBox.EditDialogBoxListener, AddDialogBox.AddDialogBoxListener, DeleteDialogBox.DeleteDialogBoxListener {

    private static final String TAG = "MainActivity";

    ListView studentListView;
    ProgressDialog progressDialog;
    private List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        loadData();
    }

    /**
     * method to set the received data, to ListView.
     */
    private void loadListView() {

        studentListView = (ListView) findViewById(R.id.list);
        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this, R.layout.row_layout, students) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.row_layout, null);
                }

                TextView rollno = (TextView) convertView.findViewById(R.id.rollno_textView);
                rollno.setText(String.valueOf(students.get(position).getRollno()));

                TextView name = (TextView) convertView.findViewById(R.id.name_textView);
                name.setText(students.get(position).getName());

                return convertView;
            }
        };
        studentListView.setAdapter(adapter);
        registerForContextMenu(studentListView);
    }

    /**
     * Pop-up menu on long click.
     *
     * @param menu     {@link android.view.Menu}
     * @param v        {@link View}
     * @param menuInfo {@link android.view.ContextMenu.ContextMenuInfo}
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * Check for action to be performed on menu-item click.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rollno = students.get(info.position).getRollno();
        Log.d(TAG, "onContextItemSelected: rollno = " + rollno);

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ROLLNO, rollno);

        switch (item.getItemId()) {
            case R.id.edit:
                DialogFragment editDialog = new EditDialogBox();
                editDialog.setArguments(bundle);
                editDialog.show(getFragmentManager(), TAG);
                return true;
            case R.id.delete:
                DialogFragment deleteDialog = new DeleteDialogBox();
                deleteDialog.setArguments(bundle);
                deleteDialog.show(getFragmentManager(), TAG);
                return true;
        }
        return false;
    }

    /**
     * TODO: Get by rollno
     */
    private void addClickListener() {
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

            }
        });
    }

    /**
     * method to pop up add student Dialog.
     *
     * @param view {@link View}
     */
    public void add(View view) {
        DialogFragment addDialog = new AddDialogBox();
        addDialog.show(getFragmentManager(), TAG);
    }

    /**
     * method to reload the data. make the server call again.
     *
     * @param view {@link View}
     */
    public void refresh(View view) {
        loadData();
    }

    /**
     * POST request to server to add new data.
     *
     * @param student {@link Student}
     */
    @Override
    public void createData(Student student) {
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d(TAG, "making POST request");

        Gson gson = new Gson();
        String json = gson.toJson(student);
        StringEntity entity = null;
        try {
            entity = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "createData: " + json);
            e.printStackTrace();
        }
        client.post(getApplicationContext(), Constants.URL, entity, Constants.APPLICATION_JSON, new AsyncHttpResponseHandler() {

            /**
             * @param statusCode integer
             * @param content string
             * @deprecated
             */
            @Override
            public void onSuccess(int statusCode, String content) {
                progressDialog.hide();
                Log.d(TAG, "SUCCESS");
                Toast.makeText(getApplicationContext(), "CREATED", Toast.LENGTH_SHORT).show();
                loadData();
            }

            /**
             * @param statusCode integer
             * @param error {@link Throwable}
             * @param content String
             * @deprecated
             */
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                progressDialog.hide();
                Log.d(TAG, "FAILURE");
                Toast.makeText(getApplicationContext(), "Requested resource not found !!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * GET request to server to get the data.
     */
    private void loadData() {

        progressDialog.show();
        students = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d(TAG, "making GET request");

        client.get(Constants.URL, new AsyncHttpResponseHandler() {

            /**
             * @param statusCode integer
             * @param content string
             * @deprecated
             */
            @Override
            public void onSuccess(int statusCode, String content) {
                progressDialog.hide();
                Gson gson = new Gson();
                students = gson.fromJson(content, new TypeToken<List<Student>>() {
                }.getType());
                Log.d(TAG, "onSuccess: students : " + students);
                loadListView();
            }

            /**
             * @param statusCode integer
             * @param error {@link Throwable}
             * @param content String
             * @deprecated
             */
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                progressDialog.hide();
                Log.d(TAG, "FAILURE");
                Toast.makeText(getApplicationContext(), "Requested resource not found !!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * PUT request to server to update the data.
     *
     * @param student
     */
    @Override
    public void updateData(Student student) {
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d(TAG, "making PUT request");

        Gson gson = new Gson();
        String json = gson.toJson(student);
        StringEntity entity = null;
        try {
            entity = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "updateData: " + json);
            e.printStackTrace();
        }
        client.put(getApplicationContext(), Constants.URL, entity, Constants.APPLICATION_JSON, new AsyncHttpResponseHandler() {

            /**
             * @param statusCode integer
             * @param content string
             * @deprecated
             */
            @Override
            public void onSuccess(int statusCode, String content) {
                progressDialog.hide();
                Log.d(TAG, "SUCCESS");
                Toast.makeText(getApplicationContext(), "UPDATED", Toast.LENGTH_SHORT).show();
                loadData();
            }

            /**
             * @param statusCode integer
             * @param error {@link Throwable}
             * @param content String
             * @deprecated
             */
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                progressDialog.hide();
                Log.d(TAG, "FAILURE");
                Toast.makeText(getApplicationContext(), "Requested resource not found !!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * DELETE request to server to delete the record.
     *
     * @param rollno int
     */
    @Override
    public void deleteData(int rollno) {
        progressDialog.show();
        students = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d(TAG, "making DELETE request");

        client.delete(Constants.URL + rollno, new AsyncHttpResponseHandler() {

            /**
             * @param statusCode integer
             * @param content string
             * @deprecated
             */
            @Override
            public void onSuccess(int statusCode, String content) {
                progressDialog.hide();
                Log.d(TAG, "SUCCESS");
                Toast.makeText(getApplicationContext(), "DELETED", Toast.LENGTH_SHORT).show();
                loadData();
            }

            /**
             * @param statusCode integer
             * @param error {@link Throwable}
             * @param content String
             * @deprecated
             */
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                progressDialog.hide();
                Log.d(TAG, "FAILURE");
                Toast.makeText(getApplicationContext(), "Requested resource not found !!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
