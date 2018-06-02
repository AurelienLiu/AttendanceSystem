package com.example.liuxuanchi.project.statistics.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuxuanchi.project.BaseActivity;
import com.example.liuxuanchi.project.MyNavigationView;
import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.SettingActivity;
import com.example.liuxuanchi.project.db.AttendanceInfo;
import com.example.liuxuanchi.project.db.ReachedInfoLitepal;
import com.example.liuxuanchi.project.login.LoginActivity;
import com.example.liuxuanchi.project.peopleManagement.AttendanceInfoAdapter;
import com.example.liuxuanchi.project.peopleManagement.MyListView;
import com.example.liuxuanchi.project.peopleManagement.PeopleInformation;
import com.example.liuxuanchi.project.peopleManagement.PeopleManagement;
import com.example.liuxuanchi.project.util.Utility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class StatisticsActivity extends BaseActivity {

    private PieChart mPieChart;
    private BarChart mBarChart;
    private DrawerLayout mDrawerLayout;
//    private List<ReachedInfoLitepal> infoList = new ArrayList<>();

    public static int choseYear;
    public static int choseMonth;
    public static int choseDay;

    MyListView attendacneList;
    private static AttendanceInfoAdapter adapter1;
    private static List<AttendanceInfo> infoList1;
    private static long choseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_statistics);

        choseDate = System.currentTimeMillis() - (getHourOfToday() + 1) * 60 * 60 * 1000;


        //饼图
        mPieChart = (PieChart)findViewById(R.id.spread_pie_chart);
        PieData mPieData = getPieData();
        showChart(mPieChart,mPieData);

        //柱状图
        mBarChart = (BarChart)findViewById(R.id.spread_bar_chart);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setMaxVisibleValueCount(60);
        mBarChart.setTouchEnabled(true);
        mBarChart.setDragEnabled(true);


        //背景白色
        //mBarChart.setBackgroundColor(Color.WHITE);
        //不要描述
        mBarChart.getDescription().setEnabled(false);
        //不显示比例图
        Legend legend = mBarChart.getLegend();
        legend.setEnabled(false);


        //设置y轴
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 1;i < 17; i++){
            float j = (int)((Math.random())*60);
            barEntries.add(new BarEntry(i,j));
        }




        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //数据显示成整数
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int)value;
                return n+"";
            }
        });
        //数据大小
        barDataSet.setValueTextSize(12f);





        BarData barData = new BarData(barDataSet);
//        barData.setBarWidth(1f);
        mBarChart.setData(barData);

        //设置x轴
        String[] times = new String[]{"8:00","8:15","8:30", "8:45", "9:00","9:15", "9:30", "9:45",
                "10:00","13:30","13:45","14:00","14:15","14:30","14:45","15:00","24:00"};
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(times));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //label放在axe正下方
        xAxis.setCenterAxisLabels(true);
        //设置进入时标签数量
        xAxis.setLabelCount(6);
        //设置最小间距
        //xAxis.setGranularity(1f);
        //动画，往y轴

        //让x轴可以左右滑动
        Matrix m = new Matrix();
        m.postScale(2.6f,1f);
        mBarChart.getViewPortHandler().refresh(m,mBarChart,false);
        mBarChart.animateXY(1000,1000);



        //加载toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_sta);
        setSupportActionBar(toolbar);


        //加载nav和导航按钮HomeAsUp
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_sta);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24px);
        }

//        //nav的菜单项设立监听事件
//        navView.setCheckedItem(R.id.nav_statistics);  //默认选项
//        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_statistics:
//                        mDrawerLayout.closeDrawers();
//                        break;
//                    case R.id.nav_users:
//                        Intent intent = new Intent(MyApplication.getContext(), PeopleManagementActivity.class);
//                        startActivity(intent);
//                        mDrawerLayout.closeDrawers();
//                        finish();
//                        break;
//                    case R.id.nav_about_us:
//                        Intent intent1 = new Intent(MyApplication.getContext(), AboutUsActivity.class);
//                        startActivity(intent1);
//                        mDrawerLayout.closeDrawers();
//                        finish();
//                    default:
//                }
//                return true;
//            }
//        });


        //设置navigationView的点击事件
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.attendance_record);
        MyNavigationView.onSelectItem(navView, StatisticsActivity.this, mDrawerLayout);



        //recyclerView
