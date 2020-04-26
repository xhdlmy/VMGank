package com.bruce.gank.net.retrofit.resp;

import java.util.List;

/**
 * Created by Bruce on 2019/7/11.
 */

public class GankClassifyData {

    /**
     * data : [{"_id":"5e51fb0c45daba871ab7b5fd","author":"李金山","category":"GanHuo","createdAt":"2019-01-03 06:04:45","desc":"Android自定义控件RulerView，身高、体重等标尺，尺码控件，滑动可修改刻度值。","images":["http://gank.io/images/589bba8973ff4726931f2ab5f4dd6116"],"likeCounts":0,"publishedAt":"2019-01-03 00:00:00","stars":4,"title":"RulerView","type":"Android","url":"https://github.com/hnsycsxhzcsh/RulerView","views":48},{"_id":"5e7225a1907078dcbfe954ac","author":"Pan_","category":"GanHuo","createdAt":"2018-12-29 06:33:23","desc":"EasyPermission是一个简单易用，且无多余的第三方依赖的Android6.0动态权限申请库，支持链式调用，方便快捷。","images":[],"likeCounts":0,"publishedAt":"2019-01-03 00:00:00","stars":1,"title":"EasyPermission","type":"Android","url":"https://github.com/panyiho/EasyPermission","views":18},{"_id":"5e51fb0e45daba871ab7b5ff","author":"aiden1698","category":"GanHuo","createdAt":"2018-12-28 06:53:48","desc":"适用于任何视图的可自定义反弹动画库。","images":["http://gank.io/images/3c433c34209b4d92a86fcecf1de53109"],"likeCounts":0,"publishedAt":"2018-12-28 00:00:00","stars":3,"title":"Bounceview-Android","type":"Android","url":"https://github.com/hariprasanths/Bounceview-Android","views":30},{"_id":"5e51fb1c45daba871ab7b602","author":"李金山","category":"GanHuo","createdAt":"2018-12-28 07:23:40","desc":"一个简单且可自定义的Android全屏图像查看器，支持共享图像转换，\u201c捏合缩放\u201d和\u201c轻扫以消失\u201d手势。","images":["http://gank.io/images/afe4b916fb0a40ec8ff83ef4e7c6e099","http://gank.io/images/df2717d7867b4907a4cbde20f829b450"],"likeCounts":0,"publishedAt":"2018-12-28 00:00:00","stars":5,"title":"StfalconImageViewer","type":"Android","url":"https://github.com/stfalcon-studio/StfalconImageViewer","views":42},{"_id":"5e51fb2145daba871ab7b605","author":"lijinshanmx","category":"GanHuo","createdAt":"2018-12-28 07:24:37","desc":"可视化的了解RxJava运算符。","images":["http://gank.io/images/f0f9406d2b914ef2837a9f889f9d0073","http://gank.io/images/b767f9aac4e743b0b3a418ba93a5f233"],"likeCounts":0,"publishedAt":"2018-12-28 00:00:00","stars":1,"title":"RxAnime","type":"Android","url":"https://github.com/amanjeetsingh150/RxAnime","views":15},{"_id":"5e51fb2545daba871ab7b607","author":"李金山","category":"GanHuo","createdAt":"2018-12-28 07:28:21","desc":"适用于Android的粒子动画库。","images":["http://gank.io/images/2a72ffd3a23e4d6b9075af5b0cd84fd5"],"likeCounts":0,"publishedAt":"2018-12-28 00:00:00","stars":3,"title":"android-particles","type":"Android","url":"https://github.com/ibrahimsn98/android-particles","views":54},{"_id":"5e51fb2845daba871ab7b609","author":"李金山","category":"GanHuo","createdAt":"2018-12-28 07:30:50","desc":"android seekbar 双向选择的进度条。","images":["http://gank.io/images/bc42dfec9f184754bc88d26f58bc1966"],"likeCounts":0,"publishedAt":"2018-12-28 00:00:00","stars":1,"title":"DoubleHeadedDragonBar","type":"Android","url":"https://github.com/yujinzhao123/DoubleHeadedDragonBar","views":58},{"_id":"5e7225a2907078dcbfe954ba","author":"lijinshanmx","category":"GanHuo","createdAt":"2018-12-28 07:34:36","desc":"封装bilibili播放器,自定义边下边播和缓存功能。","images":[],"likeCounts":0,"publishedAt":"2018-12-28 00:00:00","stars":1,"title":"VideoPlayerDemo","type":"Android","url":"https://github.com/Zhaoss/VideoPlayerDemo","views":18},{"_id":"5e51fb2a45daba871ab7b60b","author":"李金山","category":"GanHuo","createdAt":"2018-12-13 08:22:02","desc":"360开源的移动端可视化性能监控平台，为移动端APP提供性能监控与管理，可以迅速发现和定位各类APP性能和使用问题，帮助APP不断的提升用户体验。","images":["http://gank.io/images/af8146560363477ea770e8c14a6b163e"],"likeCounts":0,"publishedAt":"2018-12-13 00:00:00","stars":5,"title":"ArgusAPM","type":"Android","url":"https://github.com/Qihoo360/ArgusAPM","views":50},{"_id":"5e51fb3445daba871ab7b60f","author":"李金山","category":"GanHuo","createdAt":"2018-12-13 08:25:50","desc":"用于在Android中的ViewPager中创建书籍和卡片翻转动画的库.","images":["http://gank.io/images/d2cf95eb8e80455a94bb588dc88955fc","http://gank.io/images/3aca0d2bc2534b0caccc3286f39f860f","http://gank.io/images/6ff183fb5ca84722b8c3cfd23c52b5a0"],"likeCounts":0,"publishedAt":"2018-12-13 00:00:00","stars":3,"title":"EasyFlipViewPager","type":"Android","url":"https://github.com/wajahatkarim3/EasyFlipViewPager","views":76}]
     * page : 10
     * page_count : 244
     * status : 100
     * total_counts : 2432
     */

    public int page;
    public int page_count;
    public int total_counts;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * _id : 5e51fb0c45daba871ab7b5fd
         * author : 李金山
         * category : GanHuo
         * createdAt : 2019-01-03 06:04:45
         * desc : Android自定义控件RulerView，身高、体重等标尺，尺码控件，滑动可修改刻度值。
         * images : ["http://gank.io/images/589bba8973ff4726931f2ab5f4dd6116"]
         * likeCounts : 0
         * publishedAt : 2019-01-03 00:00:00
         * stars : 4
         * title : RulerView
         * type : Android
         * url : https://github.com/hnsycsxhzcsh/RulerView
         * views : 48
         */

        public String _id;
        public String author;
        public String category;
        public String createdAt;
        public String desc;
        public int likeCounts;
        public String publishedAt;
        public int stars;
        public String title;
        public String type;
        public String url;
        public int views;
        public List<String> images;

    }
}
