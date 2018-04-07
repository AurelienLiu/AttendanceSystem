package com.example.liuxuanchi.project.peopleManagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.AttendanceInfo;

import java.util.List;

/**
 * Created by liuxuanchi on 2018/3/31.
 */

public class AttendanceInfoAdapter extends ArrayAdapter<AttendanceInfo> {

    private int resourceId;

    public AttendanceInfoAdapter(Context context, int viewResourceId, List<AttendanceInfo> list) {
        super(context, viewResourceId, list);
        resourceId = viewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AttendanceInfo info = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView dateView = (TextView)view.findViewById(R.id.att_date);
        TextView timeView = (TextView)view.findViewById(R.id.att_time);
        ImageButton button = (ImageButton)view.findViewById(R.id.att_button);
        dateView.setText(info.getDate());
        timeView.setText(info.getTimeRange());
        setAttButton(button, info);
        return view;
    }

    private void setAttButton(ImageButton button, AttendanceInfo info) {
        if (info.getLateTime() < 0 && info.getLeaveEarlyTime() < 0 && (!info.isAbsence())) {
            button.setImageResource(R.drawable.green_color);
        } else if (info.isAbsence()) {
            button.setImageResource(R.drawable.red_color);
        } else {
            button.setImageResource(R.drawable.yellow_color);
        }
    }
}
