package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Character_ZhouJinHao extends Character{

    /**
     * 睡梦被动的标志位
     */
    private boolean isTrue = true;

    public Character_ZhouJinHao(Team team, int location) {
        super("周锦浩", 900, 350, 50, 250, 30, 40, team, location);
    }

    @Override
    public void characterMotion() {
        if (Utils.random(50)) {
            isTrue = !isTrue;
            if (isTrue) {
                Utils.print(getName() + "现在是清醒的");
            } else {
                Utils.print(getName() + "现在活在梦里");
            }
        }
        skillOne();
        skillTwo();
        if (isTrue && spd * Utils.calculateBuff(spdBuffs) > 300 && Utils.enoughMp(this, 100)) {
            skillThree();
        } else if (!isTrue && Utils.enoughMp(this, 100)) {
            skillThree();
            normalAtk();
        } else {
            normalAtk();
        }
    }

    /**
     * 活在梦里（被动）
     * 现实：清除减速buff 梦游：清除减少物伤buff
     */
    private void skillOne() {
        if (isTrue) {
            Iterator<Buff> iterator = spdBuffs.iterator();
            while (iterator.hasNext()) {
                Buff buff = iterator.next();
                if (buff.getValue() < 0) {
                    iterator.remove();
                    Utils.print(getName() + "清除了" + buff.getName() + "的负面效果");
                }
            }
        } else {
            Iterator<Buff> iterator = atkBuffs.iterator();
            while (iterator.hasNext()) {
                Buff buff = iterator.next();
                if (buff.getValue() < 0) {
                    iterator.remove();
                    Utils.print(getName() + "清除了" + buff.getName() + "的负面效果");
                }
            }
        }
    }

    /**
     * 迷之护甲（被动）
     * 现实：减少受到的20%物理伤害 梦游：减少受到的20%魔法伤害
     */
    private void skillTwo() {
        if (isTrue) {
            Utils.addBuff(reduceAtkBuffs, new Buff("迷之护甲", 0.8, false, 1));
        } else {
            Utils.addBuff(reduceMatBuffs, new Buff("迷之护甲", 0.8, false, 1));
        }
    }

    /**
     * 老人滑步（主动） 100MP
     * 现实：对目标造成50*（你的速度/100）的物理伤害 梦游：你的速度永久+20，魔抗永久+10
     */
    private void skillThree() {
        if (isTrue) {
            Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
            Utils.print(getName() + "对" + character.getName() + "使用了老人滑步");
            Utils.attackCalculate(this, character, (int) (50 * spd * Utils.calculateBuff(spdBuffs) / 100));
        } else {
            spd += 20;
            mdf += 10;
            Utils.print(getName() + "使用老人滑步增加了20点速度和10点魔抗，当前速度：" + spd + "，当前魔抗：" + mdf);
        }
    }
}
