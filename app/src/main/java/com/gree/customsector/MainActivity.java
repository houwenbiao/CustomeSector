package com.gree.customsector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gree.customsector.Util.Tools;

public class MainActivity extends AppCompatActivity implements SectorView.SelectedListener
{

    private SectorView mSectorView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSectorView = (SectorView) findViewById(R.id.sv_rf);
        mSectorView.setSelectedListener(this);
    }

    @Override
    public void onSelectedListener(int i)
    {
        Tools.showToast(this, "左右扫风当前选中第：" + i + "个", false);
    }
}
