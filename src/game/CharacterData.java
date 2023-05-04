package game;

public class CharacterData {

    private String name;

    private String desc;

    private int Hp;

    private int Mp;

    private int normalAttack;

    private int spd;

    private int def;

    private int mdf;

    private String location;


    public CharacterData(String name, int hp, int mp, int normalAttack, int spd, int def, int mdf, String location, String desc) {
        this.name = name;
        this.desc = desc;
        Hp = hp;
        Mp = mp;
        this.normalAttack = normalAttack;
        this.spd = spd;
        this.def = def;
        this.mdf = mdf;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getHp() {
        return Hp;
    }

    public void setHp(int hp) {
        Hp = hp;
    }

    public int getMp() {
        return Mp;
    }

    public void setMp(int mp) {
        Mp = mp;
    }

    public int getNormalAttack() {
        return normalAttack;
    }

    public void setNormalAttack(int normalAttack) {
        this.normalAttack = normalAttack;
    }

    public int getSpd() {
        return spd;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getMdf() {
        return mdf;
    }

    public void setMdf(int mdf) {
        this.mdf = mdf;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
