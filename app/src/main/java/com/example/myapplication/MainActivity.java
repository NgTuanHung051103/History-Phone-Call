package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.telecom.Call;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
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
        setContentView(R.layout.layout_call_log);
        _listCallLogs = new ArrayList<CallLogModel>();
        callLogTextView = findViewById(R.id.call_log_text_view);

        // Kiểm tra quyền
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    REQUEST_CODE_READ_CALL_LOG);
        } else {
            // Quyền đã được cấp, lấy lịch sử cuộc gọi
            getCallLog();
            bindData();
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
                callLogTextView.setText("Quyền bị từ chối");
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
    }

    private void bindData(){
        StringBuilder callLogBuilder = new StringBuilder();

        for(CallLogModel c : _listCallLogs){
            callLogBuilder.append("Số: ").append(c.getPhNumber())
                .append("\nLoại cuộc gọi: ").append(formatCallType(c.getCallType()))
                .append("\nThời gian: ").append(Extension.getFormattedDate(c.getCallDate()))
                .append("\nThời lượng: ").append(c.getCallDuration()).append(" giây\n\n");
        }
        callLogTextView.setText(callLogBuilder.toString());
    }

    private String formatCallType(String callType){
        int callTypeCode = Integer.parseInt(callType);

        switch (callTypeCode) {
            case CallLog.Calls.OUTGOING_TYPE:
                callType = "Outgoing";
                break;
            case CallLog.Calls.INCOMING_TYPE:
                callType = "Incoming";
                break;
            case CallLog.Calls.MISSED_TYPE:
                callType = "Missed";
                break;
        }
        return callType;
    }
}