//        infoList = DataSupport.where("arriveInTime = ?", "0").find(ReachedInfoLitepal.class);
//
//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        InfoAdapter infoAdapter = new InfoAdapter(infoList);
//        recyclerView.setAdapter(infoAdapter);

        /**
         * 后添加：点击中心日期文字来更换查看日期
         */
        Button changeDate = (Button)findViewById(R.id.statistics_change_date);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDatePickerDialog(StatisticsActivity.this, 1, mPieChart, Calendar.getInstance());
            }
        });


        /**************************************************************/

        /**
         * 后添加：考勤列表
         */

        infoList1 = new ArrayList<>();
        //通过AttendanceInfo里的peopleId匹配对应的考勤数据,匹配24小时内数据
        infoList1 = DataSupport
                .where("date>?", "" + choseDate)
                .order("date desc")
                .find(AttendanceInfo.class);
        adapter1 = new AttendanceInfoAdapter(StatisticsActivity.this,
                R.layout.attendance_info_item, infoList1);
        attendacneList = (MyListView)findViewById(R.id.attendance_list);
        attendacneList.setAdapter(adapter1);

        /****************************/
    }

    private int getHourOfToday() {
        String hour = Utility.stampToDate(System.currentTimeMillis(), "HH");
        if ("00".equals(hour)) {
            return 0;
        } else if ("01".equals(hour)) {
            return 1;
        } else if ("02".equals(hour)) {
            return 2;
        } else if ("03".equals(hour)) {
            return 3;
        } else if ("04".equals(hour)) {
            return 4;
        } else if ("05".equals(hour)) {
            return 5;
        } else if ("06".equals(hour)) {
            return 6;
        } else if ("07".equals(hour)) {
            return 7;
        } else if ("08".equals(hour)) {
            return 8;
        } else if ("09".equals(hour)) {
            return 9;
        } else if ("10".equals(hour)) {
            return 10;
        } else if ("11".equals(hour)) {
            return 11;
        } else if ("12".equals(hour)) {
            return 12;
        } else if ("13".equals(hour)) {
            return 13;
        } else if ("14".equals(hour)) {
            return 14;
        } else if ("15".equals(hour)) {
            return 15;
        } else if ("16".equals(hour)) {
            return 16;
        } else if ("17".equals(hour)) {
            return 17;
        } else if ("18".equals(hour)) {
            return 18;
        } else if ("19".equals(hour)) {
            return 19;
        } else if ("20".equals(hour)) {
            return 20;
        } else if ("21".equals(hour)) {
            return 21;
        } else if ("22".equals(hour)) {
            return 22;
        } else if ("23".equals(hour)) {
            return 23;
        } else {
            return 0;
        }
    }

    //柱状图的x轴格式（String类）
    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;
        public MyXAxisValueFormatter(String[] values){
            this.mValues = values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value%mValues.length];
        }
    }



    //toolbar上菜单选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    //piechart处理，将图表与数据关联
    private void showChart(final PieChart pieChart, PieData pieData){
        pieChart.setHoleRadius(60f);//半径
        pieChart.setTransparentCircleRadius(60f);//半透明圈
        pieChart.setDrawCenterText(true);//饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90);//初始旋转角度

        pieChart.setRotationEnabled(true);//可手动旋转

        pieChart.setDrawEntryLabels(true);//设置pieChart显示文字
        pieChart.setEntryLabelColor(Color.WHITE);//颜色
        pieChart.setEntryLabelTextSize(20);//大小

        pieData.setDrawValues(true);//显示数据实体
        pieData.setValueTextSize(20f);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int)value;
                return n+"";
            }
        });


        //中心文字
        Calendar date = Calendar.getInstance();
        int month = date.get(Calendar.MONTH) + 1;
        pieChart.setCenterText(month + "月/" + date.get(Calendar.DATE)+ "日");
        pieChart.setCenterTextSize(30);
        //pieChart.setCenterTextTypeface();字体样式



        pieChart.getDescription().setEnabled(false);

        //设置数据
        pieChart.setData(pieData);

        //不显示比例图
        Legend mLegend = pieChart.getLegend();
        mLegend.setEnabled(false);

        //动画
        pieChart.animateXY(1000, 1000);
    }
    //创建pieData，即图标的数据
    private PieData getPieData(){
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();//yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */

        int quarterly1 = 10;
        int quarterly2 = 10;
        int quarterly3 = 80;


        yValues.add(new PieEntry(quarterly1,""));
        yValues.add(new PieEntry(quarterly2,""));
        yValues.add(new PieEntry(quarterly3,""));


        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues,"Quarterly revenue 2014");//显示在比例图上，但上边设置了不显示比例图

        pieDataSet.setSliceSpace(1f);//设置个饼状图之间的距离
        pieDataSet.setSelectionShift(1f);//被选中时变化距离


        ArrayList<Integer> colors = new ArrayList<Integer>();
        //饼图颜色
        colors.add(Color.rgb(155,155,155));
        colors.add(Color.rgb(129,231,45));
        colors.add(Color.rgb(130,210,249));


        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }

    /**
     * 改变日期后执行更改日期、数据等一系列操作
     * 选择的年份为choseYear、choseMonth、choseDay
     */
    public static void changeStatisticsByDate(PieChart pieChart){
        Log.d("111111", "changeStatisticsByDate: "+ choseYear + "    " + choseMonth + choseDay);
        pieChart.setCenterText(choseMonth + "月/" + choseDay + "日");

        choseDate = Utility.dateToTimeStamp("2018-" + choseMonth + "-" + choseDay + " 00:00:00");
        updateList(choseDate);
    }

    private static void updateList(long choseDate) {
        infoList1.clear();
        infoList1.addAll(DataSupport
                .where("date>? AND date<?", "" + choseDate, "" + (choseDate + 24 * 60 * 60 * 1000))
                .order("date asc")
                .find(AttendanceInfo.class));
        adapter1.notifyDataSetChanged();

    }


}
