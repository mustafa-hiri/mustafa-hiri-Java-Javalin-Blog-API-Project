package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;


public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO AccountDAO){
        this.accountDAO = AccountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account getAccountbyUsername(String usernameString){
        return accountDAO.getAccountbyUsername(usernameString);
    }

    public Account getAccountbyID(int account_id){
        return accountDAO.getAccountbyID(account_id);
    }

    public Account register(Account account){
        return accountDAO.addAccount(account);
    }
}
