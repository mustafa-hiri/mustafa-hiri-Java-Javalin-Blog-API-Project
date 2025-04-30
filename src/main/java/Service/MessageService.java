package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO AccountDAO){
        this.messageDAO = AccountDAO;
    }

    public Message registerMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int messageId){
        return messageDAO.getMessageByID(messageId);
    }

    public Message delMessage(Message message){
        return messageDAO.delMessage(message);
    }

    public Message updMessage(Message message, String messageUp){
        return messageDAO.updateMessage(message, messageUp);
    }

    public List<Message> allMessages(int accountId){
        return messageDAO.allmessages(accountId);
    }
}
