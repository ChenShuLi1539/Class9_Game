package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Character_WeiZongYan extends Character{

    public Character_WeiZongYan(Team team, int location) {
        super("韦宗言", 800, 300, 50, 260, 40, 40, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
        skillTwo();
    }

    @Override
    public void characterMotion() {
        super.characterMotion();
        if (getCurrentMp() > 180 && Utils.random(80) && Utils.enoughMp(this, 60)) {
            skillThree();
        }
        if (getHPPercent() < 60 && Utils.enoughMp(this, 120)) {
            skillFour();
        } else {
            normalAtk();
        }
    }

    /**
     * 挺身而出（被动）
     * 我方受到普通攻击时，有35%的几率由你承担此次攻击
     */
    private void skillOne() {
        Utils.addBuff(getSpecialBuffs(), new Buff("挺身而出", 0, true, 999));
    }

    /**
     * 百折不挠（被动）
     * 你每次被救起时你的护甲、魔抗和普通攻击力永久+10
     */
    private void skillTwo() {
        Utils.addBuff(getSpecialBuffs(), new Buff("百折不挠", 0, true, 999));
    }

    /**
     * 舍身（主动） 60MP
     * 减少你20点护甲，提高你本回合20%的伤害至你下个回合开始
     */
    private void skillThree() {
        Utils.addBuff(getDamageBuffs(), new Buff("舍身", 1.2, false, 1));
        Utils.addBuff(getPlusDefBuffs(), new Buff("舍身", -20, false, 1));
        Utils.print(getName() + "使用了舍身，临时提高了本回合的伤害");
    }

    /**
     * 千帆竞发（主动） 120MP
     * 对对方随机4个目标造成1.4*（你的普通攻击力）的物理伤害
     */
    private void skillFour() {
        List<Character> team = Utils.getAnotherTeam(this).getStillAlive();
        if (team.size() <= 4) {
            for (Character character : team) {
                Utils.print(getName() + "对" + character.getName() + "使用了千帆竞发");
                Utils.attackCalculate(this, character,
                        (int) (1.4 * getNormalAtk() * Utils.calculateBuff(normalAtkBuffs)));
            }
        } else {
            List<Integer> randomList = new ArrayList<>();
            while (randomList.size() < 4) {
                int random = (int) (Math.random() * team.size());
                if (!randomList.contains(random)) {
                    randomList.add(random);
                }
            }
            for (Integer i : randomList) {
                Character character = team.get(i);
                Utils.print(getName() + "对" + character.getName() + "使用了千帆竞发");
                Utils.attackCalculate(this, character,
                        (int) (1.4 * getNormalAtk() * Utils.calculateBuff(normalAtkBuffs)));
            }
        }
    }
}
