package character;

import game.Team;
import utils.Utils;

public class Character_QinSiNing extends Character{

    /**
     * 标志物“意”
     */
    public int mark = 0;

    public Character_QinSiNing(Team team, int location) {
        super("覃思宁", 800, 0, 40, 250, 30, 70, team, location);
    }

    @Override
    public void characterMotion() {
        skillOne();
        if (Utils.random(30) && enoughMark(100)) {
            skillTwo();
        } else if (Utils.random(60) && enoughMark(250)) {
            skillThree();
        } else {
            normalAtk();
        }
        if (getCurrentHp() <= 350 && mark > 0) {
            skillFour();
        }
    }

    @Override
    public String printState() {
        return "HP:" + currentHp + "/" + maxHp + " 意:" + mark + "/" + 600;
    }

    /**
     * 随心所欲（被动）
     * 对方每消耗1点MP，获得一点“意”；你的每个回合开始时，获得50点“意”；你最多可以储存600点“意”
     */
    private void skillOne() {
        mark += 50;
        if (mark > 600) {
            mark = 600;
        }
    }

    /**
     * 震耳欲聋（主动） 100点“意”
     * 对目标造成90点穿透伤害，并减少其20%速度储存量
     */
    private void skillTwo() {
        Character character = Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic();
        Utils.print(getName() + "对" + character.getName() + "使用了震耳欲聋，" +
                character.getName() + "丢失了" + (int) (0.2 * character.getSpdRemain()) + "点速度储存量");
        character.setSpdRemain(character.getSpdRemain() * 0.8);
        Utils.trueAttackCalculate(this, character, 90);
    }

    /**
     * 剑语（主动） 250点“意”
     * 对对方HP最低的目标造成280*（对方已损失HP%）的魔法伤害
     */
    private void skillThree() {
        Character character = Utils.getAnotherTeam(this).getHPLowest();
        Utils.print(getName() + "对" + character.getName() + "使用了剑语");
        Utils.magicAttackCalculate(this, character, 280 * character.getLoseHPPercent() / 100);
    }

    /**
     * 流连其中（主动）
     * 消耗你所有的“意”，为你回复50%*丢弃数的HP
     */
    private void skillFour() {
        Utils.print(getName() + "使用了流连其中");
        Utils.cureCalculate(this, this, (int) (0.5 * mark));
        mark = 0;
    }

    private boolean enoughMark(int value) {
        if (mark >= value) {
            mark -= value;
            Utils.print(getName() + "消耗了" + value + "点'意'，剩余" + mark + "点");
            return true;
        }
        return false;
    }
}
