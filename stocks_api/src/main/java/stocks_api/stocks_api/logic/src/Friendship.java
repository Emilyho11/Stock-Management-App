package stocks_api.stocks_api.logic.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.Calendar;
import java.util.Date;

public class Friendship {

    private String username;
    private String target;
    private String status;
    private Date rejected;

    public Friendship(String username, String target, String status, Date rejected) {
        this.username = username;
        this.target = target;
        this.status = status;
        this.rejected = rejected;
    }

    public String getUsername() {
        return this.username;
    }

    public String getTargetFriend() {
        return this.target;
    }

    public String getStatus() {
        return this.status;
    }

    //checks if the time since the last rejection between 2 users has passed 5 minutes
    private static boolean checkRejectionTime(ResultSet rs) {
        try {
            LocalDateTime lastrejection = rs.getTimestamp("rejected").toLocalDateTime();
            //OffsetDateTime lastrejection = OffsetDateTime.ofInstant(rs.getTimestamp("rejected")) ;
            LocalDateTime now = LocalDateTime.now();
            System.out.println(MINUTES.between(lastrejection, now));
            if (MINUTES.between(lastrejection, now) > 5 ) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {

            return false;
        }
    }

    //checks if a user can send a friend request to target
    public static boolean checkRequestable(String username, String target, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT * FROM friendship WHERE (username = ? AND target = ?);");
            stmt.setString(1, username);
            stmt.setString(2, target);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                String friendstatus = rs.getString("status").trim();
                //return friendstatus;
                //if there is an existing friend req, dont let them
                if ("Accepted".equals(friendstatus) || "Pending".equals(friendstatus)){
                   return false;
                }
                //if there is a rejection already, check if the time has elapsed
                else if (friendstatus.equals("Rejected")){
                    return checkRejectionTime(rs);
                }
               return true;
            }
            System.out.println("Checked for this friendship requestability successfully");
            return true;
            // return "";
        } catch (SQLException ex) {
            System.out.println("Error finding this friendship's requestability: " + ex.getMessage());
            return false;
            // return "";
        }
    }

    //adds a pending friendship into the friendship table, with username username and target target
    public static void sendFriendRequest(String username, String target, Connection conn) {
        try {
            boolean requestable = checkRequestable(username, target, conn);
            System.out.println(requestable);
            if (requestable){
                PreparedStatement stmt;
                stmt = conn.prepareStatement("INSERT INTO friendship (username, target, status, rejected) "
                        + "VALUES (? , ?, 'Pending', ?) ON CONFLICT (username, target) DO UPDATE SET status = EXCLUDED.status;");
                stmt.setString(1, username);
                stmt.setString(2, target);
                stmt.setDate(3, null);
                stmt.executeUpdate();
                System.out.println("Friend request sent successfully");
            }
            // } else if (requestable.equals("Update")){
            //     PreparedStatement stmt;
            //     stmt = conn.prepareStatement("UPDATE friendship "
            //             + "SET status = 'Pending' "
            //             + "WHERE (username = ? AND target = ?);");
            //     stmt.setString(1, username);
            //     stmt.setString(2, target);
            //     stmt.executeUpdate();
            // }
        } catch (SQLException ex) {
            System.out.println("Error sending friend request: " + ex.getMessage());
        }
    }

    //changes a friendship status to accepted
    public static void acceptFriendRequest(String username, String target, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO friendship VALUES (?, ?, 'Accepted', ?), (?, ?, 'Accepted', ?) ON CONFLICT (username, target) DO UPDATE SET status = EXCLUDED.status;");
                    // + "SET status = 'Accepted' "
                    // + "WHERE ((username = ? AND target = ?) OR (username = ? AND target = ?));");
            stmt.setString(1, username);
            stmt.setString(2, target);
            stmt.setTimestamp(3, null);
            stmt.setString(4, target);
            stmt.setString(5, username);
            stmt.setTimestamp(6, null);
            // PreparedStatement stmt2;
            // stmt2 = conn.prepareStatement("INSERT INTO friendship VALUES (?, ?, 'Accepted', ?) ON CONFLICT (username, target) DO UPDATE SET status = EXCLUDED.status;");
            // stmt2.setString(1, target);
            // stmt2.setString(2, username);
            // stmt2.setTimestamp(3, null);
            stmt.executeUpdate();
            //stmt2.executeUpdate();
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
            stmt = conn.prepareStatement("SELECT username FROM friendship "
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
                    + "WHERE (username = ? AND status = 'Accepted'));");
            stmt.setString(1, myuser);
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
