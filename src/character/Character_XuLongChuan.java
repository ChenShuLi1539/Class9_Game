package character;

import buff.Buff;
import game.Team;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Character_XuLongChuan extends Character{

    public Character_XuLongChuan(Team team, int location) {
        super("许珑川", 850, 300, 50, 260, 40, 30, team, location);
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
        if (Utils.random(40) && Utils.enoughMp(this, 60)) {
            skillFour();
        }
        if (Utils.random(65) && Utils.enoughMp(this, 100)) {
            skillThree();
        } else {
            normalAtk();
        }
    }

    /**
     * 缄默（被动）
     * 你前方一排的目标封印成功率提高10%
     */
    public void skillOne() {
        if (location / 3 + 1 < 3) {
            if (getTeam().getLocationList()[(location / 3 + 1) * 3] != null) {
                Utils.addBuff(getTeam().getLocationList()[(location / 3 + 1) * 3].getForbidSuccessPlusBuffs(),
                        new Buff("缄默", 10, true, 999));
            }
            if (getTeam().getLocationList()[(location / 3 + 1) * 3 + 1] != null) {
                Utils.addBuff(getTeam().getLocationList()[(location / 3 + 1) * 3 + 1].getForbidSuccessPlusBuffs(),
                        new Buff("缄默", 10, true, 999));
            }
            if (getTeam().getLocationList()[(location / 3 + 1) * 3 + 2] != null) {
                Utils.addBuff(getTeam().getLocationList()[(location / 3 + 1) * 3 + 2].getForbidSuccessPlusBuffs(),
                        new Buff("缄默", 10, true, 999));
            }
        }
    }

    /**
     * 勇者的庇护（被动）
     * 你的回合开始时，治疗我方随机一人30*（我方被击倒数）的HP
     */
    public void skillTwo() {
        Character character = getTeam().getRandomOneStillAliveByMagic();
        Utils.print(getName() + "的回合开始使用勇者的庇护治疗了" + character.getName());
        Utils.cureCalculate(this, character,
                30 * (getTeam().getCharacterList().size() - getTeam().getStillAlive().size()));
    }

    /**
     * 一往无前（主动） 100MP
     * 对对方一排的目标造成95点魔法伤害，同时降低他们10%的抵抗封印几率两回合
     */
    public void skillThree() {
        for (Character character :
                Utils.getAnotherTeam(this).getSameRow(Utils.getAnotherTeam(this).getRandomOneStillAliveByMagic())) {
            if (character.getCurrentHp() > 0) {
                Utils.print(getName() + "对" + character.getName() + "使用了一往无前，降低了ta的抵抗封印几率");
                Utils.magicAttackCalculate(this, character, 95);
                Utils.addBuff(character.getForbidResistPlusBuffs(), new Buff("一往无前", 10, false, 3));
            }
        }
    }

    /**
     * 九变十化（主动） 60MP
     * 降低对方随机三个目标40%的治疗效果两回合
     */
    public void skillFour() {
        List<Character> team = Utils.getAnotherTeam(this).getStillAlive();
        if (team.size() <= 3) {
            for (Character character : team) {
                Utils.print(getName() + "变幻莫测，降低了" + character.getName() + "的受治疗效果");
                Utils.addBuff(character.getHpRecoverBuffs(), new Buff("九变十化", 0.6, false, 3));
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
                Utils.print(getName() + "变幻莫测，降低了" + character.getName() + "的受治疗效果");
                Utils.addBuff(character.getHpRecoverBuffs(), new Buff("九变十化", 0.6, false, 3));
            }
        }
    }
}
