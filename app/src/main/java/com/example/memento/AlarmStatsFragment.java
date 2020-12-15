package com.example.memento;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmStatsFragment extends Fragment{

    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    Integer alarmId;
    List<Boolean> last20;
    List<Timestamp> monthlyData;
    Integer last20ratio;

    DatabaseReference db;

    private PieChart pieChart;
    private BarChart barChart;


    public AlarmStatsFragment(){
        //Public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmId = getArguments().getInt("alarmId");

        FirebaseFirestore.getInstance().collection("Stats").document(alarmId.toString()).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();

                            //Récupération des 20 dernières occurences pour le PieChart
                            last20 = (List<Boolean>) document.get("last20");
                            System.out.println(last20);
                            PieData pieData = createPieData();
                            preparePieChartData(pieData);

                            //Récupération des données mensuelles pour le BarChart
                            monthlyData = (List<Timestamp>) document.get("monthlyData");
                            if(monthlyData != null){
                                System.out.println(monthlyData.get(0));
                                BarData barData = createBarData();
                                prepareBarChartData(barData);
                            }
                        }
                    }
                }
        );

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.alarmstats, container, false);
        pieChart = view.findViewById(R.id.fragment_pieChart_chart);
        barChart = view.findViewById(R.id.fragment_barChart_chart);

        return view;
    }

    private BarData createBarData(){
        int[] dataCount = new int[12];

        int year = Calendar.getInstance().get(Calendar.YEAR);

        Calendar cal = Calendar.getInstance();

        for(Integer i : dataCount){
            dataCount[i] = 0;
        }

        ArrayList<BarEntry> chartValues = new ArrayList<>();

        for(Timestamp date : monthlyData){
            cal.setTimeInMillis(date.getSeconds()*1000);
            if(cal.get(Calendar.YEAR) == year)
                dataCount[date.toDate().getMonth()] += 1;
        }

        System.out.println(dataCount.length);

        for(int i = 0; i < dataCount.length; i++){
            chartValues.add(new BarEntry(i, dataCount[i]));
        }

        BarDataSet set = new BarDataSet(chartValues, "Test");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        set.setColor(ColorTemplate.MATERIAL_COLORS[0]);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.6f);

        return data;
    }

    private void prepareBarChartData(BarData data){
        XAxis xAxis = barChart.getXAxis();
        YAxis axisLeft = barChart.getAxisLeft();
        YAxis axisRight = barChart.getAxisRight();

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return MONTHS[(int) value];
            }
        });

        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""+(int)value;
            }
        });

        axisLeft.setAxisMinimum(0);
        axisLeft.setGranularity(5);
        axisLeft.setDrawLabels(false);
        axisRight.setAxisMinimum(0);
        axisRight.setDrawLabels(false);
        axisRight.setDrawGridLines(false);

        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1);

        barChart.setPinchZoom(true);
        barChart.setScaleYEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawValueAboveBar(true);
        barChart.animateY(1000);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setExtraOffsets(10, 10, 10, 10);

        data.setValueTextColor(Color.rgb(10, 10, 10));
        data.setValueTextSize(13f);

        barChart.setData(data);
        barChart.invalidate();
    }

    private PieData createPieData(){
        float ratioA = 0;
        float ratioB = 0;

        if(last20 != null){
            for(Boolean b : last20){
                if(b)
                    ratioA += 1;
                else
                    ratioB += 1;
            }
            last20ratio = Math.round(100*ratioA/last20.size());
        }

        ArrayList<PieEntry> values = new ArrayList<>();

        values.add(new PieEntry(ratioA, 0));
        values.add(new PieEntry(ratioB, 1));
        PieDataSet dataSet = new PieDataSet(values, "Percentage");

        dataSet.setSliceSpace(10f);
        dataSet.setDrawValues(false);
        dataSet.setColors(new int[] {ColorTemplate.MATERIAL_COLORS[0], ColorTemplate.MATERIAL_COLORS[2]});

        PieData data = new PieData(dataSet);

        return data;
    }

    private void preparePieChartData(PieData data){
        String str1;
        String str2 = "de complétion parmi\nles 20 dernières fois";

        if(last20ratio != null){
            str1 = last20ratio.toString() + "%\n";
        }else{
            str1 = "0%\n";
        }

        SpannableStringBuilder sb = new SpannableStringBuilder(str1 + str2);
        ForegroundColorSpan fcs1 = new ForegroundColorSpan(ColorTemplate.MATERIAL_COLORS[0]);
        ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.BLACK);
        sb.setSpan(fcs1, 0, str1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new RelativeSizeSpan(5f), 0, str1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(fcs2, str1.length(), str2.length()+str1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        pieChart.setCenterText(sb);

        pieChart.getLegend().setEnabled(false);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000, Easing.EaseInOutQuad);
        pieChart.setRotationEnabled(false);
        pieChart.setExtraOffsets(10, 0, 10, 10);
        pieChart.setHoleRadius(75);
        pieChart.setTransparentCircleRadius(80);
        pieChart.invalidate();
    }
}
