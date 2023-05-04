package character;

import buff.Buff;
import game.Team;
import utils.Utils;

public class Character_LiMingKun extends Character{

    /**
     * 疯狂被动积累的数值
     */
    protected int crazyNum = 0;

    public Character_LiMingKun(Team team, int location) {
        super("李明坤", 950, 350, 70, 250, 30, 40, team, location);
    }

    @Override
    public void characterMotion() {
        skillOne();
        if (Utils.random(25) && Utils.enoughMp(this, 100)) {
            skillThree();
        } else {
            normalAtk();
        }
    }

    @Override
    public void afterAttack(Character main, Character passive, int fromWhere, int damage) {
        super.afterAttack(main, passive, fromWhere, damage);
        if (this.equals(main)) {
            if (fromWhere == Utils.FROM_NORMAL_ATTACK) {
                skillTwo(damage);
                if (Utils.hasBuff(this, "疯狂")) {
                    Utils.cureCalculate(this, this, (int) (0.5 * damage));
                }
            }
        }
    }

    /**
     * 疯狂（被动）
     * 一局游戏中，如果你累计增加了300点护甲，则每次普通攻击后为你回复50%（造成的伤害）的HP
     */
    private void skillOne() {
        if (crazyNum >= 300 && Utils.hasBuff(this, "疯狂")) {
            Utils.addBuff(specialBuffs, new Buff("疯狂", 0, true, 999));
            Utils.print(getName() + "陷入了疯狂，现在ta每次攻击都会回复HP");
        }
    }

    /**
     * 无敌傻子王（被动）
     * 在你普通攻击后，为你增加30%（本回合造成的普通伤害）的护甲，持续一回合
     */
    private void skillTwo(int damage) {
        Utils.addBuff(plusDefBuffs, new Buff("无敌傻子王", 0.3 * damage, false, 1));
        crazyNum += 0.3 * damage;
        Utils.print(getName() + "增加了" + (int) (0.3 * damage) + "点护甲，持续一回合，目前已累计增加" + crazyNum + "点护甲");
    }

    /**
     * 打爆你眼镜（主动） 100MP
     * 造成3*（100 - 对方护甲）的物理伤害（最少为30），如果目标被减速，则再造成20%（对方当前HP）的穿透伤害
     */
    private void skillThree() {
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        Utils.print(getName() + "对" + character.getName() + "使用了打爆你眼镜");
        int coefficient = (int) (100 -
                Utils.calculateDefByPlus(character) * Utils.calculateBuff(character.getDefBuffs()));
        if (coefficient <= 1) {
            coefficient = 1;
        }
        Utils.attackCalculate(this, character, 3 * coefficient);
        if (Utils.calculateBuff(character.spdBuffs) < 1.0) {
            Utils.print(getName() + "的打爆你眼镜因为对方被减速追加了穿透伤害");
            Utils.trueAttackCalculate(this, character, (int) (0.2 * character.currentHp));
        }
    }
}
