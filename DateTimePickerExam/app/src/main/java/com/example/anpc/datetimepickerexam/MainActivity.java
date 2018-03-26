package com.example.anpc.datetimepickerexam;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText edtCV, edtND;
    private TextView tvShowDate, tvShowTime;
    private Button btnDate, btnTime, btnAddCV;
    private ListView listView;
    private ArrayAdapter mAdapter;
    private ArrayList<String> mData;
    private Date dateFinish;
    private Date timeFinish;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFormWidgets();
        getDefaultInfor();
        addEventFormWidgets();
    }
    public void getFormWidgets(){
        edtCV = findViewById(R.id.edtCV);
        edtND = findViewById(R.id.edtND);
        tvShowDate = findViewById(R.id.tvShowDate);
        tvShowTime = findViewById(R.id.tvShowTime);
        btnAddCV = findViewById(R.id.btnAddCV);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        listView = findViewById(R.id.listView);

        mData = new ArrayList<>();
        mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mData);
        listView.setAdapter(mAdapter);
    }
    public void getDefaultInfor()
    {
        cal=Calendar.getInstance();
        SimpleDateFormat dft=null;
        dft=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        tvShowDate.setText(strDate);
        dft=new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String strTime=dft.format(cal.getTime());
        tvShowTime.setText(strTime);
        dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
        tvShowTime.setTag(dft.format(cal.getTime()));

        edtCV.requestFocus();
        //gán cal.getTime() cho ngày hoàn thành và giờ hoàn thành
        dateFinish=cal.getTime();
        timeFinish=cal.getTime();
    }
    public void addEventFormWidgets()
    {
        btnDate.setOnClickListener(new MyButtonEvent());
        btnTime.setOnClickListener(new MyButtonEvent());
        btnAddCV.setOnClickListener(new MyButtonEvent());
        listView.setOnItemClickListener(new MyListViewEvent());
        listView.setOnItemLongClickListener(new MyListViewEvent());
    }
    private class MyButtonEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnDate:
                    showDatePickerDialog();
                    break;
                case R.id.btnTime:
                    showTimePickerDialog();
                    break;
                case R.id.btnAddCV:
                    processAddJob();
                    break;
            }
        }
    }
    public void showDatePickerDialog()
    {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                tvShowDate.setText((dayOfMonth) +"/"+(monthOfYear+1)+"/"+year);
                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish=cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=tvShowDate.getText()+"";
        String strArrtmp[]=s.split("/");
        int ngay=Integer.parseInt(strArrtmp[0]);
        int thang=Integer.parseInt(strArrtmp[1])-1;
        int nam=Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(MainActivity.this, callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày hoàn thành");
        pic.show();
    }
    public void showTimePickerDialog()
    {
        TimePickerDialog.OnTimeSetListener callback=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Xử lý lưu giờ và AM,PM
                String s=hourOfDay +":"+minute;
                int hourTam=hourOfDay;
                if(hourTam>12)
                    hourTam=hourTam-12;
                tvShowTime.setText
                        (hourTam +":"+minute +(hourOfDay>12?" PM":" AM"));
                //lưu giờ thực vào tag
                tvShowTime.setTag(s);
                //lưu vết lại giờ vào hourFinish
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                timeFinish = cal.getTime();
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong TimePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s = tvShowTime.getTag()+"";
        String strArr[]=s.split(":");
        int gio = Integer.parseInt(strArr[0]);
        int phut = Integer.parseInt(strArr[1]);
        TimePickerDialog time=new TimePickerDialog(MainActivity.this, callback, gio, phut, true);
        time.setTitle("Chọn giờ hoàn thành");
        time.show();
    }
    private void processAddJob(){
        String str = edtCV.getText() + " - " + tvShowDate.getText() + " - " + tvShowTime.getText() + ".";
        mData.add(str);
        mAdapter.notifyDataSetChanged();
    }
    private class MyListViewEvent implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
            mData.remove(position);
            mAdapter.notifyDataSetChanged();
            return false;
        }
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
            Toast.makeText(MainActivity.this,mData.get(position), Toast.LENGTH_LONG).show();
        }

    }
}
