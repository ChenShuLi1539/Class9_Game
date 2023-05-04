package character;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import buff.Buff;
import game.Game;
import game.Team;
import utils.Utils;

public class Character {

    protected String name;

    protected int maxHp;

    protected int maxMp;

    protected int normalAtk;

    /**
     * 速度
     */
    protected int spd;

    /**
     * 速度储存量
     */
    protected double spdRemain = 0;

    /**
     * 物防
     */
    protected int def;

    /**
     * 法防
     */
    protected int mdf;

    protected int currentHp;

    protected int currentMp;

    protected List<List<Buff>> buffLists = new ArrayList<>();

    /**
     * 生命回复buff
     */
    protected List<Buff> hpRecoverBuffs = new ArrayList<>();

    /**
     * 魔法回复buff
     */
    protected List<Buff> mpRecoverBuffs = new ArrayList<>();

    protected List<Buff> normalAtkBuffs = new ArrayList<>();

    protected List<Buff> spdBuffs = new ArrayList<>();

    protected List<Buff> defBuffs = new ArrayList<>();

    /**
     * 加算护甲buff
     */
    protected List<Buff> plusDefBuffs = new ArrayList<>();

    protected List<Buff> mdfBuffs = new ArrayList<>();

    /**
     * 加算魔抗buff
     */
    protected List<Buff> plusMdfBuffs = new ArrayList<>();

    /**
     * 减少物伤buff
     */
    protected List<Buff> reduceAtkBuffs = new ArrayList<>();

    /**
     * 减少法伤buff
     */
    protected List<Buff> reduceMatBuffs = new ArrayList<>();

    /**
     * 物伤buff
     */
    protected List<Buff> atkBuffs = new ArrayList<>();

    /**
     * 法伤buff
     */
    protected List<Buff> matBuffs = new ArrayList<>();


    protected List<Buff> specialBuffs = new ArrayList<>();

    /**
     * 伤害结果乘算buff
     */
    protected List<Buff> damageBuffs = new ArrayList<>();

    /**
     * 封印buff
     */
    protected List<Buff> forbidBuffs = new ArrayList<>();

    /**
     * 封印几率加算buff
     */
    protected List<Buff> forbidSuccessPlusBuffs = new ArrayList<>();

    protected Team team;

    protected int location;

    protected int totalDamage = 0;

    protected int round = 0;

    Character(String name, int maxHp, int maxMp, int normalAtk, int spd, int def, int mdf, Team team, int location) {
        this.name = name;
        this.maxHp = maxHp;
        this.maxMp = maxMp;
        this.normalAtk = normalAtk;
        this.spd = spd;
        this.def = def;
        this.mdf = mdf;
        currentHp = maxHp;
        currentMp = maxMp;
        this.team = team;
        this.location = location;

        buffLists.add(hpRecoverBuffs);
        buffLists.add(mpRecoverBuffs);
        buffLists.add(normalAtkBuffs);
        buffLists.add(spdBuffs);
        buffLists.add(defBuffs);
        buffLists.add(mdfBuffs);
        buffLists.add(plusDefBuffs);
        buffLists.add(plusMdfBuffs);
        buffLists.add(atkBuffs);
        buffLists.add(matBuffs);
        buffLists.add(reduceAtkBuffs);
        buffLists.add(reduceMatBuffs);
        buffLists.add(specialBuffs);
        buffLists.add(damageBuffs);
        buffLists.add(forbidBuffs);
        buffLists.add(forbidSuccessPlusBuffs);

    }

    public void motion() {
        // 倒地时跳过
        if (currentHp <= 0) {
            return;
        }

        round += 1;

        // 回蓝
        currentMp += 30 * Utils.calculateBuff(mpRecoverBuffs);
        if (currentMp > maxMp) {
            currentMp = maxMp;
        }
        Utils.print("");
        Utils.print(getName() + "开始ta的回合，当前是ta的第" + round + "回合，" + printState());
        // 速度储存量清0
        spdRemain = 0;
        // 减少buff时长
        for (List<Buff> list : buffLists) {
            Iterator<Buff> iterator = list.iterator();
            while (iterator.hasNext()) {
                Buff buff = iterator.next();
                buff.setLastTime(buff.getLastTime() - 1);
                Utils.print("————" + buff.getName() + "剩余" + buff.getLastTime() + "回合");
                if (buff.getLastTime() <= 0 && !buff.isAlways()) {
                    iterator.remove();
                }
            }
        }
        // 许悦加钱
        for (Character character : team.getStillAlive()) {
            if (character instanceof Character_XuYue) {
                ((Character_XuYue) character).coin++;
                Utils.print(character.getName() + "增加了1点钱币");
            }
        }
        if (Utils.hasBuff(this, "紧张感")) {
            Utils.print(getName() + "因为太紧张而跳过了ta的回合");
            return;
        }
        characterMotion();
    }

    public void prepareSkill() {

    }

    public void characterMotion() {

    }

    public void normalAtk() {
        if (Utils.hasBuff(this, "失心")) {
            Utils.print(getName() + "因为失心无法普通攻击");
            return;
        }
        if (Utils.hasBuff(this, "耀眼锋芒")) {
            Utils.print(getName() + "因为耀眼锋芒无法普通攻击");
            return;
        }
        Utils.normalAtkCalculate(this, Utils.getAnotherTeam(this).getRandomOneStillAliveByNormalAttack(this));
    }

    public void beforeAttack(Character main, Character passive) {

    }

