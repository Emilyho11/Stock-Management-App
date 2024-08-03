package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.Calendar;
import java.util.Date;

public class Friendship {

    private String username;
    private String targetFriendUsername;
    private String status;
    private Date timeRejected;

    public Friendship() {
        //String username, String target, String status, Date rejection
        // this.username = username;
        // this.targetFriendUsername = target;
        // this.status = status;
        // this.timeRejected = rejection;
    }

    public String getUsername() {
        return this.username;
    }

    public String getTargetFriend() {
        return this.targetFriendUsername;
    }

    public String getStatus() {
        return this.status;
    }

    //checks if the time since the last rejection between 2 users has passed 5 minutes
    private static boolean checkRejectionTime(ResultSet rs) {
        try {
            LocalTime lastrejection = rs.getTime("rejected").toLocalTime();
            LocalTime now = LocalTime.now();
            if (MINUTES.between(now, lastrejection) > 5 ) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {

            return false;
        }
    }

    //checks if a user can send a friend request to target
    private static String checkRequestable(String username, String target, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT * FROM friendship WHERE ((username = ? AND target = ?) OR (username = ? AND target = ?));");
            stmt.setString(1, username);
            stmt.setString(2, target);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                String friendstatus = rs.getString("status");
                //if there is an existing friend req, dont let them
                if (friendstatus.equals("Accepted") || friendstatus.equals("Pending")){
                    return "No";
                }
                //if there is a rejection already, check if the time has elapsed
                else if (friendstatus.equals("Rejected")){
                    if (checkRejectionTime(rs)){
                        return "Update";
                    }
                    return "No";
                }
                return "Update";
            }
            System.out.println("Checked for this friendship requestability successfully");
            return "Yes";
        } catch (SQLException ex) {
            System.out.println("Error finding this friendship's requestability: " + ex.getMessage());
            return "No";
        }
    }

    //adds a pending friendship into the friendship table, with username username and target target
    public static void sendFriendRequest(String username, String target, Connection conn) {
        try {
            String requestable = checkRequestable(username, target, conn);
            if (requestable.equals("Yes")){
                PreparedStatement stmt;
                stmt = conn.prepareStatement("INSERT INTO friendship (username, target, status, rejected) "
                        + "VALUES (? , ?, 'Pending', ?);");
                stmt.setString(1, username);
                stmt.setString(2, target);
                stmt.setDate(3, null);
                stmt.executeUpdate();
                System.out.println("Friend request sent successfully");
            } else if (requestable.equals("Update")){
                PreparedStatement stmt;
                stmt = conn.prepareStatement("UPDATE friendship "
                        + "SET status = 'Pending' "
                        + "WHERE (username = ? AND target = ?);");
                stmt.setString(1, username);
                stmt.setString(2, target);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Error sending friend request: " + ex.getMessage());
        }
    }

    //changes a friendship status to accepted
    public static void acceptFriendRequest(String username, String target, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE friendship "
                    + "SET status = 'Accepted' "
                    + "WHERE ((username = ? AND target = ?) OR (username = ? AND target = ?));");
            stmt.setString(1, username);
            stmt.setString(2, target);
            stmt.setString(3, target);
            stmt.setString(4, username);
            stmt.executeUpdate();
            System.out.println("Friend request accepted successfully");
        } catch (SQLException ex) {
            System.out.println("Error accepting friend request: " + ex.getMessage());
        }
    }

    //changes a friendship status to rejected and records the time rejected
    public static void rejectFriendRequest(String username, String target, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE friendship "
                    + "SET status = 'Rejected', rejected = ? "
                    + "WHERE (username = ? AND target = ?);");
            java.sql.Timestamp time = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            stmt.setTimestamp(1, time);
            stmt.setString(2, target);
            stmt.setString(3, username);
            stmt.executeUpdate();
            System.out.println("Friend request rejected successfully");
        } catch (SQLException ex) {
            System.out.println("Error rejecting friend request: " + ex.getMessage());
        }
    }

    //get all friend requests sent by the user
    public static ResultSet viewSentFriendRequests(String myuser, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT target FROM friendship "
                    + "WHERE (username = ? AND status = 'Pending');");
            stmt.setString(1, myuser);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved sent friend requests successfully");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error viewing sent friend requests: " + ex.getMessage());
        }
        return null;
    }

    //get all friend requests sent to the user
    public static ResultSet viewIncomingFriendRequests(String myuser, Connection conn) {
        try {
            //get friendships from database
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT target FROM friendship "
                    + "WHERE (target = ? AND status = 'Pending');");
            stmt.setString(1, myuser);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved incoming friend requests successfully");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error viewing incoming friend request: " + ex.getMessage());
        }
        return null;
    }

    //get all friends of the user
    public static ResultSet viewFriends(String myuser, Connection conn) {
        try {
            //get friendships from database
            PreparedStatement stmt;
            stmt = conn.prepareStatement("(SELECT target FROM friendship "
                    + "WHERE (username = ? AND status = 'Accepted')) UNION ALL (SELECT target FROM friendship WHERE (target = ? AND status = 'Accepted'));");
            stmt.setString(1, myuser);
            stmt.setString(2, myuser);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Retrieved friends successfully");
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error viewing friends: " + ex.getMessage());
        }
        return null;
    }

    //delete 2 users friendship
    public static void deleteFriend(String username, String target, Connection conn) {
        try {
            //get friendships from database
            PreparedStatement stmt;
            stmt = conn.prepareStatement("DELETE FROM friendship "
                    + "WHERE ((username = ? AND target = ?) OR (username = ? AND target = ?));");
            stmt.setString(1, username);
            stmt.setString(2, target);
            stmt.setString(3, target);
            stmt.setString(4, username);
            stmt.executeUpdate();
            System.out.println("Deleted friendship successfully");
        } catch (SQLException ex) {
            System.out.println("Error deleting friend: " + ex.getMessage());
        }
    }
}
