package character;

import buff.Buff;
import game.Team;
import utils.Utils;

public class Character_MaYuanYuan extends Character{

    private boolean isUsedSkillOne = false;

    public Character_MaYuanYuan(Team team, int location) {
        super("马圆圆", 800, 350, 55, 240, 10, 40, team, location);
    }

    @Override
    public void characterMotion() {
        super.characterMotion();
        skillOne();
        if (Utils.random(40) && Utils.enoughMp(this, 50)) {
            skillThree();
        }
        if (Utils.random(40 + getLoseHPPercent()) && Utils.enoughMp(this, 90)) {
            skillFour();
        } else {
            normalAtk();
        }
    }

    @Override
    public void afterAttack(Character main, Character passive, int fromWhere, int damage) {
        super.afterAttack(main, passive, fromWhere, damage);
        if (passive.equals(this)) {
            skillTwo(damage);
        }
    }

    /**
     * 孤注一掷（被动）
     * 你的回合开始时，如果你的HP低于50%，回复你300点HP，每局游戏限一次
     */
    private void skillOne() {
        if (!isUsedSkillOne && getHPPercent() < 50) {
            Utils.print(getName() + "还有最后的底牌");
            Utils.cureCalculate(this, this, 300);
            isUsedSkillOne = true;
        }
    }

    /**
     * 底力（被动）
     * 你每失去40点HP，永久增加你5点护甲
     */
    private void skillTwo(int damage) {
        int num = damage / 40;
        setDef(getDef() + num * 5);
    }

    /**
     * 咆哮（主动） 50MP
     * 增加你40点护甲至你下个回合开始
     */
    private void skillThree() {
        Utils.print(getName() + "使用了咆哮，提高了护甲");
        Utils.addBuff(getPlusDefBuffs(), new Buff("咆哮", 40, false, 1));
    }

    /**
     * 毅力猛击（主动） 90MP
     * 对目标造成2.5*（你的护甲）的物理伤害
     */
    private void skillFour() {
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        Utils.print(getName() + "对" + character.getName() + "使用了毅力猛击");
        Utils.attackCalculate(this, character,
                (int) (2.5 * Utils.calculateDefByPlus(this) * Utils.calculateBuff(this.getDefBuffs())));
    }
}
