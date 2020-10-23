package maciej;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Lang {

    private final YamlConfiguration lang;

    public Lang(YamlConfiguration lang){
        this.lang = lang;
    }

    public String getplayerFoundDiamond(Player player) {
        return String.format(Objects.requireNonNull(lang.getString("player-found-diamond")), player.getDisplayName());
    }

    public String getplayerFoundEmerald(Player player) {
        return String.format(Objects.requireNonNull(lang.getString("player-found-emerald")), player.getDisplayName());
    }

    public String getPlayerJoined(Player player) {
        return String.format(Objects.requireNonNull(lang.getString("player-joined")), player.getDisplayName());
    }

    public String getPlayerLeft(Player player) {
        return String.format(Objects.requireNonNull(lang.getString("player-left")), player.getDisplayName());
    }

    public String getChatColorOnJoinTip() {
        return Objects.requireNonNull(lang.getString("color-change-on-join-tip"));
    }

    public String getPlayerPlacedTNT(Player player, Location location) {
        return String.format(Objects.requireNonNull(lang.getString("player-placed-tnt")), player.getDisplayName(),
                location.getX(), location.getY(), location.getZ());
    }

    public String getPlayerSetFireOnTNT(Player player, Location location) {
        return String.format(Objects.requireNonNull(lang.getString("player-set-fire-on-tnt")), player.getDisplayName(),
                location.getX(), location.getY(), location.getZ());
    }

    public String getShellCouldNotExecute() {
        return Objects.requireNonNull(lang.getString("shell-could-not-execute"));
    }

    public String getShellFinishedWithResult(int result) {
        return String.format(Objects.requireNonNull(lang.getString("shell-finished-with-result")), result);
    }

    public String getShellErrOnReadOutput() {
        return Objects.requireNonNull(lang.getString("shell-error-on-read-output"));
    }

    public String getChatColorNewColor() {
        return Objects.requireNonNull(lang.getString("chat-color-new-color"));
    }

    public String getVotekickStart(Player playerStarting, Player targetPlayer) {
        return String.format(Objects.requireNonNull(lang.getString("votekck-start")),
                playerStarting.getDisplayName(),
                targetPlayer.getDisplayName());
    }

    public String getToVotekickYes() {
        return Objects.requireNonNull(lang.getString("to-vote-yes"));
    }

    public String getHeWantToKickToo(Player playerStarting) {
        return String.format(Objects.requireNonNull(lang.getString("he-want-to-kick-too")),
                playerStarting.getDisplayName());
    }

    public String getInfoForKickedPlayer() {
        return Objects.requireNonNull(lang.getString("info-for-kicked-player"));
    }

    public String getFailedVotekick(Player player) {
        return String.format(Objects.requireNonNull(lang.getString("failed-votekick")),
                player.getDisplayName());
    }

    public String getVotekickTimeLeft(int time) {
        return String.format(Objects.requireNonNull(lang.getString("votekick-time-left")),
                time);
    }

    public String getMoreVotesNeeded(int amount) {
        return String.format(Objects.requireNonNull(lang.getString("more-votes-needed")),
                amount);
    }
}
