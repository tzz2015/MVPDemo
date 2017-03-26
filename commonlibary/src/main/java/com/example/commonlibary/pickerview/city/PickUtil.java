package com.example.commonlibary.pickerview.city;

import android.content.Context;
import android.util.Log;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公司：杭州融科网络科技
 * 刘宇飞 创建 on 2017/3/7.
 * 描述：pickerview工具类
 */

public class PickUtil {

    private static ArrayList<String> options1Items = new ArrayList<>();//一级
    private static ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//二级
    private static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//三级

    private static String mJsonObj;
    private static Gson gson;

    /**
     * 获取城市信息字符串
     *
     * @param context
     * @return
     */
    public static String getCityJson(Context context) {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = context.getAssets().open("json/address.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            mJsonObj = sb.toString();
            return mJsonObj;
        } catch (Exception e) {
            return null;
        }
    }

    public static void initPickView(Context context) {
        String cityJson = getCityJson(context);
        if (gson == null) {
            gson = new Gson();
        }
        if (cityJson == null) {
            return;
        }
        try {
            CityModle cityModle = gson.fromJson(cityJson, CityModle.class);
            if (cityModle != null && cityModle.getCitylist() != null) {
                List<CityModle.Province> citylist = cityModle.getCitylist();
                options1Items.clear();
                options2Items.clear();
                options3Items.clear();
                for (int i = 0; i < citylist.size(); i++) {
                    options1Items.add(citylist.get(i).getP());//省

                    List<CityModle.Province.City> cEntities = citylist.get(i).getC();//市

                    ArrayList<String> options2Items_item = new ArrayList<>();
                    ArrayList<ArrayList<String>> options3Items_item = new ArrayList<>();

                    for (int j = 0; j < cEntities.size(); j++) {//市
                        options2Items_item.add(cEntities.get(j).getN());


                        List<CityModle.Province.City.Area> qEntities = cEntities.get(j).getA(); //区

                        ArrayList<String> options3Items_item_item = new ArrayList<>();

                            for (CityModle.Province.City.Area entity : qEntities) {//区
                                options3Items_item_item.add(entity.getS());
                            }




                        options3Items_item.add(options3Items_item_item);

                    }
                    options2Items.add(options2Items_item);
                    options3Items.add(options3Items_item);

                }
            }
        } catch (Exception e) {
            Log.e("e:", "" + e.getMessage());
        }

    }

    /**
     *
     * @param
     * @param context
     */
    public static void showCityPickView( Context context,final ChooseCityListener listener) {
        OptionsPickerView pvOptions = new OptionsPickerView(context);
        if (options1Items.isEmpty() || options2Items.isEmpty() || options3Items.isEmpty()) {
            return;
        }
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
//                pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
               String p= options1Items.get(options1);
                String c=options2Items.get(options1).get(option2);
                String a=options3Items.get(options1).get(option2).get(options3);
                if(p.equals(c)){
                    listener.chooseCity(c+"_"+a);
                }else {
                    listener.chooseCity(p+"_"+c+"_"+a);
                }

            }
        });
        pvOptions.show();
    }


    public static  void showTime(Context context, final SelectTimeLisener lisener){
        TimePickerView pvTime= new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);


        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setTitle("选择日期");

        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                lisener.timeBack(date);
            }
        });
        pvTime.show();
    }

    public interface SelectTimeLisener{
        void timeBack(Date date);
    }
}
