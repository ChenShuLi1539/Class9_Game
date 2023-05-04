package buff;

public class Buff {

    private String name;

    /**
     * 计算伤害时，对攻击力正为加成；对减伤正为加成
     */
    private double value;

    private boolean isAlways;

    private int lastTime = 0;

    public Buff(String name, double value, boolean isAlways, int lastTime) {
        this.name = name;
        this.value = value;
        this.isAlways = isAlways;
        this.lastTime = lastTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isAlways() {
        return isAlways;
    }

    public void setAlways(boolean always) {
        isAlways = always;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }
}
