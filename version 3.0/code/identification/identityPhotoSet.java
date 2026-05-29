import java.util.ArrayList;
import java.util.List;

public class identityPhotoSet {

    private String photoSetId;
    private String frontPhoto;
    private String backPhoto;

    public identityPhotoSet(String photoSetId) {
        this.photoSetId = photoSetId;
    }

    public void captureIdentityPhoto(String side, String photo) {

        if(side.equalsIgnoreCase("front")) {
            frontPhoto = photo;
        }

        if(side.equalsIgnoreCase("back")) {
            backPhoto = photo;
        }
    }

    public List<String> getPhotos(){

        List<String> photos = new ArrayList<>();

        photos.add(frontPhoto);
        photos.add(backPhoto);

        return photos;
    }

    public boolean isComplete(){

        return frontPhoto != null
                && backPhoto != null;
    }

    public String getFrontPhoto() {
        return frontPhoto;
    }

    public String getBackPhoto() {
        return backPhoto;
    }
}