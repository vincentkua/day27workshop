package nus.iss.day27workshop.controller;

import java.sql.Timestamp;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import nus.iss.day27workshop.model.NewReview;
import nus.iss.day27workshop.repository.ReviewRepository;

@RestController
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepository;

    @PostMapping(value = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> postReview(NewReview newReview) {
        Boolean binserted = reviewRepository.insertReview(newReview);
        if (binserted) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Review Inserted Successfully...");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Game Id Not Found... Unable to insert ... ");
        }
    }

    @PutMapping(value = "/review/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateReview(@RequestBody String jsonString, @PathVariable("id") String id) {
        Document toupdate = Document.parse(jsonString);
        Boolean bupdated = reviewRepository.updateReview(id, toupdate);
        if (bupdated) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Review Updated Successfully...");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Review Id Not Found... Unable to update ... ");
        }
    }

    @GetMapping(value = "/review/{reviewid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReview(@PathVariable("reviewid") String reviewid) {
        Document review = reviewRepository.getReview(reviewid);
        if (review != null) {
            Boolean beditted = false;
            if (review.get("edited") != null) {
                beditted = true;
            }

            long currentTimeMillis = System.currentTimeMillis();
            Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
            JsonObject payload = Json.createObjectBuilder()
                    .add("user", review.getString("user"))
                    .add("rating", review.getInteger("rating"))
                    .add("comment", review.getString("comment"))
                    .add("id", review.getInteger("ID"))
                    .add("posted", review.get("posted").toString())
                    .add("name", review.getString("name"))
                    .add("edited", beditted)
                    .add("timestamp", currentTimestamp.toString())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(payload.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review ID Not Found");
        }
    }

    @GetMapping(value = "/review/{reviewid}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReviewHistory(@PathVariable("reviewid") String reviewid) {
        Document review = reviewRepository.getReview(reviewid);
        if (review != null) {
            JsonArrayBuilder jsonarray = Json.createArrayBuilder();
            if (review.get("edited") != null) {
                List<Document> reviewlogs = review.getList("edited", Document.class);
                for (Document reviewlog : reviewlogs) {
                    JsonObject reviewjson = Json.createObjectBuilder()
                            .add("comment", reviewlog.getString("comment"))
                            .add("rating", reviewlog.getInteger("rating"))
                            .add("posted", reviewlog.get("posted").toString())
                            .build();
                    jsonarray.add(reviewjson);
                }
            }

            long currentTimeMillis = System.currentTimeMillis();
            Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
            JsonObject payload = Json.createObjectBuilder()
                    .add("user", review.getString("user"))
                    .add("rating", review.getInteger("rating"))
                    .add("comment", review.getString("comment"))
                    .add("id", review.getInteger("ID"))
                    .add("posted", review.get("posted").toString())
                    .add("name", review.getString("name"))
                    .add("edited", jsonarray)
                    .add("timestamp", currentTimestamp.toString())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(payload.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review ID Not Found");
        }
    }

    @DeleteMapping("/review/{reviewid}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewid") String reviewid) {
        Boolean bdeleted = reviewRepository.deleteReview(reviewid);
        if (bdeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Review Deleted...");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review Not found and unable to delete...");
        }
    }

}
