## XPopup
![](https://api.bintray.com/packages/li-xiaojun/jrepo/xpopup/images/download.svg)  ![](https://img.shields.io/badge/platform-android-blue.svg)  ![](https://img.shields.io/badge/author-li--xiaojun-brightgreen.svg) ![](https://img.shields.io/badge/compileSdkVersion-26-blue.svg) ![](https://img.shields.io/badge/minSdkVersion-15-blue.svg) ![](https://img.shields.io/hexpm/l/plug.svg)
![](screenshot/logo.png)

### 中文 | [English](https://github.com/li-xiaojun/XPopup/blob/master/README-en.md)

功能强大，UI简洁，交互优雅的通用弹窗！可以替代Dialog，PopupWindow，PopupMenu，BottomSheet，DrawerLayout，Spinner等组件，自带十几种效果良好的动画，
支持完全的UI和动画自定义！它有这样几个特点：
1. 功能强大，内部封装了常用的弹窗，内置十几种良好的动画，将弹窗和动画的自定义设计的极其简单
2. UI和动画简洁，遵循Material Design，在设计动画的时候考虑了很多细节，过渡，层级的变化；或者说是模拟系统组件的动画，具体可以从Demo中感受
3. 交互优雅，实现了优雅的手势交互以及智能的嵌套滚动，具体看Demo
4. 适配全面屏，目前适配了小米，华为，谷歌，OPPO，VIVO，三星，魅族，一加全系全面屏手机
5. 通用性，项目需求复杂多变，产品经理天马行空，虽然很难做到UI的通用，但是你可以看到交互和动画完全可以通用；至于弹窗的UI和逻辑可能需要你自定义
6. 易用性，所有的自定义弹窗只需继承对应的类，实现你的布局，然后在`onCreate`方法写逻辑即可


**编写本库的初衷有以下几点**：
1. 项目有这样常见需求：中间和底部弹出甚至可拖拽的对话框，指定位置的PopupMenu或者PopupWindow，指定区域阴影的弹出层效果
2. 市面上已有的类库要么功能不足够，要么交互效果不完美，有着普遍的缺点，就像BottomSheet存在的问题一样。比如：窗体消失的动画和背景渐变动画不一致，窗体消失后半透明背景仍然停留一会儿


**设计思路**：
综合常见的弹窗场景，我将其分为几类：
1. Center类型，就是在中间弹出的弹窗，比如确认和取消弹窗，Loading弹窗
2. Bottom类型，就是从页面底部弹出，比如从底部弹出的分享窗体，知乎的从底部弹出的评论列表，我内部会处理好手势拖拽和嵌套滚动
3. Attach类型，就是弹窗的位置需要依附于某个View或者某个触摸点，就像系统的PopupMenu效果一样，但PopupMenu的自定义性很差，淘宝的商品列表筛选的下拉弹窗也属于这种，微信的朋友圈点赞弹窗也是这种。
4. DrawerLayout类型，就是从窗体的坐边或者右边弹出，并支持手势拖拽；好处是与界面解耦，可以在任何界面显示DrawerLayout
5. 大图浏览类型，就像掘金那样的图片浏览弹窗，带有良好的拖拽交互体验
6. 全屏弹窗，弹窗是全屏的，就像Activity那样，可以设置任意的动画器；适合用来实现登录，选择性的界面效果。


## ScreenShot

![](screenshot/preview.gif) ![](screenshot/preview_bottom.gif)

![](screenshot/preview_attach.gif) ![](screenshot/preview_drawer.gif)

![](screenshot/bottom_edit.gif) ![](screenshot/fullscreen.gif) 

![](screenshot/horizontal_attach.gif) ![](screenshot/preview_part.gif) 

![](screenshot/image_viewer1.gif) ![](screenshot/image_viewer2.gif) 

![](screenshot/image_viewer3.gif) ![](screenshot/preview2.gif) 

![](screenshot/preview3.gif) ![](screenshot/preview4.gif)

![](screenshot/comment_edit.gif)

## 快速体验
扫描二维码下载Demo：
![](screenshot/download.png)

如果二维码图片不可见，[点我下载Demo体验](https://fir.im/2q63)

## Gradle
首先需要添加Gradle依赖：
```groovy
implementation 'com.lxj:xpopup:1.7.3'
//for androidx.
implementation 'com.lxj:xpopup:1.7.3-x'
```

必须添加的依赖库：
```groovy
//版本号在26以及以上即可
implementation 'com.android.support:appcompat-v7:28.0.0'
implementation 'com.android.support:recyclerview-v7:28.0.0'
implementation 'com.android.support:design:28.0.0'
```

## WIKI
具体使用方法全在WIKI中，请查看下面各个章节：
- [介绍](https://github.com/li-xiaojun/XPopup/wiki/1.-%E4%BB%8B%E7%BB%8D)
- [如何使用内置的弹窗](https://github.com/li-xiaojun/XPopup/wiki/2.-%E5%86%85%E7%BD%AE%E7%9A%84%E5%BC%B9%E7%AA%97%E5%AE%9E%E7%8E%B0)
- [如何自定义弹窗](https://github.com/li-xiaojun/XPopup/wiki/3.-%E8%87%AA%E5%AE%9A%E4%B9%89%E5%BC%B9%E7%AA%97)
- [如何自定义动画](https://github.com/li-xiaojun/XPopup/wiki/4.-%E8%87%AA%E5%AE%9A%E4%B9%89%E5%8A%A8%E7%94%BB)
- [常用设置](https://github.com/li-xiaojun/XPopup/wiki/5.-%E5%B8%B8%E7%94%A8%E8%AE%BE%E7%BD%AE)


## 混淆
```
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}
```


## 谁在用XPopup
我本人很希望您能[点击这里附上](https://github.com/li-xiaojun/XPopup/issues/93)使用这个库的App名或者公司名，这样会给我更大的动力和热情去维护这个类库。

根据热心朋友提供的信息，目前使用XPopup的产品和公司有：
- 海鸥地图（https://cn.gullmap.com/）
- 马自达汽车检测（主要是一个汽车厂商工作人员使用的汽车检测APP）
- 变福侠App
- 进境肉牛检疫追溯系统(App端)
- 太极 (app名, 下载地址：https://www.coolapk.com/apk/me.weishu.exp)


## 联系方式

![](screenshot/qqgroup.png)

Gmail: lxj16167479@gmail.com

QQ Email: 16167479@qq.com

QQ: 16167479

## Licenses
```
 Copyright 2019 li-xiaojun

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```