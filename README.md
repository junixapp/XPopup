## XPopup
![](https://api.bintray.com/packages/li-xiaojun/jrepo/xpopup/images/download.svg)  ![](https://img.shields.io/badge/author-li--xiaojun-brightgreen.svg)  ![](https://img.shields.io/hexpm/l/plug.svg)


功能强大，UI简洁，交互优雅的通用弹窗！可以替代Dialog，PopupWindow，PopupMenu，BottomSheet，DrawerLayout效果等组件，自带十几种效果良好的动画，
支持完全的UI和动画自定义！

编写本库的初衷有以下几点：
1. 项目有这样常见需求：中间和底部弹出甚至可拖拽的对话框，指定位置的PopupMenu或者PopupWindow，指定区域阴影的弹出层效果
2. 市面上已有的类库要么功能不足够，要么交互效果不完美，有着普遍的缺点，就像BottomSheet存在的问题一样。比如：窗体消失的动画和背景渐变动画不一致，窗体消失后半透明背景仍然停留一会儿

**设计思路**：
综合常见的弹窗场景，我将其分为几类：
1. Center类型，就是在中间弹出的弹窗，比如确认和取消弹窗，Loading弹窗
2. Bottom类型，就是从页面底部弹出，比如从底部弹出的分享窗体，知乎的从底部弹出的评论列表
3. Attach类型，就是弹窗的位置需要依附于某个View或者某个触摸点，就像系统的PopupMenu效果一样，但PopupMenu的自定义性很差
4. DrawerLayout类型，就是从窗体的坐边或者右边弹出，并支持手势拖拽；好处是与界面解耦，可以在任何界面显示DrawerLayout

尽管我已经内置了几种常见弹窗的实现，但不可能满足所有的需求，你很可能需要自定义；你自定义的弹窗类型应该属于这几种之一。

**动画设计**：
为了增加交互的趣味性，遵循Material Design，在设计动画的时候考虑了很多细节，过渡，层级的变化。具体可以从Demo中感受。


## ScreenShot

![](screenshot/preview.gif) ![](screenshot/preview_attach.gif)

![](screenshot/preview_drawer.gif) ![](screenshot/preview_part.gif)

![](screenshot/preview2.gif) ![](screenshot/preview3.gif)

![](screenshot/preview4.gif)

## 体验Demo
直接扫码二维码下载安装，快速体验。
![](data:image/png;base64,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)

## 使用
首先需要添加Gradle依赖：
```groovy
implementation 'com.lxj:xpopup:latest release'
```

为了方便使用，已经内置了几种常见弹窗的实现：
1. **显示确认和取消对话框**
    ```java
    XPopup.get(getContext()).asConfirm("我是标题", "我是内容",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                   toast("click confirm");
                                }
                            })
                            .show();
    ```
2. **显示带输入框的确认和取消对话框**
    ```java
    XPopup.get(getContext()).asInputConfirm("我是标题", "请输入内容。",
                            new OnInputConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    toast("input text: " + text);
                                }
                            })
                            .show();
    ```
3. **显示中间弹出的列表弹窗**
    ```java
    XPopup.get(getActivity()).asCenterList("请选择一项",new String[]{"条目1", "条目2", "条目3", "条目4"},
                            // null, /** 图标Id数组，可无 **/
                            // 1,    /** 选中的position，默认没有选中效果 **/
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    toast("click "+text);
                                }
                            })
                            .show();
    ```
4. **显示中间弹出的加载框**
    ```java
    XPopup.get(getActivity()).asLoading().show();
    ```
5. **显示从底部弹出的列表弹窗**
    ```java
    XPopup.get(getActivity()).asBottomList("请选择一项",new String[]{"条目1", "条目2", "条目3", "条目4","条目5"},
                            // null, /** 图标Id数组，可无 **/
                            // 1,    /** 选中的position，默认没有选中效果 **/
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    toast("click "+text);
                                }
                            })
                            .show();
    ```
6. **显示依附于某个View或者某个点的弹窗**
    ```java
    XPopup.get(getActivity()).asAttachList(new String[]{"分享", "编辑", "不带icon"},
                            new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher},
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    toast("click "+text);
                                }
                            })
                            .atView(v)  // 如果是要依附某个View，必须设置
                            .show();
    ```
    如果是想依附于某个View的触摸点，则需要先`watch`该View，然后当单击或长按触发的时候去显示：
    ```java
    // 必须在事件发生前，调用这个方法来监视View的触摸
    XPopup.get(getActivity()).watch(view);
    view.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            XPopup.get(getActivity()).asAttachList(new String[]{"置顶", "复制", "删除"},null,
                    new OnSelectListener() {
                        @Override
                        public void onSelect(int position, String text) {
                            toast("click "+text);
                        }
                    })
                    // 注意：已经监视了View的触摸点，无需调用atView()方法
                    .show();
            return false;
        }
    });
    ```

7. **关闭弹窗**
    ```java
    XPopup.get(getContext()).dismiss();
    ```

8. **自定义弹窗**

    当你自定义弹窗的时候，需要选择继承`CenterPopupView`，`BottomPopupView`，`AttachPopupView`，`DrawerPopupView`，`PartShadowPopupView`其中之一。假设需要自定义Center类型的弹窗：
    ```java
    class CustomPopup extends CenterPopupView{
            public CustomPopup(@NonNull Context context) {
                super(context);
            }
            // 返回自定义弹窗的布局
            @Override
            protected int getImplLayoutId() {
                return R.layout.custom_popup;
            }
            // 执行初始化操作，比如：findView，设置点击，或者任何你弹窗内的业务逻辑
            @Override
            protected void initPopupContent() {
                super.initPopupContent();
                findViewById(R.id.tv_close).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss(); // 关闭弹窗
                    }
                });
            }
            // 设置最大宽度，看需要而定
            @Override
            protected int getMaxWidth() {
                return super.getMaxWidth();
            }
            // 设置最大高度，看需要而定
            @Override
            protected int getMaxHeight() {
                return super.getMaxHeight();
            }
            // 设置自定义动画器，看需要而定
            @Override
            protected PopupAnimator getPopupAnimator() {
                return super.getPopupAnimator();
            }
        }
    ```
    使用自定义弹窗：
    ```java
    XPopup.get(getContext())
            .asCustom(new CustomPopup(getContext()))
            .show();
    ```

9. **自定义动画**

    自定义动画已经被设计得非常简单，动画和弹窗是无关的；这意味着你可以将动画设置给内置弹窗或者自定义弹窗。继承`PopupAnimator`，实现3个方法：
    - 如何初始化动画
    - 动画如何开始
    - 动画如何结束

    比如：自定义一个旋转的动画：
    ```java
    class RotateAnimator extends PopupAnimator{
            @Override
            public void initAnimator() {
                targetView.setScaleX(0);
                targetView.setScaleY(0);
                targetView.setAlpha(0);
                targetView.setRotation(360);
            }
            @Override
            public void animateShow() {
                targetView.animate().rotation(0).scaleX(1).scaleY(1).alpha(1).setInterpolator(new FastOutSlowInInterpolator()).setDuration(animateDuration).start();
            }
            @Override
            public void animateDismiss() {
                targetView.animate().rotation(360).scaleX(0).scaleY(0).alpha(0).setInterpolator(new FastOutSlowInInterpolator()).setDuration(animateDuration).start();
            }
        }
    ```
    使用自定义动画：
    ```java
    XPopup.get(getContext())
            .asConfirm(...)
            .customAnimator(new RotateAnimator())
            .show();
    ```

10. **显示DrawerLayout类型弹窗**

    对于DrawerLayout类型的弹窗，我只能帮你做好弹窗效果和手势交互。里面的UI和逻辑是无法帮你完成的，所以需要自定义一个弹窗，继承`DrawerPopupView`。代码非常简单，如下：
    ```java
    public class CustomDrawerPopupView extends DrawerPopupView {
        public CustomDrawerPopupView(@NonNull Context context) {
            super(context);
        }
        @Override
        protected int getImplLayoutId() {
            return R.layout.custom_drawer_popup;
        }
        @Override
        protected void initPopupContent() {
            super.initPopupContent();
            findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "nothing!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    ```
    使用自定义的DrawerLayout弹窗：
    ```java
    XPopup.get(getActivity())
            .asCustom(
                    new CustomDrawerPopupView(getContext())
                    //.setDrawerPosition(PopupDrawerLayout.Position.Right)
                    // 添加状态栏Shadow，默认是false；如果你的Drawer背景是白色，会导致状态栏的白色文字看不清，
                    // 此时建议设置该标志为true；
                    //.hasStatusBarShadow(true)
            )
            .show();
    ```


11. **自定义局部阴影弹窗**

    这种效果从分类上看仍然是Attach类型，因为要依附于某个View，在其上方或者下方显示。常见于列表条件筛选弹窗，比如京东或者淘宝的商品列表筛选。同样我只能帮你把复杂的交互效果做了，弹窗里面的UI和逻辑需要你自己继承`PartShadowPopupView`来做，这当然非常简单。
    最简单的示例如下：
    ```java
    public class CustomPartShadowPopupView extends PartShadowPopupView {
        public CustomPartShadowPopupView(@NonNull Context context) {
            super(context);
        }
        @Override
        protected int getImplLayoutId() {
            return R.layout.custom_part_shadow_popup; // 编写你自己的布局
        }
        @Override
        protected void initPopupContent() {
            super.initPopupContent();
            // 实现一些UI的初始和逻辑处理
        }
    }
    ```
    显示的时候仍然需要指定atView显示，内部会智能判断应该如何展示以及使用最佳的动画器：
    ```java
    XPopup.get(getActivity())
        .asCustom(new CustomPartShadowPopupView(getContext()))
        .atView(ll_container)
        .show();
    ```

12. **其他**
- 设置主色调

    默认情况下，XPopup的主色为灰色，主色作用于Button文字，EditText边框和光标，Check文字的颜色上。因为XPopup是单例，所以主色调只需要设置一次即可，可以放在Application中设置。
  ```java
  XPopup.get(this).setPrimaryColor(getResources().getColor(R.color.colorPrimary));
  ```

- 常用设置
  ```java
  XPopup.get(this)
      .hasShadowBg(true) // 是否有半透明的背景，默认为true
      .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
      .dismissOnTouchOutside(true) // 点击外部是否关闭弹窗，默认为true
      .popupAnimation(PopupAnimation.ScaleAlphaFromCenter) // 设置内置的动画
      .customAnimator(null) // 设置自定义的动画器
      .setPopupCallback(new XPopupCallback() { //设置显示和隐藏的回调
          @Override
          public void onShow() {
              // 完全显示的时候执行
          }
          @Override
          public void onDismiss() {
              // 完全隐藏的时候执行
          }
      })
  ```


## 待办
- [ ] Bottom类型的弹出支持手势拖拽，就像知乎的评论弹窗那样