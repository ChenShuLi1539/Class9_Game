package character;

import buff.Buff;
import game.Team;
import utils.Utils;

public class Character_GanNingDa extends Character{

    public Character_GanNingDa(Team team, int location) {
        super("甘宁达", 850, 350, 60, 280, 30, 50, team, location);
    }

    @Override
    public void prepareSkill() {
        super.prepareSkill();
        skillOne();
    }

    @Override
    public void characterMotion() {
        super.characterMotion();
        skillTwo();
        if (Utils.random(40) && Utils.enoughMp(this, 50)) {
            skillThree();
        }
        if (Utils.random(30 + getLoseHPPercent()) && Utils.enoughMp(this, 90)) {
            skillFour();
        } else {
            normalAtk();
        }
    }

    /**
     * 如虎添翼（被动）
     * 当你加速时，再加速15%
     */
    private void skillOne() {
        Utils.addBuff(getSpecialBuffs(), new Buff("如虎添翼", 0, true, 999));
    }

    /**
     * 后勇（被动）
     * 你的回合开始时，如果你的HP低于50%，临时提高你本回合15%的伤害
     */
    private void skillTwo() {
        if (getHPPercent() < 50) {
            Utils.addBuff(getDamageBuffs(), new Buff("后勇", 1.15, false, 1));
            Utils.print(getName() + "感受到了压力，临时提高了伤害");
        }
    }

    /**
     * 便捷英语（主动） 50MP
     * 降低对方全体20点护甲直到他们的回合开始
     */
    private void skillThree() {
        Utils.print(getName() + "使用了便捷英语，降低了对方全体的护甲");
        for (Character character : Utils.getAnotherTeam(this).getStillAlive()) {
            Utils.addBuff(character.getPlusDefBuffs(), new Buff("便捷英语", -20, false, 1));
        }
    }

    /**
     * 拨云见日（主动） 90MP
     * 该技能有10%的暴击几率，对HP低于自身的目标使用时，再提高20%暴击几率。对其造成180点物理伤害
     */
    private void skillFour() {
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        if (character.getCurrentHp() < getCurrentHp()) {
            Utils.addBuff(getCriticalStrikePlusBuffs(), new Buff("拨云见日", 30, false, 1));
        } else {
            Utils.addBuff(getCriticalStrikePlusBuffs(), new Buff("拨云见日", 10, false, 1));
        }
        Utils.print(getName() + "对" + character.getName() + "使用了拨云见日");
        Utils.attackCalculate(this, character, 180);
    }
}
