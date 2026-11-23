package kr.cobblebrain.ko;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Only affects visible chat strings, NEVER CobbleBrain AI's internal identifiers. */
public final class KoreanText {
    private KoreanText() {}
    private static final Map<String, String> SPECIES = new HashMap<>();
    private static final Pattern FRIENDSHIP = Pattern.compile("^Friendship\\s+(.+?):\\s*([+-]\\d+)\\s*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCOUT_SPOT = Pattern.compile("^(.+?) spotted a (.+?) at (X:[^\\s]+\\s+Y:[^\\s]+\\s+Z:[^\\s]+)\\.?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCOUT_DONE = Pattern.compile("^(.+?) completed its reconnaissance\\.$", Pattern.CASE_INSENSITIVE);
    private static final Pattern POKEMON_CHAT = Pattern.compile("^([^:]{1,80}):\\s*(.+)$", Pattern.DOTALL);

    static {
        try (InputStream in = KoreanText.class.getClassLoader().getResourceAsStream("pokemon_names.tsv")) {
            if (in != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int t = line.indexOf('\t');
                        if (t < 1) continue;
                        String en = line.substring(0,t).trim();
                        String ko = line.substring(t + 1).trim();
                        if (!en.isEmpty() && !ko.isEmpty()) SPECIES.put(en.toLowerCase(Locale.ROOT), ko);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[CobbleBrain Korean] species dictionary load failed: " + e);
        }
    }

    private static String name(String s) { return SPECIES.getOrDefault(s.trim().toLowerCase(Locale.ROOT), s.trim()); }
    private static boolean knownName(String s) { return SPECIES.containsKey(s.trim().toLowerCase(Locale.ROOT)); }

    public static String translate(String text) {
        if (text == null || text.isEmpty()) return text;
        Matcher m = FRIENDSHIP.matcher(text);
        if (m.matches() && knownName(m.group(1))) return name(m.group(1)) + " 친밀도 " + m.group(2);
        m = SCOUT_SPOT.matcher(text);
        if (m.matches() && knownName(m.group(1))) return name(m.group(1)) + " — 정찰 중 발견: " + translatePlace(m.group(2)) + " (" + m.group(3) + ")";
        m = SCOUT_DONE.matcher(text);
        if (m.matches() && knownName(m.group(1))) return name(m.group(1)) + "가 정찰을 마쳤습니다.";
        m = POKEMON_CHAT.matcher(text);
        if (m.matches() && knownName(m.group(1))) return name(m.group(1)) + ": " + m.group(2);
        return text;
    }
    private static String translatePlace(String s) {
        return switch(s.toLowerCase(Locale.ROOT)) {
            case "shipwreck" -> "난파선";
            case "village" -> "마을";
            case "temple" -> "사원";
            default -> s;
        };
    }
}