    public String printState() {
        return "HP:" + currentHp + "/" + maxHp + " MP:" + currentMp + "/" + maxMp;
    }

    public void afterAttack(Character main, Character passive, int fromWhere, int damage) {
        if (main.equals(this)) {
            totalDamage += damage;
        }
        if (currentHp <= 0) {
            currentHp = 0;
            Utils.print(getName() + "倒地了！！！");
            for (Character c : Utils.getAllStillAlive()) {
                if (c.currentHp > 0 && Utils.hasBuff(c, "学佬之力")) {
                    int value = (int) (100 * Utils.calculateBuff(c.getMpRecoverBuffs()));
                    if (c.getCurrentMp() + value > c.getMaxMp()) {
                        value = c.getMaxMp() - c.getCurrentMp();
                    }
                    c.currentMp += value;
                    Utils.print(c.getName() + "因为学佬之力回复了" + value + "点MP");
                }
            }
            return;
        }
    }

    public String getName() {
        return team.getTeamName() + name + " ";
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public int getNormalAtk() {
        return normalAtk;
    }

    public void setNormalAtk(int normalAtk) {
        this.normalAtk = normalAtk;
    }

    public int getSpd() {
        return spd;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public double getSpdRemain() {
        return spdRemain;
    }

    public void setSpdRemain(double spdRemain) {
        this.spdRemain = spdRemain;
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

    public List<Buff> getPlusDefBuffs() {
        return plusDefBuffs;
    }

    public void setPlusDefBuffs(List<Buff> plusDefBuffs) {
        this.plusDefBuffs = plusDefBuffs;
    }

    public List<Buff> getPlusMdfBuffs() {
        return plusMdfBuffs;
    }

    public void setPlusMdfBuffs(List<Buff> plusMdfBuffs) {
        this.plusMdfBuffs = plusMdfBuffs;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getCurrentMp() {
        return currentMp;
    }

    public void setCurrentMp(int currentMp) {
        this.currentMp = currentMp;
    }

    public List<List<Buff>> getBuffLists() {
        return buffLists;
    }

    public void setBuffLists(List<List<Buff>> buffLists) {
        this.buffLists = buffLists;
    }

    public List<Buff> getHpRecoverBuffs() {
        return hpRecoverBuffs;
    }

    public void setHpRecoverBuffs(List<Buff> hpRecoverBuffs) {
        this.hpRecoverBuffs = hpRecoverBuffs;
    }

    public List<Buff> getMpRecoverBuffs() {
        return mpRecoverBuffs;
    }

    public void setMpRecoverBuffs(List<Buff> mpRecoverBuffs) {
        this.mpRecoverBuffs = mpRecoverBuffs;
    }

    public List<Buff> getNormalAtkBuffs() {
        return normalAtkBuffs;
    }

    public void setNormalAtkBuffs(List<Buff> normalAtkBuffs) {
        this.normalAtkBuffs = normalAtkBuffs;
    }

    public List<Buff> getSpdBuffs() {
        return spdBuffs;
    }

    public void setSpdBuffs(List<Buff> spdBuffs) {
        this.spdBuffs = spdBuffs;
    }

    public List<Buff> getDefBuffs() {
        return defBuffs;
    }

    public void setDefBuffs(List<Buff> defBuffs) {
        this.defBuffs = defBuffs;
    }

    public List<Buff> getMdfBuffs() {
        return mdfBuffs;
    }

    public void setMdfBuffs(List<Buff> mdfBuffs) {
        this.mdfBuffs = mdfBuffs;
    }

    public List<Buff> getReduceAtkBuffs() {
        return reduceAtkBuffs;
    }

    public void setReduceAtkBuffs(List<Buff> reduceAtkBuffs) {
        this.reduceAtkBuffs = reduceAtkBuffs;
    }

    public List<Buff> getReduceMatBuffs() {
        return reduceMatBuffs;
    }

    public void setReduceMatBuffs(List<Buff> reduceMatBuffs) {
        this.reduceMatBuffs = reduceMatBuffs;
    }

    public List<Buff> getAtkBuffs() {
        return atkBuffs;
    }

    public void setAtkBuffs(List<Buff> atkBuffs) {
        this.atkBuffs = atkBuffs;
    }

    public List<Buff> getMatBuffs() {
        return matBuffs;
    }

    public void setMatBuffs(List<Buff> matBuffs) {
        this.matBuffs = matBuffs;
    }

    public List<Buff> getSpecialBuffs() {
        return specialBuffs;
    }

    public void setSpecialBuffs(List<Buff> specialBuffs) {
        this.specialBuffs = specialBuffs;
    }

    public List<Buff> getDamageBuffs() {
        return damageBuffs;
    }

    public void setDamageBuffs(List<Buff> damageBuffs) {
        this.damageBuffs = damageBuffs;
    }

    public List<Buff> getForbidBuffs() {
        return forbidBuffs;
    }

    public void setForbidBuffs(List<Buff> forbidBuffs) {
        this.forbidBuffs = forbidBuffs;
    }

    public List<Buff> getForbidSuccessPlusBuffs() {
        return forbidSuccessPlusBuffs;
    }

    public void setForbidSuccessPlusBuffs(List<Buff> forbidSuccessPlusBuffs) {
        this.forbidSuccessPlusBuffs = forbidSuccessPlusBuffs;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(int totalDamage) {
        this.totalDamage = totalDamage;
    }

    public int getHPPercent() {
        return 100* getCurrentHp() / getMaxHp();
    }

    public int getLoseHPPercent() {
        return 100 - getHPPercent();
    }
}
