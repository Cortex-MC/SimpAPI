package me.kodysimpson.simpapi.region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static org.bukkit.Bukkit.getServer;

//Created by milo
public class RegionSelector {

    public static void killSelectorsWithTag(String inputTag) {
        for (World world : getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                for (String tag : entity.getScoreboardTags()) {
                    if (tag.equals("regionselector-" + inputTag)) {
                        entity.remove();
                        break;
                    }
                }
            }
        }
    }

    public static void killAllSelectors() {
        for (World world : getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                for (String tag : entity.getScoreboardTags()) {
                    if (tag.startsWith("regionselector-")) {
                        entity.remove();
                        break;
                    }
                }
            }
        }
    }

    public static void removeTempTeams() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team team : board.getTeams()) {
            if (team.getName().startsWith("regionselector+")) {
                team.unregister();
            }
        }
    }

    public static void drawSelector(Region region, String id, ChatColor glowColor) {

        Location loc1 = region.getCorner1();
        Location loc2 = region.getCorner2();
        World world = region.getWorld();
        if (loc1.getWorld() == null || !loc1.getWorld().equals(loc2.getWorld())) {
            return;
        }
        int minx = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int miny = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxx = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxy = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        int highestNumber = 0;

        for (Team team : board.getTeams()) {
            if (team.getName().startsWith("regionselector+")) {
                String number = team.getName().substring(13);
                try {
                    int tempNumber = Integer.parseInt(number);
                    if (tempNumber >= highestNumber) {
                        highestNumber = tempNumber;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        Team selectorTeam = board.registerNewTeam("regionselector+" + (highestNumber + 1));
        if (glowColor != null) {
            selectorTeam.setColor(glowColor);
        }
        selectorTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        for (double x = minx; x <= maxx; x++) {
            makeSlime(id, world, selectorTeam, x, miny, minz);
            makeSlime(id, world, selectorTeam, x, maxy, minz);
            makeSlime(id, world, selectorTeam, x, miny, maxz);
            makeSlime(id, world, selectorTeam, x, maxy, maxz);
        }

        for (double y = miny; y <= maxy; y++) {
            makeSlime(id, world, selectorTeam, minx, y, minz);
            makeSlime(id, world, selectorTeam, maxx, y, minz);
            makeSlime(id, world, selectorTeam, minx, y, maxz);
            makeSlime(id, world, selectorTeam, maxx, y, maxz);
        }

        for (double z = minz; z <= maxz; z++) {
            makeSlime(id, world, selectorTeam, minx, miny, z);
            makeSlime(id, world, selectorTeam, maxx, miny, z);
            makeSlime(id, world, selectorTeam, minx, maxy, z);
            makeSlime(id, world, selectorTeam, maxx, maxy, z);
        }

    }

    private static void makeSlime(String id, World world, Team selectorTeam, double x, double y, double z) {
        Slime slime = (Slime) world.spawnEntity(new Location(world, x + 0.5, y, z + 0.5), EntityType.SLIME);
        slime.setSize(2);
        slime.setAI(false);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setSilent(true);
        slime.setCanPickupItems(false);
        slime.setGlowing(true);
        slime.setInvulnerable(true);
        slime.setInvisible(true);
        slime.addScoreboardTag("regionselector-" + id);
        slime.setPersistent(true);
        selectorTeam.addEntry(String.valueOf(slime.getUniqueId()));
    }

}