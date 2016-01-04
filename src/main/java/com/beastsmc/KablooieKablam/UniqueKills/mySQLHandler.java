package com.beastsmc.KablooieKablam.UniqueKills;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLHandler
{
  private final String url;
  private final String user;
  private final String pass;
  private Connection dbConn;
  private static final String getKillsWorld = "select count(distinct victim) as kills from `lb-world-kills` where (select ip from `lb-players` where playerid=victim)!='' and killer=(select playerid from `lb-players` where playername=?);";
  private static final String getKillsNether = "select count(distinct victim) as kills from `lb-world_nether-kills` where (select ip from `lb-players` where playerid=victim)!='' and killer=(select playerid from `lb-players` where playername=?);";
  private static final String getKillsEnd = "select count(distinct victim) as kills from `lb-world_the_end-kills` where (select ip from `lb-players` where playerid=victim)!='' and killer=(select playerid from `lb-players` where playername=?);";
  private static final String getDeathsWorld = "select count(distinct killer) as deaths from `lb-world-kills` where (select ip from `lb-players` where playerid=killer)!='' and victim=(select playerid from `lb-players` where playername=?);";
  private static final String getDeathsNether = "select count(distinct killer) as deaths from `lb-world_nether-kills` where (select ip from `lb-players` where playerid=killer)!='' and victim=(select playerid from `lb-players` where playername=?);";
  private static final String getDeathsEnd = "select count(distinct killer) as deaths from `lb-world_the_end-kills` where (select ip from `lb-players` where playerid=killer)!='' and victim=(select playerid from `lb-players` where playername=?);";
  private static final String getAllPlayerKillsWorld = "select count(distinct victim) as kills,(select playername from `lb-players` where playerid=killer) as playername from `lb-world-kills` where (select ip from `lb-players` where playerid=victim)!='' and (select ip from `lb-players` where playerid=killer)!='' group by killer order by count(distinct victim) desc;";
  private static final String getAllPlayerKillsNether = "select count(distinct victim) as kills,(select playername from `lb-players` where playerid=killer) as playername from `lb-world_nether-kills` where (select ip from `lb-players` where playerid=victim)!='' and (select ip from `lb-players` where playerid=killer)!='' group by killer order by count(distinct victim) desc;";
  private static final String getAllPlayerKillsEnd = "select count(distinct victim) as kills,(select playername from `lb-players` where playerid=killer) as playername from `lb-world_the_end-kills` where (select ip from `lb-players` where playerid=victim)!='' and (select ip from `lb-players` where playerid=killer)!='' group by killer order by count(distinct victim) desc;";

  public MySQLHandler(String db, String host, String port, String username, String password)
    throws SQLException
  {
    this.url = String.format("jdbc:mysql://%s:%s/%s", new Object[] { host, port, db });
    this.user = username;
    this.pass = password;
    this.dbConn = DriverManager.getConnection(this.url, this.user, this.pass);
  }

  private Connection getConnection() {
    try {
      if (this.dbConn.isClosed())
        this.dbConn = DriverManager.getConnection(this.url, this.user, this.pass);
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return this.dbConn;
  }

  public int getPlayerKills(String name) throws SQLException {
    PreparedStatement[] stmts = { 
      getConnection().prepareStatement("select count(distinct victim) as kills from `lb-world-kills` where (select ip from `lb-players` where playerid=victim)!='' and killer=(select playerid from `lb-players` where playername=?);"), 
      getConnection().prepareStatement("select count(distinct victim) as kills from `lb-world_nether-kills` where (select ip from `lb-players` where playerid=victim)!='' and killer=(select playerid from `lb-players` where playername=?);"), 
      getConnection().prepareStatement("select count(distinct victim) as kills from `lb-world_the_end-kills` where (select ip from `lb-players` where playerid=victim)!='' and killer=(select playerid from `lb-players` where playername=?);") };

    int total = 0;
    ResultSet rs;
    for (PreparedStatement stmt : stmts) {
      stmt.setString(1, name);
      rs = stmt.executeQuery();
      if (rs.first()) {
        int kills = rs.getInt("kills");
        total += kills;
      }
      rs.close();
      stmt.close();
    }
    PreparedStatement[] dstmts = { 
      getConnection().prepareStatement("select count(distinct killer) as deaths from `lb-world-kills` where (select ip from `lb-players` where playerid=killer)!='' and victim=(select playerid from `lb-players` where playername=?);"), 
      getConnection().prepareStatement("select count(distinct killer) as deaths from `lb-world_nether-kills` where (select ip from `lb-players` where playerid=killer)!='' and victim=(select playerid from `lb-players` where playername=?);"), 
      getConnection().prepareStatement("select count(distinct killer) as deaths from `lb-world_the_end-kills` where (select ip from `lb-players` where playerid=killer)!='' and victim=(select playerid from `lb-players` where playername=?);") };

    for (PreparedStatement stmt : dstmts) {
      stmt.setString(1, name);
      ResultSet rs = stmt.executeQuery();
      if (rs.first()) {
        int deaths = rs.getInt("deaths");
        total -= deaths;
      }
      rs.close();
      stmt.close();
    }
    return total;
  }

  public ArrayList<PlayerScoreNode> getPlayerKills() throws SQLException {
    PreparedStatement[] stmts = { 
      getConnection().prepareStatement("select count(distinct victim) as kills,(select playername from `lb-players` where playerid=killer) as playername from `lb-world-kills` where (select ip from `lb-players` where playerid=victim)!='' and (select ip from `lb-players` where playerid=killer)!='' group by killer order by count(distinct victim) desc;"), 
      getConnection().prepareStatement("select count(distinct victim) as kills,(select playername from `lb-players` where playerid=killer) as playername from `lb-world_nether-kills` where (select ip from `lb-players` where playerid=victim)!='' and (select ip from `lb-players` where playerid=killer)!='' group by killer order by count(distinct victim) desc;"), 
      getConnection().prepareStatement("select count(distinct victim) as kills,(select playername from `lb-players` where playerid=killer) as playername from `lb-world_the_end-kills` where (select ip from `lb-players` where playerid=victim)!='' and (select ip from `lb-players` where playerid=killer)!='' group by killer order by count(distinct victim) desc;") };

    ArrayList scores = new ArrayList();

    for (PreparedStatement stmt : stmts) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        scores.add(new PlayerScoreNode(rs.getString("playername"), rs.getInt("kills")));
      }
      rs.close();
      stmt.close();
    }

    return scores;
  }
}

/* Location:           /Users/zane/KillScore.jar
 * Qualified Name:     com.beastsmc.KablooieKablam.UniqueKills.MySQLHandler
 * JD-Core Version:    0.6.2
 */