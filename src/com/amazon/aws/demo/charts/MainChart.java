package com.amazon.aws.demo.charts;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.List;

import com.amazon.aws.demo.GetbucketName;
import com.amazon.aws.demo.R;



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainChart extends Activity {
	List<PieDetailsItem> piedata=new ArrayList<PieDetailsItem>(0);
	TextView t1,t2,t3,t4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
       PieDetailsItem item;
       int maxCount=0;
        int itemCount=0;
        int usedsize=20480-Integer.parseInt(GetbucketName.getSize());
        int remainsize=20480-usedsize;
        int items[]={remainsize,usedsize};
        int colors[]={-6777216,-16776961};
        t1=(TextView) findViewById(R.id.total);
        t2=(TextView) findViewById(R.id.used);
        t3=(TextView) findViewById(R.id.remain);
        t4=(TextView) findViewById(R.id.days);
        t1.setText("Total Bucket Size             : 20480 Bytes");
        t2.setText("Used Bucket Size             : " +usedsize+" Bytes");
        t3.setText("Remaining Bucket Size    : "+remainsize+" Bytes");
        t4.setText("Your Account Will Expired On  : "+GetbucketName.getDate());
       String itemslabel[]={" vauesr ur 100"," vauesr ur 200"};
        for(int i=0;i<items.length;i++)
        {
        itemCount=items[i];
        item=new PieDetailsItem();
        item.count=itemCount;
        item.label=itemslabel[i];
        item.color=colors[i];
        piedata.add(item);
       maxCount=maxCount+itemCount;
        }
        int size=155;
        int BgColor=0xffa11b1;
       Bitmap mBaggroundImage=Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
        View_PieChart piechart=new View_PieChart(this);
        piechart.setLayoutParams(new LayoutParams(size,size));
        piechart.setGeometry(size, size, 2, 2, 2, 2, 2130837504);
        piechart.setSkinparams(BgColor);
        piechart.setData(piedata, maxCount);
        piechart.invalidate();
        piechart.draw(new Canvas(mBaggroundImage));
        piechart=null;
        ImageView mImageView=new ImageView(this);
        mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        mImageView.setBackgroundColor(BgColor);
        mImageView.setImageBitmap(mBaggroundImage);
        LinearLayout finalLayout=(LinearLayout)findViewById(R.id.pie_container);
        finalLayout.addView(mImageView);
    }
}