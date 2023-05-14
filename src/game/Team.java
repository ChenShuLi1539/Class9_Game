package game;

import character.Character;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Team {

    public static final int MAX_CHARACTER_NUMS = 5;

    private List<Character> characterList = new ArrayList<>(MAX_CHARACTER_NUMS);

    private Character[] locationList = new Character[9];

    private String teamName;

    public Team(String name) {
        teamName = name;
    }

    public void add(Character character, int location) {
        if (location >= 0 && location < 9) {
            characterList.add(character);
            locationList[location] = character;
        }
    }

    public Character getRandomOneStillAliveByMagic() {
        List<Character> list = new ArrayList<>();
        for (Character character : characterList) {
            if (character.getCurrentHp() > 0) {
                list.add(character);
            }
        }
        int random = new Random().nextInt(list.size());
        return list.get(random);
    }

    /**
     * 普通攻击会被前排所承受
     * @return
     */
    public Character getRandomOneStillAliveByNormalAttack(Character attacker) {
        List<Character> list = new ArrayList<>();
        for (Character character : characterList) {
            if (character.getCurrentHp() > 0) {
                list.add(character);
            }
        }
        int random = new Random().nextInt(list.size());
        if (Utils.hasBuff(attacker, "急袭")) {
            return list.get(random);
        }
        int location = list.get(random).getLocation();
        if (location + 3 < 9 && locationList[location + 3] != null) {
            if (location + 6 < 9 && locationList[location + 6] != null) {
                if (locationList[location + 6].getCurrentHp() > 0) {
                    return locationList[location + 6];
                }
            }
            if (locationList[location + 3].getCurrentHp() > 0) {
                return locationList[location + 3];
            }
        }
        return list.get(random);
    }

    public boolean hasFallDownCharacter() {
        List<Character> list = new ArrayList<>();
        for (Character character : characterList) {
            if (character.getCurrentHp() <= 0) {
                list.add(character);
            }
        }
        return list.size() != 0;
    }

    public Character getRandomOneFallDown() {
        List<Character> list = new ArrayList<>();
        for (Character character : characterList) {
            if (character.getCurrentHp() <= 0) {
                list.add(character);
            }
        }
        if (list.size() == 0) {
            return null;
        }
        int random = new Random().nextInt(list.size());
        return list.get(random);
    }

    public List<Character> getStillAlive() {
        List<Character> list = new ArrayList<>();
        for (Character character : characterList) {
            if (character.getCurrentHp() > 0) {
                list.add(character);
            }
        }
        return list;
    }

    public List<Character> getSameRow(Character character) {
        List<Character> list = new ArrayList<>();
        int row = character.getLocation() / 3;
        for (int i = 0; i < 3; i++) {
            if (locationList[3 * row + i] != null && locationList[3 * row + i].getCurrentHp() > 0) {
                list.add(locationList[3 * row + i]);
            }
        }
        return list;
    }

    public List<Character> getNumCharacter(int num) {
        List<Character> stillAlive = getStillAlive();
        if (num >= stillAlive.size()) {
            return stillAlive;
        } else {
            List<Integer> randomList = new ArrayList<>();
            List<Character> characters = new ArrayList<>();
            while (randomList.size() < num) {
                int random = (int) (Math.random() * stillAlive.size());
                if (!randomList.contains(random)) {
                    randomList.add(random);
                    characters.add(stillAlive.get(random));
                }
            }
            return characters;
        }
    }

    public double getMyTeamHPPercent() {
        List<Character> list = getStillAlive();
        int current = 0;
        int max = 0;
        for (Character character : list) {
            current += character.getCurrentHp();
            max += character.getMaxHp();
        }
        return (double) current / max;
    }

    public Character getHPLowest() {
        List<Character> list = getStillAlive();
        Character result = list.get(0);
        for (Character character : list) {
            if (character.getCurrentHp() < result.getCurrentHp()) {
                result = character;
            }
        }
        return result;
    }

    public List<Character> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(List<Character> characterList) {
        this.characterList = characterList;
    }

    public Character[] getLocationList() {
        return locationList;
    }

    public void setLocationList(Character[] locationList) {
        this.locationList = locationList;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
