package backend_old.src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public boolean checkCanRequest() {
        if (this.timeRejected == null) {
            return true;
        }
        //return if ((new Date() - this.timeRejected) > 5);
        return true;
    }

    //checks for if two users are already friends
    public boolean checkFriends(String username, String target) {
        //search in database
        return false;
    }

    //checks if there is already a pending request from username to target
    public boolean checkFriendRequest(String username, String target) {
        //search in database
        return false;
    }

    //finds any friendship requests between two users
    public Friendship findFriendship(String username, String target) {
        //search in database
        return null;
    }

    //adds a pending friendship into the friendship table, with username username and target target
    public static void sendFriendRequest(String username, String target, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("INSERT INTO friendship (username, target, status, rejected) "
                    + "VALUES (? , ?, 'Pending', ?)");
            stmt.setString(1, username);
            stmt.setString(2, target);
            stmt.setDate(3, null);
            stmt.executeUpdate();

        } catch (SQLException ex) {
        }
    }

    //changes a friendship status to accepted
    public void acceptFriendRequest(String username, String target, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE Friendship "
                    + "SET status = Accepted "
                    + "WHERE ((username = ? && target = ?) || (username = ? && target = ?))");
            stmt.setString(1, username);
            stmt.setString(2, target);
            stmt.setString(3, target);
            stmt.setString(4, username);
            stmt.executeUpdate();
        } catch (SQLException ex) {
        }
    }

    //changes a friendship status to rejected and records the time rejected
    public void rejectFriendRequest(String username, String target, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE Friendship "
                    + "SET status = Rejected, timeRejected = ?"
                    + "WHERE (username = ? && target = ?)");
            java.sql.Time time = new java.sql.Time(Calendar.getInstance().getTime().getTime());
            stmt.setTime(1, time);
            stmt.setString(2, username);
            stmt.setString(3, target);
            stmt.executeUpdate();
        } catch (SQLException ex) {
        }
    }

    //get all friend requests sent by the user
    public ResultSet viewSentFriendRequests(String myuser, Connection conn) {
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT target FROM Friendship "
                    + "WHERE (username = ? && status = Pending");
            stmt.setString(1, myuser);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
        }
        return null;
    }

    //get all friend requests sent to the user
    public ResultSet viewIncomingFriendRequests(String myuser, Connection conn) {
        try {
            //get friendships from database
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT target FROM Friendship "
                    + "WHERE (target = ? && status = Pending");
            stmt.setString(1, myuser);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
        }
        return null;
    }

    //get all friends of the user
    public ResultSet viewFriends(String myuser, Connection conn) {
        try {
            //get friendships from database
            PreparedStatement stmt;
            stmt = conn.prepareStatement("SELECT target FROM Friendship "
                    + "WHERE (username = ? && status = Accepted");
            stmt.setString(1, myuser);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
        }
        return null;
    }
}
