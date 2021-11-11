package io.temp.file.storage.store.Services;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.IOException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.temp.file.storage.store.model.LoadFile;

@Service
public class FileService {
    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private MongoOperations mongoOperations;

    /**
     * ? I tried to implement deletion by Date expiration mechanism but failed.
     * ? It was commented out instead of deletion as it might get implemented in the
     * ? future
     */

    public String addFile(MultipartFile upload, int downloadLimit/* Date expiration */) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("filesize", upload.getSize());
        metadata.put("DLimit", downloadLimit);
        // metadata.put("expiration", expiration);

        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(),
                metadata);

        return fileID.toString();
    }

    // public void deleteExpiredUploads() {

    // FindIterable<Document> fileList =
    // mongoOperations.getCollection("fs.files").find(regex("_id", ".*"));
    // MongoCursor<Document> run = fileList.iterator();

    // while (run.hasNext())
    // if (run.next().getDate("uploadDate")
    // .compareTo(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
    // <= 0) {
    // System.out.println();
    // ObjectId id = run.next().getObjectId("_id");
    // mongoOperations.getCollection("fs.files").deleteOne(eq("_id", id));
    // mongoOperations.getCollection("fs.chunks").deleteMany(eq("files_id", id));
    // }
    // }

    private void deleteFile(String id, int limit) {

        if (limit > -1) {

            if (limit > 0) {
                mongoOperations.getCollection("fs.files").findOneAndUpdate(eq("_id", new ObjectId(id)),
                        set("metadata.DLimit", (limit - 1)));
            }

            else {
                mongoOperations.getCollection("fs.files").deleteOne(eq("_id", new ObjectId(id)));
                mongoOperations.getCollection("fs.chunks").deleteMany(eq("files_id", new ObjectId(id)));
            }
        }
    }

    public LoadFile downloadFile(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        LoadFile loadFile = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {

            loadFile.setFileName(gridFSFile.getFilename());

            loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());

            loadFile.setFileSize(gridFSFile.getMetadata().get("filesize").toString());

            loadFile.setFile(IOUtils.toByteArray(gridFsOperations.getResource(gridFSFile).getInputStream()));

            deleteFile(id, Integer.valueOf(gridFSFile.getMetadata().get("DLimit").toString()));

        }
        return loadFile;
    }
}
