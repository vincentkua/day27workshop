package nus.iss.day27workshop.model;

public class NewReview {

    private String name;
    private Integer rating;
    private String comment;
    private Integer gameid;

    public NewReview() {
    }

    public NewReview(String name, Integer rating, String comment, Integer gameid) {
        this.name = name;
        this.rating = rating;
        this.comment = comment;
        this.gameid = gameid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getGameid() {
        return gameid;
    }

    public void setGameid(Integer gameid) {
        this.gameid = gameid;
    }

    @Override
    public String toString() {
        return "NewReview [name=" + name + ", rating=" + rating + ", comment=" + comment + ", gameid=" + gameid + "]";
    }

}
