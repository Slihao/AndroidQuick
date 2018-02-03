package com.sdwfqin.quicklib.module.seeimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quicklib.base.Constants;

import java.io.File;

/**
 * 描述：显示图片
 *
 * @author zhangqin
 * @date 2017/8/7
 */
public class SeeImageFragment extends BaseFragment {

    private PhotoView mShowimageImg;
    private ProgressBar mProgressBar;
    private String url;
    private int position;

    public static SeeImageFragment newInstance(String s, int position) {

        Bundle bundle = new Bundle();
        bundle.putString("url", s);
        bundle.putInt("position", position);
        SeeImageFragment fragment = new SeeImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_see_image;
    }

    @Override
    protected void initEventAndData() {

        mShowimageImg = mView.findViewById(R.id.showimage_img);
        mProgressBar = mView.findViewById(R.id.progressBar);

        if (getArguments() != null) {
            url = getArguments().getString("url");
            position = getArguments().getInt("position", 0);
            LogUtils.i("initEventAndData: " + url);
        }

        Glide
                .with(mContext)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mShowimageImg.setImageBitmap(resource);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

        // setOnLongClickListener中return的值决定是否在长按后再加一个短按动作
        // true为不加短按,false为加入短按
        mShowimageImg.setOnLongClickListener(view -> {
            saveImage();
            return false;
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initMenuClick();
    }

    /**
     * 使用Activity中toolbar的menu
     */
    private void initMenuClick() {
        SeeImageActivity activity = (SeeImageActivity) getActivity();
        if (activity != null){
            activity.mSave.setOnClickListener(v -> saveImage());
        }
    }

    /**
     * 保存图片
     */
    public void saveImage() {
        if (SDCardUtils.isSDCardEnable()) {
            BitmapDrawable bitmapDrawable = null;
            try {
                bitmapDrawable = (BitmapDrawable) mShowimageImg.getDrawable();
                String file = Constants.SAVE_REAL_PATH + url.substring(url.lastIndexOf("/"));
                if (FileUtils.createOrExistsFile(file)) {
                    byte[] bitmap2Bytes = ImageUtils.bitmap2Bytes(bitmapDrawable.getBitmap(), Bitmap.CompressFormat.JPEG);
                    if (FileIOUtils.writeFileFromBytesByStream(file, bitmap2Bytes)) {
                        Snackbar.make(mView, "图片已保存至：" + Constants.SAVE_REAL_PATH, Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(new File(Constants.SAVE_REAL_PATH));
                        intent.setData(uri);
                        mContext.sendBroadcast(intent);
                    } else {
                        throw new Exception("写入失败");
                    }
                } else {
                    throw new Exception("路径不存在或创建失败");
                }
            } catch (Exception e) {
                LogUtils.e(e);
                showMsg(e.getMessage());
            }
        } else {
            showMsg("内存卡不可用");
        }
    }
}
