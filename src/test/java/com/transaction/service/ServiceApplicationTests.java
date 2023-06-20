package com.transaction.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import static com.mongodb.client.model.Filters.*;
import com.transaction.service.database.DBClient;
import com.transaction.service.enums.DebitCredit;
import com.transaction.service.enums.db.Collection;
import com.transaction.service.managers.TransactionManager;
import com.transaction.service.models.Amount;
import com.transaction.service.models.requests.LoadRequest;
import com.transaction.service.models.responses.LoadResponse;


@SpringBootTest
class ServiceApplicationTests {

	@Autowired
	private TransactionManager transactionManager;
	
	@MockBean
	private DBClient db;

	@MockBean
	private MongoCollection collection;

	@Test
	public void addFunds() {
		String messageId = "50e70c62-e480-49fc-bc1b-e991ac672178";
		String userId = "8786e2f9-d472-46a8-958f-d659880e723d";
		String currency = "USD";
		String amount = "1.0";
		DebitCredit transactionType = DebitCredit.DEBIT;
		LoadRequest request = new LoadRequest(
			userId,
			messageId,
			new Amount(amount, currency, transactionType));

		// mongo mock set up
		Mockito.when(db.getCollection(Collection.ACCOUNTS))
			.thenReturn(collection);

		Document transactionDoc = new Document()
			.append("_id", new ObjectId())
			.append("messageId", messageId)    
			.append("currency", request.getTransactionAmount().getCurrency())
			.append("amount", request.getTransactionAmount().getAmount())
			.append("debitOrCredit", transactionType);
		List<Document> subdocuments = new ArrayList<>();
        subdocuments.add(transactionDoc);

		Document accountDoc = new Document()
			.append("_id", new ObjectId())
			.append(currency, Double.valueOf(amount))
			.append("transactions", subdocuments);

		FindIterable<Document> findIterableMocked = (FindIterable<Document>) Mockito.mock(FindIterable.class);
		
		when(findIterableMocked.first()).thenReturn(null);
		
		Bson messageFilter = eq("transactions.messageId", messageId);
		Mockito.when(collection.find(messageFilter)).thenReturn(findIterableMocked);

		Mockito.when(collection.findOneAndUpdate(
			ArgumentMatchers.any(Bson.class), ArgumentMatchers.any(Bson.class), 
			ArgumentMatchers.any(FindOneAndUpdateOptions.class)))
			.thenReturn(accountDoc);
		// mock set up done
		
		LoadResponse response = transactionManager.processTransaction(request);

		assert(response.getUserId().equals(userId));
		assert(response.getMessageId().equals(messageId));
		assert(response.getBalance().getCurrency()).equals(currency);
		assert(response.getBalance().getAmount()).equals(amount);
		assert(response.getBalance().getDebitOrCredit()).equals(transactionType);
	}

	@Test
	public void removeFunds() {
		String messageId = "50e70c62-e480-49fc-bc1b-e991ac672178";
		String userId = "8786e2f9-d472-46a8-958f-d659880e723d";
		String currency = "USD";
		String amount = "1.0";
		DebitCredit transactionType = DebitCredit.CREDIT;
		LoadRequest request = new LoadRequest(
			userId,
			messageId,
			new Amount(amount, currency, transactionType));

		// mongo mock set up
		Mockito.when(db.getCollection(Collection.ACCOUNTS))
			.thenReturn(collection);

		Document transactionDoc = new Document()
			.append("_id", new ObjectId())
			.append("messageId", messageId)    
			.append("currency", request.getTransactionAmount().getCurrency())
			.append("amount", request.getTransactionAmount().getAmount())
			.append("debitOrCredit", transactionType);
		List<Document> subdocuments = new ArrayList<>();
        subdocuments.add(transactionDoc);

		Document accountDoc = new Document()
			.append("_id", new ObjectId())
			.append(currency, Double.valueOf(amount) - 1)
			.append("transactions", subdocuments);

		FindIterable<Document> findIterableMocked = (FindIterable<Document>) Mockito.mock(FindIterable.class);
		
		when(findIterableMocked.first()).thenReturn(null);
		
		Bson messageFilter = eq("transactions.messageId", messageId);
		Mockito.when(collection.find(messageFilter)).thenReturn(findIterableMocked);

		Mockito.when(collection.findOneAndUpdate(
			ArgumentMatchers.any(Bson.class), ArgumentMatchers.any(Bson.class), 
			ArgumentMatchers.any(FindOneAndUpdateOptions.class)))
			.thenReturn(accountDoc);
		// mock set up done
		
		LoadResponse response = transactionManager.processTransaction(request);

		assert(response.getUserId().equals(userId));
		assert(response.getMessageId().equals(messageId));
		assert(response.getBalance().getCurrency()).equals(currency);
		assert(response.getBalance().getAmount()).equals("0.0");
		assert(response.getBalance().getDebitOrCredit()).equals(transactionType);
	}
}
