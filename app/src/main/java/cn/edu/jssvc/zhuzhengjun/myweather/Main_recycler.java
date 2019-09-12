package cn.edu.jssvc.zhuzhengjun.myweather;

public class Main_recycler {

    private String time;
    private String zhuangtai;
    private String wendu;

    public Main_recycler(String time, String zhuangtai, String wendu) {
        this.time = time;
        this.zhuangtai = zhuangtai;
        this.wendu = wendu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }
}
