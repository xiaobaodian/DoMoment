package DataStructures;

import android.view.View;

import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;

/**
 * Created by zhang on 2017/8/4.
 */

public class DemoDatas {

    private DateTimeHelper DateTime = DoMoment.getDateTime();
    public DemoDatas(){

    }

    public void BuildDatas(){

        Task t0 = new Task("清理猫砂", "市场",1);
        t0.setStartDate(2017,9,1);
        //t0.setStartDateTime(DateTime.SetDate(date,2017,9,2));
        DoMoment.getDataManger().AddTask(t0);

        Task t1 = new Task("制作鸟笼", "家里",2);
        t1.setStartDate(2017,9,5);
        DoMoment.getDataManger().AddTask(t1);

        Task t2 = new Task("购买小龙虾", "附近菜场",2);
        t2.setStartDate(2017,8,29);
        DoMoment.getDataManger().AddTask(t2);

        Task t3 = new Task("检查汽车的机油", "公司",1);
        t3.setStartDate(2017,9,1);
        DoMoment.getDataManger().AddTask(t3);

        Task t4 = new Task("看看安卓的开发技术书籍", "公司",1);
        t4.setStartDate(2017,9,7);
        DoMoment.getDataManger().AddTask(t4);

        Task t5 = new Task("研究快速消费品", "公司",2);
        t5.setStartDate(2018,3,2);
        DoMoment.getDataManger().AddTask(t5);

        Task t6 = new Task("找找Android数据库方面的书籍", "公司",1);
        t6.setNoDate();
        //t6.setStartDate(2018,3,2);
        DoMoment.getDataManger().AddTask(t6);

        DoMoment.getDataManger().getTodoFragment().setProgressBarVisibility(View.GONE);

        DoMoment.getDataManger().getCategoryList().BindDatas();
    }


}
