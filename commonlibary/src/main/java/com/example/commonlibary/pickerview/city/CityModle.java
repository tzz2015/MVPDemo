package com.example.commonlibary.pickerview.city;

import java.util.List;

/**
 * 公司：杭州融科网络科技
 * 刘宇飞 创建 on 2017/3/7.
 * 描述：
 */

public class CityModle {


    private List<Province> citylist;

    public void setCitylist(List<Province> citylist) {
        this.citylist = citylist;
    }

    public List<Province> getCitylist() {
        return citylist;
    }

    public static class Province {

        private String p;
        private List<City> c;

        public void setP(String p) {
            this.p = p;
        }

        public void setC(List<City> c) {
            this.c = c;
        }

        public String getP() {
            return p;
        }

        public List<City> getC() {
            return c;
        }

        public static class City {
            /**
             * n : 石家庄
             * a : [{"s":"长安区"},{"s":"桥东区"},{"s":"桥西区"},{"s":"新华区"},{"s":"井陉矿区"},{"s":"裕华区"},{"s":"井陉县"},{"s":"正定县"},{"s":"栾城县"},{"s":"行唐县"},{"s":"灵寿县"},{"s":"高邑县"},{"s":"深泽县"},{"s":"赞皇县"},{"s":"无极县"},{"s":"平山县"},{"s":"元氏县"},{"s":"赵县"},{"s":"辛集市"},{"s":"藁城市"},{"s":"晋州市"},{"s":"新乐市"},{"s":"鹿泉市"}]
             */

            private String n;
            private List< Area> a;

            public void setN(String n) {
                this.n = n;
            }

            public void setA(List<Area> a) {
                this.a = a;
            }

            public String getN() {
                return n;
            }

            public List<Area> getA() {
                return a;
            }

            public static class Area {
                /**
                 * s : 长安区
                 */
                private String n;

                public void setN(String n) {
                    this.n = n;
                }

                public String getN() {
                    return n;
                }
                private String s;

                public void setS(String s) {
                    this.s = s;
                }

                public String getS() {
                    if(s==null){
                        s=n;
                    }
                    return s;
                }
            }
        }
    }
}
