package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Registration logic including validations
    public Account register(Account account) {
        // Check if username or password are invalid
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null;
        }

        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        // Check if username already exists
        if (accountDAO.getAccountbyUsername(account.getUsername()) != null) {
            return null;
        }

        return accountDAO.addAccount(account);
    }

    // Login logic - match username and password
    public Account login(Account account) {
        Account existingAccount = accountDAO.getAccountbyUsername(account.getUsername());

        if (existingAccount != null &&
            existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }

        return null; // login failed
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account getAccountbyUsername(String usernameString) {
        return accountDAO.getAccountbyUsername(usernameString);
    }

    public Account getAccountbyID(int account_id) {
        return accountDAO.getAccountbyID(account_id);
    }
}