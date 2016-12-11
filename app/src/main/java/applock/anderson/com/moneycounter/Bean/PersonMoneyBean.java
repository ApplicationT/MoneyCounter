package applock.anderson.com.moneycounter.Bean;


import com.orhanobut.logger.Logger;

/**
 * Created by Xiamin on 2016/12/11.
 */

public class PersonMoneyBean {
    public String name;              //每个红包的用户名称
    public String moneyString;       //红包金额的字符串
    public float moneyFloat;         //红包金额字符串转换结果
    public String moneyChange;       //以后修改红包金额

    public void setMoneyString(String moneyString) {
        this.moneyString = moneyString;
        parseMoneyFloat();
        Logger.i(this.toString());
    }

    private void parseMoneyFloat() {
        if (moneyString == null) {
            return;
        }
        String b = moneyString.substring(0, moneyString.indexOf("元"));
        System.out.println(b);
        try {
            this.moneyFloat = Float.parseFloat(b);
        } catch (Exception e) {
            Logger.e("浮点数转换出错 " + b + "  " + e.toString());
        }

    }

    @Override
    public String toString() {
        return "姓名 " + name + " 红包字符串 " + moneyString + " 金额为 " + moneyFloat + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoneyChange() {
        return moneyChange;
    }

    public void setMoneyChange(String moneyChange) {
        this.moneyChange = moneyChange;
    }

    public float getMoneyFloat() {
        return moneyFloat;
    }

    public void setMoneyFloat(float moneyFloat) {
        this.moneyFloat = moneyFloat;
    }

    public String getMoneyString() {
        return moneyString;
    }


}
