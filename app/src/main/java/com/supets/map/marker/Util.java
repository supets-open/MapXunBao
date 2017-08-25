package com.supets.map.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.TextView;

import com.supets.map.R;

public class Util {

    public static View getDrawableOnMap(Context context, String driverName, int starts) {
        if (starts < 0 && starts > 5) {
            return null;
        }

        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.layer, null);

        //获取背景图片的宽和高
        Drawable d = context.getResources().getDrawable(R.drawable.search_annotation_red);
        Bitmap b = Bitmap.createBitmap(d.getMinimumWidth(), d.getMinimumHeight(), Bitmap.Config.RGB_565);
        //设置名字
        view.draw(new Canvas(b));
        TextView text = (TextView) view.findViewById(R.id.textview);
        text.setText(driverName);
        //设置评价数
        ImageView img1 = view.findViewById(R.id.imageview_start1);
        ImageView img2 = view.findViewById(R.id.imageview_start2);
        ImageView img3 = view.findViewById(R.id.imageview_start3);
        ImageView img4 = view.findViewById(R.id.imageview_start4);
        ImageView img5 = view.findViewById(R.id.imageview_start5);
        switch (starts) {
            case 1:
                img1.setVisibility(View.VISIBLE);
                break;
            case 2:
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
                break;
            case 3:
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
                img3.setVisibility(View.VISIBLE);
                break;
            case 4:
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
                img3.setVisibility(View.VISIBLE);
                img4.setVisibility(View.VISIBLE);
                break;
            case 5:
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
                img3.setVisibility(View.VISIBLE);
                img4.setVisibility(View.VISIBLE);
                img5.setVisibility(View.VISIBLE);
                break;
        }

        return view;

        //return new BitmapDrawable(getViewBitmap(view));
    }

    private static Bitmap getViewBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
