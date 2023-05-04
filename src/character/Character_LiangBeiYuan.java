package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.List;

public class Character_LiangBeiYuan extends Character{
    public Character_LiangBeiYuan(Team team, int location) {
        super("梁倍源", 800, 450, 60, 260, 40, 60, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
    }

    @Override
    public void afterAttack(Character main, Character passive, int fromWhere, int damage) {
        super.afterAttack(main, passive, fromWhere, damage);
        if (main.equals(this)) {
            skillTwo(passive);
        }
    }

    @Override
    public void characterMotion() {
        if (Utils.random((int) (50 + getLoseHPPercent())) && Utils.enoughMp(this, 130)) {
            skillThree();
        } else {
            normalAtk();
        }
    }

    /**
     * 鼓舞（被动）
     * 我方造成的魔法伤害增加15%
     */
    private void skillOne() {
        for (Character character : team.getCharacterList()) {
            Utils.addBuff(character.getMatBuffs(), new Buff("鼓舞", 1.15, true, 999));
        }
    }

    /**
     * 心灵压迫（被动）
     * 你造成伤害时会降低目标15%的HP回复一回合
     */
    private void skillTwo(Character character) {
        Utils.print(getName() + "降低了" + character.getName() + "的HP回复效果");
        Utils.addBuff(character.getHpRecoverBuffs(), new Buff("心灵压迫", 0.85, false, 2));
    }

    /**
     * 嚣哑下的力量（主动） 130MP
     * 对目标造成60+200*（你已损失HP%）的魔法伤害，你每失去25%HP则会多作用于一个目标
     */
    private void skillThree() {
        List<Character> list = Utils.getAnotherTeam(this).getStillAlive();
        double loseHPPercent = ((double) getMaxHp() - getCurrentHp()) / getMaxHp();
        int targetNum = 0;
        do {
            targetNum++;
            loseHPPercent -= 0.25;
        } while (loseHPPercent >= 0);
        if (list.size() <= targetNum) {
            for (Character character : list) {
                Utils.print(getName() + "对" + character.getName() + "使用了嚣哑下的力量");
                Utils.magicAttackCalculate(this, character, 60 + 200 * (getMaxHp() - getCurrentHp()) / getMaxHp());
            }
        } else {
            for (int i = 0; i < targetNum; i++) {
                Character target = list.get((int) (Math.random() * list.size()));
                Utils.print(getName() + "对" + target.getName() + "使用了嚣哑下的力量");
                Utils.magicAttackCalculate(this, target, 60 + 200 * (getMaxHp() - getCurrentHp()) / getMaxHp());
                list.remove(target);
            }
        }
    }

}
