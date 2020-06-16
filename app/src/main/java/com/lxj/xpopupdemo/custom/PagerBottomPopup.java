package com.lxj.xpopupdemo.custom;

import android.content.Context;;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopupdemo.R;


/**
 * Description: 自定义带有ViewPager的Bottom弹窗
 * Create by dance, at 2019/5/5
 */
public class PagerBottomPopup extends BottomPopupView {
    public PagerBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_view_pager;
    }

    ViewPager pager;

    @Override
    protected void onCreate() {
        super.onCreate();
        pager = findViewById(R.id.pager);
        pager.setAdapter(new PAdapter());
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    class PAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            NestedScrollView scrollView = new NestedScrollView(container.getContext());
            LinearLayout linearLayout = new LinearLayout(container.getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            scrollView.addView(linearLayout);
            TextView textView = new TextView(container.getContext());
            textView.setPadding(40,40,40,40);
            textView.setText("2008年，文章在热播剧《奋斗》中出演男二号向南，引起广泛关注。这也是文章和妻子马伊琍的第二次合作。2009年，文章进入电影届，主演第一部电影《走着瞧》。该片在上海国际电影节新片展映单元和东京国际电影节“亚洲风”单元获奖。文章也凭主演的北京青年马杰一角获得第12届上海国际电影节最受关注新人演员奖。2009年，文章主演电视剧《爱在日月潭》，参演热播电视剧《蜗居》，成功饰演配角小贝，知名度得到进一步提高。2010年，文章与李连杰共同主演文艺片《海洋天堂》，文章饰演青年自闭症患者大福，被认为是中国的“达斯汀·霍夫曼”。文章凭此片获得第14届中国电影华表奖优秀新人男演员奖，第13届上海国际电影节最佳男主角奖和第18届北京大学生电影节最受大学生欢迎男演员奖。该片获得上海国际电影节“金爵奖”，中国电影华表奖优秀故事片奖和第18届北京大学生电影节人文关怀奖。同年，文章主演电视剧《雪豹》，该剧被各大电视台反复播放。文章凭借周卫国一角获得第12届四川电视艺术节金熊猫奖电视剧类最佳男演员，第9届中国金鹰电视艺术节最具人气男演员和第26届中国电视金鹰奖最受观众喜爱男演员。2011年，文章在滕华弢导演、改编自网络小说的电视剧《裸婚时代》中出演男主角刘易阳，被网友誉为灯笼男。文章在《裸婚时代》中更身兼剧本策划，片中许多经典台词被年轻人推崇。文章凭此片获得2011国剧盛典年度最佳男演员。由文章与白百何主演，滕华弢导演，改编自网络小说的电影《失恋33天》在各大影院上映，创造了小成本电影的票房。文章凭王小贱一角获得第31届大众电影百花奖最佳男演员奖。他是第一个获此殊荣的80后男演员。在电影《万有引力》中，文章饰演其中一个单元的男主角高洋，该片入围加拿大蒙特利尔国际电影节最佳影片。文章还参演了电影《白蛇传说》，饰演了一个非常抢眼的配角能忍。2012年，文章主演贺岁片《亲家过年》饰演男主角张雪伦。2013年，由文章主演的周星驰电影《西游·降魔篇》打破华语片票房记录，收获全球票房2.15亿美元。同年文章自己经营的影视工作室-君竹（北京）影视文化有限公司参与投资制作的第一部影视作品《小爸爸》在大陆四大卫视黄金档播出。文章在该剧中不仅饰演男主角于果，还担任导演并参与剧本策划。这是文章第一次出任导演。文章凭《小爸爸》获2013国剧盛典 观众喜爱的导演，该片同时获得2013国剧盛典十佳电视剧奖。妻子马伊琍在片中饰演女主角，并担任制片人。2014年，文章参演姜文导演的电影《一步之遥》，饰演主角之一武七。该片入围第65届柏林电影节。2016年，文章主演张黎导演的电视剧《少帅》饰演历史人物张学良。文章凭此片获得2017中国电视剧品质盛典年度品质表演剧星。同年七月，由文章的君竹（上海）影视文化有限公司参与制作的喜剧爱情电影《陆垚知马俐》在中国大陆，美国，加拿大，澳大利亚及新西兰上映。文章不仅担任导演，还兼任编剧。此片以中小成本获得近2亿票房。文章的导演风格被一些影评人誉为“文房四宝”-- “斗嘴皮、飙演技、抖机灵、戳心窝”。文章凭此处女作提名第十九届上海国际电影节之亚洲新人奖最佳导演奖。同年八月《陆垚知马俐》获得第40届加拿大蒙特利尔国际电影节中国电影银奖。2017年，《陆垚知马俐》获得第31届中国电影金鸡奖七项提名 - 最佳导演处女作，最佳男主角，最佳女主角，最佳男配角，最佳女配角，最佳录音和最佳摄影。最后文章荣获最佳导演处女作奖。2017年，文章的君竹（上海）影视文化有限公司参与制作谍战剧《剃刀边缘》，文章担任导演并饰演男主角许从良。妻子马伊俐出演女主角。此片打破以往谍战剧的套路，以小人物的成长为主线，加入爱情，探案等元素。电视剧播出后，口碑收视均取得良好的成绩。同年三月，文章重返舞台，出演独角话剧《每一件美妙的小事》。文章凭此剧获得2017上海静安现代戏剧谷壹戏剧大赏年度最佳男演员奖。2017年11月12日，由文章编剧并导演，阿里巴巴创始人马云主演的微电影《功守道》在优酷独家播出。这部只有22分钟的短片汇集了当今中国顶尖功夫明星和武术指导，包括李连杰，甄子丹，吴京，袁和平，洪金宝以及程小东。电影播出不到两天，网络点击率突破1.5亿，评论量1.5万，顶8.1万。2017年3月已经签约上海话剧艺术中心的文章出演独角话剧《每一件美妙的小事》，文章一人演满90分钟，以细腻而真挚的表演，将母与子的情感对峙、亲情中爱与恨的挣扎，展现得淋漓尽致。这是一部成长的独角戏，文章以收放自如的表演，体现了忧郁的诗意，荣获2017年上海静安现代戏剧谷“壹戏剧大赏”年度最佳男演员。2018年10月18日到11月3日，文章领衔主演中德合作话剧《茶馆》。这部话剧由著名先锋导演孟京辉执导，德国知名戏剧构作塞巴斯蒂安•凯撤担任戏剧构作。这版《茶馆》在乌镇戏剧节成为开幕话剧，开票仅三分钟，4400张票即告售罄。文章在剧中有大段爆发式独白，他以多年的表演功底和艺术能量的积累，用自由真实的表达征服在场观众。这是文章和舞台的亲密接触，更是他对表演和经典的致敬。");
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(R.mipmap.ic_launcher);
            linearLayout.addView(imageView);
            container.addView(scrollView);
            return scrollView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }
    }
}
