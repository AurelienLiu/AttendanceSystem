package com.example.liuxuanchi.project.statistics.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.liuxuanchi.project.MyNavigationView;
import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.ReachedInfoLitepal;
import com.example.liuxuanchi.project.peopleManagement.PeopleManagement;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class StatisticsActivity extends AppCompatActivity {

    private PieChart mPieChart;
    private BarChart mBarChart;
    private DrawerLayout mDrawerLayout;
    private List<ReachedInfoLitepal> infoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_statistics);


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
        mBarChart.setTouchEnabled(false);
        //背景白色
        //mBarChart.setBackgroundColor(Color.WHITE);
        //不要描述
        mBarChart.getDescription().setEnabled(false);
        //不显示比例图
        Legend legend = mBarChart.getLegend();
        legend.setEnabled(false);


        //设置y轴
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,1f));
        barEntries.add(new BarEntry(2,20f));
        barEntries.add(new BarEntry(3,59f));
        barEntries.add(new BarEntry(4,10f));
        barEntries.add(new BarEntry(5,2f));
        barEntries.add(new BarEntry(6,8f));
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
        barData.setBarWidth(0.9f);
        mBarChart.setData(barData);

        //设置x轴
        String[] times = new String[]{"8:00","8:15","8:30", "8:45", "9:00", "9:15", "9:30","9:45"};
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(times));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        //label放在axe正下方
        xAxis.setCenterAxisLabels(true);
        //设置最小间距
        //xAxis.setGranularity(1f);
        //动画，往y轴
        mBarChart.animateY(1500);




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
        infoList = DataSupport.where("arriveInTime = ?", "0").find(ReachedInfoLitepal.class);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        InfoAdapter infoAdapter = new InfoAdapter(infoList);
        recyclerView.setAdapter(infoAdapter);


    }

    //柱状图的x轴格式（String类）
    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;
        public MyXAxisValueFormatter(String[] values){
            this.mValues = values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
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
    private void showChart(PieChart pieChart, PieData pieData){
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
}




