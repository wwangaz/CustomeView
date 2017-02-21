package com.example.wangweimin.customerview.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.view.zoomHeader.FlagImage;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangweimin on 17/2/15.
 */

public class ColorMatrixActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private final static float MID_VALUE = 50f;

    private final static float START_VALUE = 1f;

    private final static int PHOTO_REQUEST_CODE = 1;

    @BindView(R.id.content)
    LinearLayout contentLayout;

    @BindView(R.id.imageView)
    ImageView mImageView;

    @BindView(R.id.saturation_seekbar)
    AppCompatSeekBar mSaturationSeekBar;

    @BindView(R.id.hue_seekbar)
    AppCompatSeekBar mHueSeekBar;

    @BindView(R.id.lum_seekbar)
    AppCompatSeekBar mLumSeekBar;

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                mImageView.setImageBitmap(handleImageNegative(mBitmap));
                break;
            case R.id.button2:
                mImageView.setImageBitmap(handleImageFade(mBitmap));
                break;
            case R.id.button3:
                mImageView.setImageBitmap(handleImageRelief(mBitmap));
                break;
            case R.id.button4:
                handleImageFlag(mBitmap);
                break;
        }
    }

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
        if (bitmap != null) {
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

    private Bitmap handleImageNegative(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int color;
            int r, g, b, a;

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int[] oldPx = new int[width * height];
            int[] newPx = new int[width * height];
            bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);

            for (int i = 0; i < width * height; i++) {
                color = oldPx[i];
                r = Color.red(color);
                g = Color.green(color);
                b = Color.blue(color);
                a = Color.alpha(color);

                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                checkColor(r);
                checkColor(g);
                checkColor(b);

                newPx[i] = Color.argb(a, r, g, b);
            }
            bmp.setPixels(newPx, 0, width, 0, 0, width, height);
            return bmp;
        }
        return null;
    }

    private Bitmap handleImageFade(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int color;
            int r, g, b, a;
            int r1, g1, b1;

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int[] oldPx = new int[width * height];
            int[] newPx = new int[width * height];
            bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);

            for (int i = 0; i < width * height; i++) {
                color = oldPx[i];
                r = Color.red(color);
                g = Color.green(color);
                b = Color.blue(color);
                a = Color.alpha(color);

                r1 = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                g1 = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                b1 = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                checkColor(r1);
                checkColor(g1);
                checkColor(b1);

                newPx[i] = Color.argb(a, r1, g1, b1);
            }
            bmp.setPixels(newPx, 0, width, 0, 0, width, height);
            return bmp;
        }
        return null;
    }

    private Bitmap handleImageRelief(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int color;
            int r, g, b, a;
            int rp = 0, gp = 0, bp = 0;
            int r1, g1, b1;

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int[] oldPx = new int[width * height];
            int[] newPx = new int[width * height];
            bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);

            for (int i = 0; i < width * height; i++) {
                color = oldPx[i];
                r = Color.red(color);
                g = Color.green(color);
                b = Color.blue(color);
                a = Color.alpha(color);

                r1 = rp - r + 127;
                g1 = gp - g + 127;
                b1 = bp - b + 127;

                checkColor(r1);
                checkColor(g1);
                checkColor(b1);

                rp = r;
                gp = g;
                bp = b;

                newPx[i] = Color.argb(a, r1, g1, b1);
            }
            bmp.setPixels(newPx, 0, width, 0, 0, width, height);
            return bmp;
        }
        return null;
    }

    private void handleImageFlag(Bitmap bitmap) {
        if (bitmap != null) {
            FlagImage image = new FlagImage(this, bitmap);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
            contentLayout.addView(image, 0, params);
        }
    }

    private int checkColor(int color) {
        if (color > 255)
            color = 255;
        else if (color < 0)
            color = 0;
        return color;
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
