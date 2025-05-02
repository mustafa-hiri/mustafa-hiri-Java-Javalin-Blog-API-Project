package Controller;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;

public class SocialMediaController {

    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Defining all the routes this app will respond to
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

    // Handle user registration requests
    private void postRegisterHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        Account createdAccount = accountService.register(account);
        if (createdAccount == null) {
            context.status(400); // Something was wrong with input or user already exists
        } else {
            context.status(200).json(createdAccount);
        }
    }

    // Handle login attempts
    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginRequest = mapper.readValue(context.body(), Account.class);

        Account foundAccount = accountService.login(loginRequest);
        if (foundAccount == null) {
            context.status(401); // Unauthorized
        } else {
            context.status(200).json(foundAccount);
        }
    }

    // Create a new message
    private void postMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        Message createdMessage = messageService.registerMessage(message);
        if (createdMessage == null) {
            context.status(400);
        } else {
            context.status(200).json(createdMessage);
        }
    }

    // Get all messages
    private void getMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }

    // Get a single message by ID
    private void getMessagesIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(messageId);

        if (message == null) {
            context.status(200).json(""); // Returning empty response if not found
        } else {
            context.status(200).json(message);
        }
    }

    // Delete a message by ID
    private void deleteMessagesIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deleted = messageService.delMessageById(messageId);

        context.status(200).json(deleted != null ? deleted : "");
    }

    // Update a message's text
    private void patchMessagesIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode body = mapper.readTree(context.body());

        if (!body.has("message_text")) {
            context.status(400);
            return;
        }

        String newText = body.get("message_text").asText();
        int messageId = Integer.parseInt(context.pathParam("message_id"));

        Message updated = messageService.updateMessage(messageId, newText);

        if (updated == null) {
            context.status(400);
        } else {
            context.status(200).json(updated);
        }
    }

    // Get all messages posted by a specific account
    private void getAccountIdHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.allMessages(accountId);
        context.status(200).json(messages != null ? messages : "");
    }
}
