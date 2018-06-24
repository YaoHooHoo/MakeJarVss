package com.oit.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by john
 * time: 2017-03-28
 * desc: ${TODO}
 */

public abstract class CommonDialog {
    private Dialog mDialog;
    private Window mDialogWindow;
    private DialogViewHolder dialogVh;
    private View mRootView;

    public CommonDialog(Context context, int layoutId) {
        if (dialogVh == null)
            dialogVh = DialogViewHolder.get(context, layoutId);
        mRootView = dialogVh.getConvertView();
        if (mDialog == null)
            mDialog = new Dialog(context);
        mDialog.setContentView(mRootView);
        mDialogWindow = mDialog.getWindow();
        convert(dialogVh);
    }

    public CommonDialog(Context context, int layoutId, @StyleRes int themeResId) {
        if (dialogVh == null)
            dialogVh = DialogViewHolder.get(context, layoutId);
        mRootView = dialogVh.getConvertView();
        if (mDialog == null)
            mDialog = new Dialog(context, themeResId);
        mDialog.setContentView(mRootView);
        mDialogWindow = mDialog.getWindow();
        convert(dialogVh);
    }

    public Dialog getDialog(){
        return mDialog;
    }

    /**
     * B
     * 把弹出框view holder传出去
     */
    public abstract void convert(DialogViewHolder holder);

    public DialogViewHolder getDialogVh() {
        return dialogVh;
    }

    public static AlertDialog.Builder creatNormalDialogBuilder(Context context, String title, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message);
    }

    /**
     * 显示dialog
     */
    public CommonDialog showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
        return this;
    }

    /**
     * @param light 弹出时背景亮度 值为0.0~1.0    1.0表示全黑  0.0表示全白
     * @return
     */
    public CommonDialog backgroundLight(double light) {
        if (light < 0.0 || light > 1.0)
            return this;
        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.dimAmount = (float) light;
        mDialogWindow.setAttributes(lp);
        return this;
    }

    public CommonDialog showCustomPosition(int x, int y) {
        WindowManager.LayoutParams wl = mDialogWindow.getAttributes();
        mDialogWindow.setGravity(Gravity.BOTTOM);
        wl.x = x;
        wl.y = y;
        mDialogWindow.setAttributes(wl);
        return this;
    }


    /**
     * @param style 显示一个Dialog自定义一个弹出方式  具体怎么写 可以模仿上面的
     * @return
     */
    public CommonDialog showDialog(@StyleRes int style) {
        mDialogWindow.setWindowAnimations(style);
        mDialog.show();
        return this;
    }

    /**
     * 全屏显示
     */
    public CommonDialog fullScreen() {
        WindowManager.LayoutParams wl = mDialogWindow.getAttributes();
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }


    public CommonDialog setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        mDialog.setOnKeyListener(onKeyListener);
        return this;
    }

    /**
     * 全屏宽度
     */
    public CommonDialog fullWidth() {
        WindowManager.LayoutParams wl = mDialogWindow.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * 全屏高度
     */
    public CommonDialog fullHeight() {
        WindowManager.LayoutParams wl = mDialogWindow.getAttributes();
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * @param width  自定义的宽度
     * @param height 自定义的高度
     * @return
     */
    public CommonDialog setWidthAndHeight(int width, int height) {
        WindowManager.LayoutParams wl = mDialogWindow.getAttributes();
        wl.width = width;
        wl.height = height;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * 设置宽度
     *
     * @param width
     * @return
     */
    public CommonDialog setWidth(int width) {
        WindowManager.LayoutParams wl = mDialogWindow.getAttributes();
        wl.width = width;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * 设置高度
     *
     * @param height
     * @return
     */
    public CommonDialog setHeight(int height) {
        WindowManager.LayoutParams wl = mDialogWindow.getAttributes();
        wl.height = height;
        mDialog.onWindowAttributesChanged(wl);
        return this;
    }

    /**
     * cancel dialog
     */
    public void cancelDialog() {
        if (mDialog != null && mDialog.isShowing())
            dismiss();
    }

    /**
     * cancel dialog
     */
    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 设置监听
     */
    public CommonDialog setDialogDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
        return this;
    }

    /**
     * 设置监听
     */
    public CommonDialog setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mDialog.setOnCancelListener(listener);
        return this;
    }

    /**
     * 设置是否能取消
     */
    public CommonDialog setCancelAble(boolean cancel) {
        mDialog.setCancelable(cancel);
        return this;
    }


    /**
     * 设置触摸其他地方是否能取消
     */
    public CommonDialog setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
}
