package Service;

import java.util.List;

import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = new AccountDAO();
    }

    // Validates and saves a new message
    public Message registerMessage(Message message) {
        // Check if user exists
        Account existingUser = accountDAO.getAccountbyID(message.getPosted_by());
        if (existingUser == null) {
            return null;
        }

        // Validate message content
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            return null;
        }

        if (message.getMessage_text().length() > 255) {
            return null;
        }

        // Passed all checks, create the message
        return messageDAO.addMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int messageId) {
        return messageDAO.getMessageByID(messageId);
    }

    public Message delMessageById(int messageId) {
        Message existing = messageDAO.getMessageByID(messageId);
        if (existing != null) {
            return messageDAO.delMessage(existing);
        }
        return null;
    }

    public Message updateMessage(int messageId, String newText) {
        // Validate new text
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }

        Message existing = messageDAO.getMessageByID(messageId);
        if (existing == null) {
            return null;
        }

        return messageDAO.updateMessage(existing, newText);
    }

    public List<Message> allMessages(int accountId) {
        return messageDAO.allmessages(accountId);
    }
}