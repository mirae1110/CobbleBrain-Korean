package kr.cobblebrain.ko;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Translators for CobbleBrain-produced chat lines. Other mods' chat should pass untouched. */
public final class KoreanText {
    private KoreanText() {}
    private static final Map<String, String> SPECIES = new LinkedHashMap<>();
    private static final Pattern FRIENDSHIP = Pattern.compile("^Friendship\\s+(.+?):\\s*([+-]\\d+)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCOUT_SPOT = Pattern.compile("^(.+?) spotted a (.+?) at (X:[^\\s]+\\s+Y:[^\\s]+\\s+Z:[^\\s]+)\\.?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCOUT_DONE = Pattern.compile("^(.+?) completed its reconnaissance\\.$", Pattern.CASE_INSENSITIVE);
    private static final Pattern POKEMON_CHAT = Pattern.compile("^([A-Za-z][A-Za-z .'-]{1,40}):\\s*(.+)$");
    static {
        SPECIES.put("Torchic", "아차모");
        SPECIES.put("Staravia", "찌르버드");
        SPECIES.put("Staraptor", "찌르호크");
        SPECIES.put("Starly", "찌르꼬");
        SPECIES.put("Absol", "앱솔");
        SPECIES.put("Metagross", "메타그로스");
        SPECIES.put("Charizard", "리자몽");
        SPECIES.put("Pikachu", "피카츄");
        SPECIES.put("Bulbasaur", "이상해씨");
        SPECIES.put("Charmander", "파이리");
        SPECIES.put("Squirtle", "꼬부기");
        SPECIES.put("Eevee", "이브이");
        SPECIES.put("Piplup", "팽도리");
        SPECIES.put("Meowth", "나옹");
    }
    private static String name(String s) { return SPECIES.getOrDefault(s.trim(), s.trim()); }
    public static String translate(String s) {
        Matcher m = FRIENDSHIP.matcher(s);
        if (m.matches()) return name(m.group(1)) + " 친밀도 " + m.group(2);
        m = SCOUT_SPOT.matcher(s);
        if (m.matches()) return name(m.group(1)) + " — 정찰 중 발견: " + translatePlace(m.group(2)) + " (" + m.group(3) + ")";
        m = SCOUT_DONE.matcher(s);
        if (m.matches()) return name(m.group(1)) + "가 정찰을 마쳤습니다.";
        m = POKEMON_CHAT.matcher(s);
        if (m.matches() && SPECIES.containsKey(m.group(1))) return name(m.group(1)) + ": " + m.group(2);
        return s;
    }
    private static String translatePlace(String s) {
        return switch(s.toLowerCase()) {
            case "shipwreck" -> "난파선";
            case "village" -> "마을";
            case "temple" -> "사원";
            default -> s;
        };
    }
}
