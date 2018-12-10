package com.lxj.xpopup.enums;

/**
 * Description:
 * Create by dance, at 2018/12/9
 */
public enum PopupAnimation {
    // 缩放+透明渐变
    ScaleFromCenter, // 从中心进行缩放+透明渐变
    ScaleFromLeftTop, //从左上角进行缩放+透明渐变
    ScaleFromRightTop, //从右上角进行缩放+透明渐变
    ScaleFromLeftBottom, //从左下角进行缩放+透明渐变
    ScaleFromRightBottom, //从右下角进行缩放+透明渐变

    // 平移
    TranslateFromLeft,  // 从左平移进入
    TranslateFromRight, // 从右平移进入
    TranslateFromTop,   // 从上方平移进入
    TranslateFromBottom, // 从下方平移进入
}
