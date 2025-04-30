package Controller;

import java.security.Provider.Service;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.liferay.portal.kernel.service.AccountService;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
       // app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessagesIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessagesIdHandler);
        app.patch("/messages/{message_id}", this::patchMessagesIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAccountIdHandler);
        return app;
    }
    

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    /**private void exampleHandler(Context context) {
        context.json("sample text");
    }*/

    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();

    private void postRegisterHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if(account.getUsername() == null || account.getUsername().isBlank() || 
        account.getPassword() == null || account.getPassword().length()<4){
            context.status(400);
            return;
        }
        if(accountService.getAccountbyUsername(account.getUsername()) != null){
            context.status(400);
            return;
        }
        Account creatAccount = accountService.register(account);
        context.status(200).json(creatAccount);
    }

    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account userNameFromAccount = accountService.getAccountbyUsername(account.getUsername());
    
        if (userNameFromAccount != null &&
            account.getUsername().equals(userNameFromAccount.getUsername()) &&
            account.getPassword().equals(userNameFromAccount.getPassword())) {
            context.status(200).json(userNameFromAccount);
        } else {
            context.status(401);
        }
    }
    
    private void postMessagesHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Account ExistenceUser = accountService.getAccountbyID(message.getPosted_by());
        if(ExistenceUser!= null && !message.getMessage_text().isBlank() &&
        message.getMessage_text().length()<255 &&
        message.getPosted_by() == ExistenceUser.account_id){
            Message creatMessage = messageService.registerMessage(message);
            context.status(200).json(creatMessage);
        }
        else{
            context.status(400);
        }
    }
    private void getMessagesHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }
    private void getMessagesIdHandler(Context context)throws JsonProcessingException{
        int messageId = Integer.parseInt(context.pathParam("message_id"));

        Message messageRecived = messageService.getMessageByID(messageId);
        
        if(messageRecived == null){
            context.json("");
        }
        else context.status(200).json(messageRecived);
    }
    private void deleteMessagesIdHandler(Context context) throws JsonProcessingException{
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message existedMessage = messageService.getMessageByID(messageId);
        if(existedMessage == null){
            context.status(200);
            context.json("");
        } else{
            Message deletedMessage = messageService.delMessage(existedMessage);
            context.status(200).json(deletedMessage);
        }
    }
    private void patchMessagesIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode body_messageText = mapper.readTree(context.body());
        if (!body_messageText.has("message_text")) {
            context.status(400);
            return;
        }
        String newText = body_messageText.get("message_text").asText();
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            context.status(400);
            return;
        }


        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message existedMessage = messageService.getMessageByID(messageId);

        if (existedMessage == null) {
            context.status(400);
            return;
        }
        Message updateMessageReturned = messageService.updMessage(existedMessage, newText);
        context.status(200).json(updateMessageReturned);
    }
    private void getAccountIdHandler(Context context){
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        //Account account = accountService.ge
        List<Message> allmessages = messageService.allMessages(accountId);
        if(allmessages == null){
            context.status(200);
            context.json("");
        } else{
            context.status(200).json(allmessages);
        }
    }
}