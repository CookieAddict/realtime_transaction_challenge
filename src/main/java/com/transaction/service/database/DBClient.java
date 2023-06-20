package com.transaction.service.database;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.transaction.service.enums.db.Collection;

@Component

public class DBClient {

    private MongoClient client;

    private String dbName;

    public DBClient(@Value("${mongodb.uri}") String dbUri, @Value("${mongodb.database.name}") String dbName) {
        this.client = MongoClients.create(dbUri);
        this.dbName = dbName;
    }

    public MongoCollection<Document> getCollection(Collection name) {
        return this.client
                .getDatabase(this.dbName)
                .getCollection(name.toString());
    }

    public MongoClient getClient() {
        return this.client;
    }
  
}
