package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO is a class that mediates the transformation of data between the 
 * format of objects in Java to rows in a database. The methods here are 
 * mostly filled out, you will just need to add a SQL statement.
 */
public class AccountDAO {

    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> Accounts = new ArrayList<>();
        try {
            String sql = "select * from Account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getString("username"), rs.getString("password"));
                Accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Accounts;
    }


    public Account getAccountbyUsername(String usernameString){
        Account account = null;
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from Account where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usernameString);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                account = new Account(id, username, password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
          return account;
        }

        public Account getAccountbyID(int account_id){
            Account account = null;
            Connection connection = ConnectionUtil.getConnection();
            try {
                String sql = "select * from Account where account_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, account_id);
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()){
                    int id = rs.getInt("account_id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    account = new Account(id, username, password);
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
              return account;
            }


        public Account addAccount(Account account){
            Connection connection = ConnectionUtil.getConnection();
            try {
                String sql = "insert into Account(username, password) values (?,?)" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getPassword());
                
                preparedStatement.executeUpdate();
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_account_id = (int) pkeyResultSet.getLong(1);
                    return new Account(generated_account_id,account.getUsername(),account.getPassword());
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return null;
        }
}