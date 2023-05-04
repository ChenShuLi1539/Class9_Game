package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Character_WeiChengYang extends Character{
    public Character_WeiChengYang(Team team, int location) {
        super("韦承洋", 900, 320, 65, 280, 30, 30, team, location);
    }

    @Override
    public void afterAttack(Character main, Character passive, int fromWhere, int damage) {
        super.afterAttack(main, passive, fromWhere, damage);
        if (main.equals(this)) {
            skillOne(passive);
        }
    }

    @Override
    public void characterMotion() {
        skillTwo();
        if (Utils.random(50) && Utils.enoughMp(this, 110)) {
            skillThree();
        } else {
            normalAtk();
        }
    }

    /**
     * 耀眼锋芒（被动）
     * 你每次造成伤害后，有10%的几率使对方下回合无法普通攻击
     */
    private void skillOne(Character character) {
        if (Utils.calculateForbidSuccess(this, character, 10)) {
            Utils.addBuff(character.getForbidBuffs(), new Buff("耀眼锋芒", 0, false, 2));
            Utils.print(getName() + "令" + character.getName() + "被强光灼目，不能进行普通攻击了");
        } else {
            Utils.print(getName() + "的耀眼锋芒没能封印命中");
        }
    }

    /**
     * 盛气凌人（被动）
     * 回合开始时，如果我方存活人数落后，临时提高你本回合20%封印命中率
     */
    private void skillTwo() {
        if (team.getStillAlive().size() < Utils.getAnotherTeam(this).getStillAlive().size()) {
            Utils.print("局面上的落后激起了" + getName() + "的斗志，临时提高了封印命中率");
            Utils.addBuff(getForbidSuccessPlusBuffs(), new Buff("盛气凌人", 20, false, 1));
        }
    }

    /**
     * 残影（主动） 110MP
     * 对对方随机三个目标造成110点物理伤害，并提高你10%的速度一回合
     */
    private void skillThree() {
        List<Character> team = Utils.getAnotherTeam(this).getStillAlive();
        if (team.size() <= 3) {
            for (Character character : team) {
                Utils.print(getName() + "对" + character.getName() + "使用了残影");
                Utils.attackCalculate(this, character, 110);
            }
        } else {
            List<Integer> randomList = new ArrayList<>();
            while (randomList.size() < 3) {
                int random = (int) (Math.random() * team.size());
                if (!randomList.contains(random)) {
                    randomList.add(random);
                }
            }
            for (Integer i : randomList) {
                Character character = team.get(i);
                Utils.print(getName() + "对" + character.getName() + "使用了残影");
                Utils.attackCalculate(this, character, 110);
            }
        }
        Utils.print(getName() + "的速度提高了");
        Utils.addBuff(getSpdBuffs(), new Buff("残影", 1.1, false, 1));
    }
}
