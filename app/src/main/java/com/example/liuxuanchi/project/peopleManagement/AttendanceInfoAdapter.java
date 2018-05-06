package com.example.liuxuanchi.project.peopleManagement;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.AttendanceInfo;
import com.example.liuxuanchi.project.util.Utility;

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
        FrameLayout layout = (FrameLayout)view.findViewById(R.id.att_layout);
        if (info.isAbsence()) {
            dateView.setText(Utility.stampToDate(info.getDate(), "yyyy-MM-dd") + "    缺勤");
            layout.setBackgroundColor(Color.RED);
        } else if (Utility.isLateOrGoEarly(parent.getContext(), info.getDate())) {
            dateView.setText(Utility.stampToDate(info.getDate()));
            layout.setBackgroundColor(Color.YELLOW);
        } else {
            dateView.setText(Utility.stampToDate(info.getDate()));
        }
        return view;
    }


}
