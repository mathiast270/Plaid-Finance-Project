package com.example.FinanceDisplay;

import com.example.FinanceDisplay.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

public interface repository extends MongoRepository<User, ObjectId> {
    @Query("{auth_token : ?0}")
    User finduserByToken(String auth_token);
    @Query("{username : ?0}")
    User finduserByusername(String username);
}
