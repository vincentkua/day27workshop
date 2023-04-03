package nus.iss.day27workshop.repository;

import java.sql.Timestamp;
import java.util.Date;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import nus.iss.day27workshop.model.NewReview;

@Repository
public class ReviewRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public Boolean insertReview(NewReview newReview) {
        Query query = Query.query(Criteria.where("gid").is(newReview.getGameid()));
        Document game = mongoTemplate.findOne(query, Document.class, "game");

        if (game != null) {
            Document toinsert = new Document();
            toinsert.put("user", newReview.getName());
            toinsert.put("rating", newReview.getRating());
            toinsert.put("comment", newReview.getComment());
            toinsert.put("ID", newReview.getGameid());
            toinsert.put("posted", new Date());
            toinsert.put("name", game.getString("name"));
            Document newDoc = mongoTemplate.insert(toinsert, "reviews");
            System.out.println("New Review Inserted: " + newDoc);

            return true;

        } else {
            System.out.println("Game ID Not Found.....");
            return false;
        }

    }

    public Boolean updateReview(String reviewid, Document toupdate) {

        String newcomment = toupdate.getString("comment");
        Integer newrating = toupdate.getInteger("rating");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Document editrecord = new Document();
        editrecord.put("comment", newcomment);
        editrecord.put("rating", newrating);
        editrecord.put("posted", timestamp);

        // Check if comment exist
        Query query = Query.query(Criteria.where("_id").is(reviewid));
        Document review = mongoTemplate.findOne(query, Document.class, "reviews");

        if (review != null) {
            Update updateOps = new Update()
                    .set("comment", newcomment)
                    .set("rating", newrating)
                    .push("edited", editrecord);

            UpdateResult updateResult = mongoTemplate.updateFirst(query, updateOps, Document.class, "reviews");

            if (updateResult.getModifiedCount() > 0) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    public Document getReview(String reviewid) {
        Query query = Query.query(Criteria.where("_id").is(reviewid));
        Document review = mongoTemplate.findOne(query, Document.class, "reviews");
        return review;
    }

    public Boolean deleteReview(String reviewid) {
        Query query = Query.query(Criteria.where("_id").is(reviewid));
        DeleteResult result = mongoTemplate.remove(query, "reviews");
        if (result.getDeletedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

}
