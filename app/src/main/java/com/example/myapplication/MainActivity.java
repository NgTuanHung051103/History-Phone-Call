package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_CALL_LOG = 1;
    private TextView callLogTextView;
    private ArrayList<CallLogModel> _listCallLogs;
    private RecyclerView rv_call_logs;
    private CallLogAdapter callLogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.layout_call_log);
        _listCallLogs = new ArrayList<CallLogModel>();
//        callLogTextView = findViewById(R.id.call_log_text_view);

        // Kiểm tra quyền
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    REQUEST_CODE_READ_CALL_LOG);
        } else {
            callLogAdapter = new CallLogAdapter(this, _listCallLogs);
            rv_call_logs = findViewById(R.id.activity_main_rv);
            rv_call_logs.setLayoutManager(new LinearLayoutManager(this));
            getCallLog();
            rv_call_logs.setAdapter(callLogAdapter);
//            bindData();
        }
    }

    // Xử lý kết quả yêu cầu quyền
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCallLog();
            } else {
//                callLogTextView.setText("Quyền bị từ chối");
            }
        }
    }

    // Lấy lịch sử cuộc gọi
    private void getCallLog() {
        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, sortOrder);
        _listCallLogs.clear();

            if (cursor != null) {
            int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
            int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

            while (cursor.moveToNext()) {
                CallLogModel c = new CallLogModel();
                c.setPhNumber(cursor.getString(numberIndex));
                c.setCallType(cursor.getString(typeIndex));
                c.setCallDate(cursor.getString(dateIndex));
                c.setCallDuration(cursor.getString(durationIndex));

                _listCallLogs.add(c);
            }
            cursor.close();
        }
        // Cập nhật RecyclerView sau khi có dữ liệu
        if (callLogAdapter != null) {
            callLogAdapter.notifyDataSetChanged();
        }
    }
}