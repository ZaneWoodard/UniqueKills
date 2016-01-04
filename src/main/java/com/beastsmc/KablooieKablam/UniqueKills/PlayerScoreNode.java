package com.beastsmc.KablooieKablam.UniqueKills;

public class PlayerScoreNode
{
  private final String playername;
  private int kills;

  public PlayerScoreNode(String name, int kills)
  {
    this.playername = name;
    this.kills = kills;
  }

  public String getName() {
    return this.playername;
  }

  public int getKills() {
    return this.kills;
  }

  public void addKills(int increment) {
    this.kills += increment;
  }
}