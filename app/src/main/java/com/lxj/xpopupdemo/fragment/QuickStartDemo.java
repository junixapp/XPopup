package com.lxj.xpopupdemo.fragment;

import android.view.View;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopupdemo.R;

/**
 * Description:
 * Create by lxj, at 2018/12/11
 */
public class QuickStartDemo extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_quickstart;
    }

    @Override
    public void init(View view) {
        view.findViewById(R.id.btnShowConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnShowInputConfirm).setOnClickListener(this);
        view.findViewById(R.id.btnShowAttachList).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnShowConfirm:
                XPopup.get(getContext()).asConfirm("我是标题", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                               toast("click confirm");
                            }
                        })
                        .show();
                break;
            case R.id.btnShowInputConfirm:
                XPopup.get(getContext()).asInputConfirm("我是标题", "请输入内容。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                toast("input text: " + text);
                            }
                        })
                        .show();
                break;
            case R.id.btnShowAttachList:
                XPopup.get(getActivity()).asAttachList(new String[]{"分享", "编辑", "删除"},
                        new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher},
                        -100, 0,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click "+text);
                            }
                        })
                        .atView(getView().findViewById(R.id.btnShowAttachList))
                        .show();
                break;
        }
    }
}
