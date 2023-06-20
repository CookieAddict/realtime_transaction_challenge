package com.transaction.service.managers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.transaction.service.database.DBClient;
import com.transaction.service.enums.DebitCredit;
import com.transaction.service.enums.ResponseCode;
import com.transaction.service.enums.db.Collection;
import com.transaction.service.models.Amount;
import com.transaction.service.models.requests.LoadRequest;
import com.transaction.service.models.responses.AuthorizationResponse;
import com.transaction.service.models.responses.LoadResponse;

@Component
public class TransactionManager {

    @Autowired
    private DBClient db;

    private static Logger log = LogManager.getLogger(TransactionManager.class);

    public LoadResponse processTransaction(LoadRequest request) {
        String messageId = request.getMessageId();

        log.info("Processing transaction with message Id {}", messageId);
        
        double amount = Double.valueOf(request.getTransactionAmount().getAmount());
        String currency = request.getTransactionAmount().getCurrency();
        DebitCredit transactionType = request.getTransactionAmount().getDebitOrCredit();
        String userId = request.getUserId();

        // I want to avoid repeated processing of the same message
        // if the messageId already exists, no changes will occur
        Bson messageFilter = eq("transactions.messageId", messageId);
        FindIterable<Document> result = db.getCollection(Collection.ACCOUNTS).find(messageFilter);

        if (result.first() != null) {
            log.info("Message Id {} already processed, declining transaction", messageId);
            Document accountDoc = getAccount(userId);

            LoadResponse response = new AuthorizationResponse(
                userId, messageId, 
                new Amount(String.valueOf(accountDoc.get(currency)), currency, transactionType),
                ResponseCode.DECLINED);

            return response;
        }

        Bson filter = eq("userId", userId);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(true);

        if (transactionType == DebitCredit.CREDIT) {
            filter = and(eq("userId", userId), gte(currency, amount));
            options.upsert(false);
        }

        Document transactionDoc = new Document()
        .append("_id", new ObjectId())
        .append("messageId", messageId)    
        .append("currency", request.getTransactionAmount().getCurrency())
        .append("amount", request.getTransactionAmount().getAmount())
        .append("debitOrCredit", transactionType);
        
        int direction = transactionType == DebitCredit.DEBIT ? 1 : -1;
        Bson update = combine(inc(currency, amount * direction), addToSet("transactions", transactionDoc));
        
        Document accountDoc = db.getCollection(Collection.ACCOUNTS).findOneAndUpdate(filter, update, options);
        ResponseCode responseCode = ResponseCode.APPROVED;

        if (transactionType == DebitCredit.CREDIT && accountDoc == null) {
            log.info("Not enough funds available to perform credit, declining transaction");
            accountDoc = getAccount(userId);
            responseCode = ResponseCode.DECLINED;
        }

        LoadResponse response = new AuthorizationResponse(
            userId, messageId, 
            new Amount(String.valueOf(accountDoc.get(currency)), currency, transactionType),
            responseCode);


        log.info("Finished processing transaction with message Id {}", messageId);
        return response;
    }

    private Document getAccount(String userId) {
        Bson filter = eq("userId", userId);
        FindIterable<Document> result = db.getCollection(Collection.ACCOUNTS).find(filter);
        return result.first();
    }
}
