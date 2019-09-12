package cn.edu.jssvc.zhuzhengjun.myweather;

public class Main_list {

    private String xingqi,zhuangtai,max,min;
    private int iamge;

    public Main_list(String xingqi, int iamge, String zhuangtai ,String max, String min) {
        this.xingqi = xingqi;
        this.iamge = iamge;
        this.zhuangtai = zhuangtai;
        this.max = max;
        this.min = min;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getXingqi() {
        return xingqi;
    }

    public void setXingqi(String xingqi) {
        this.xingqi = xingqi;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public int getIamge() {
        return iamge;
    }

    public void setIamge(int iamge) {
        this.iamge = iamge;
    }
}
