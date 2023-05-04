package character;

import buff.Buff;
import game.Team;
import utils.Utils;

public class Character_HeZhengLei extends Character{

    private boolean skillTwoFlag = false;

    public Character_HeZhengLei(Team team, int location) {
        super("何峥磊", 800, 300, 60, 300, 40, 20, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
    }

    @Override
    public void characterMotion() {
        if ((double) currentHp / maxHp >= 0.5) {
            currentHp *= 0.94;
            skillTwo();
        } else if (Utils.enoughMp(this, 100)) {
            skillThree();
        } else {
            normalAtk();
        }
    }

    @Override
    public void afterAttack(Character main, Character passive, int fromWhere, int damage) {
        super.afterAttack(main, passive, fromWhere, damage);
        if (skillTwoFlag) {
            Utils.print(getName() + "受到了来自破釜沉舟反弹的" + (int) (0.25 * damage) + "点伤害");
            currentHp -= 0.25 * damage;
            if (currentHp < 0) {
                currentHp = 0;
                Utils.print(getName() + "倒地了！！！");
                for (Character c : Utils.getAllStillAlive()) {
                    if (c.currentHp > 0 && Utils.hasBuff(c, "学佬之力")) {
                        c.currentMp += 100;
                        Utils.print(c.getName() + "因为学佬之力回复了100点MP");
                    }
                }
            }
        }
    }

    /**
     * 急袭（被动）
     * 你的普通攻击可以攻击任意一个目标
     */
    private void skillOne() {
        Utils.addBuff(specialBuffs, new Buff("急袭", 0, true, 999));
    }

    /**
     * 破釜沉舟（主动） 6%当前HP 大于50%最大HP时才能释放
     * 对同一个目标连续普通攻击三次，反弹你造成的25%的伤害
     */
    private void skillTwo() {
        skillTwoFlag = true;
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        Utils.print(getName() + "对" + character.getName() + "使用了破釜沉舟，当前是第一段攻击");
        Utils.normalAtkCalculate(this, character);
        if (currentHp <= 0) {
            skillTwoFlag = false;
            return;
        }
        Utils.print(getName() + "的破釜沉舟第二段攻击");
        Utils.normalAtkCalculate(this, character);
        if (currentHp <= 0) {
            skillTwoFlag = false;
            return;
        }
        Utils.print(getName() + "的破釜沉舟第三段攻击");
        Utils.normalAtkCalculate(this, character);
        skillTwoFlag = false;
    }

    /**
     * 绝命一击（主动） 100MP
     * 对目标造成1.2*（你的普通攻击伤害）的物理伤害，若击倒对方，则目标三回合内受到的治疗效果降低35%
     */
    private void skillThree() {
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        Utils.print(getName() + "对" + character.getName() + "使用了绝命一击");
        Utils.attackCalculate(this, character, (int) (2.4 * normalAtk * Utils.calculateBuff(normalAtkBuffs)));
        if (character.getCurrentHp() <= 0) {
            Utils.print("由于绝命一击的效果，" + character.getName() + "在三回合内受到的治疗效果降低了");
            Utils.addBuff(character.getHpRecoverBuffs(), new Buff("绝命一击", 0.65, false, 4));
        }
    }
}
