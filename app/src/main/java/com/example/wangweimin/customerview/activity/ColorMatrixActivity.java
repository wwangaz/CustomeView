package com.example.wangweimin.customerview.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.wangweimin.customerview.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangweimin on 17/2/15.
 */

public class ColorMatrixActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private final static float MID_VALUE = 50f;

    private final static float START_VALUE = 1f;

    private final static int PHOTO_REQUEST_CODE = 1;

    @BindView(R.id.imageView)
    ImageView mImageView;

    @BindView(R.id.saturation_seekbar)
    AppCompatSeekBar mSaturationSeekBar;

    @BindView(R.id.hue_seekbar)
    AppCompatSeekBar mHueSeekBar;

    @BindView(R.id.lum_seekbar)
    AppCompatSeekBar mLumSeekBar;

    private float mSaturation = START_VALUE;

    private float mHue = 0;

    private float mLum = START_VALUE;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_matrix);
        ButterKnife.bind(this);
        mSaturationSeekBar.setOnSeekBarChangeListener(this);
        mHueSeekBar.setOnSeekBarChangeListener(this);
        mLumSeekBar.setOnSeekBarChangeListener(this);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_CODE);
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.saturation_seekbar:
                mSaturation = progress * 1.0f / MID_VALUE;
                break;
            case R.id.hue_seekbar:
                mHue = (progress - MID_VALUE) * 1.0f / MID_VALUE * 180;
                break;
            case R.id.lum_seekbar:
                mLum = progress * 1.0f / MID_VALUE;
                break;
        }
        mImageView.setImageBitmap(handleImageEffect(mBitmap, mHue, mSaturation, mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private Bitmap handleImageEffect(Bitmap bitmap, float hue, float saturation, float lum) {
        if(bitmap != null) {
            Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();

            ColorMatrix hueMatrix = new ColorMatrix();
            hueMatrix.setRotate(0, hue);
            hueMatrix.setRotate(1, hue);
            hueMatrix.setRotate(2, hue);

            ColorMatrix saturationMatrix = new ColorMatrix();
            saturationMatrix.setSaturation(saturation);

            ColorMatrix lumMatrix = new ColorMatrix();
            lumMatrix.setScale(lum, lum, lum, 1);

            ColorMatrix imgMatrix = new ColorMatrix();
            imgMatrix.postConcat(hueMatrix);
            imgMatrix.postConcat(saturationMatrix);
            imgMatrix.postConcat(lumMatrix);

            paint.setColorFilter(new ColorMatrixColorFilter(imgMatrix));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            return bmp;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    mImageView.setImageBitmap(mBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
