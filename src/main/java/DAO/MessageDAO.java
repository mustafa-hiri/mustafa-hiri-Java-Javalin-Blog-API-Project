package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
   
    
    public Message addMessage(Message message){
            Connection connection = ConnectionUtil.getConnection();
            try {
                String sql = "insert into Message(posted_by,message_text,time_posted_epoch) values (?,?,?)" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                
                preparedStatement.setInt(1, message.getPosted_by());
                preparedStatement.setString(2, message.getMessage_text());
                preparedStatement.setLong(3, message.getTime_posted_epoch());
                
                preparedStatement.executeUpdate();
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_message_id = (int) pkeyResultSet.getLong(1);
                    return new Message(generated_message_id,message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return null;
        }

        public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){    
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageByID(int MesssgeId){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            String sql = "select * from Message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, MesssgeId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){    
                message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    public Message delMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql= "delete from Message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,message.getMessage_id());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return message; 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message updateMessage(Message message, String textUpdate){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "update Message set message_text = ? where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, textUpdate);
            preparedStatement.setInt(2, message.getMessage_id());
            int affectedRow = preparedStatement.executeUpdate();
            if(affectedRow>0){
                message.setMessage_text(textUpdate);
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> allmessages(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from Message where posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){    
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}