package com.wisdombud.mongodb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class GridFsTool {
	private Mongo mongo = null;
	private DB db = null;
	private GridFS gfs = null;

	public GridFsTool() throws UnknownHostException {
		this("localhost");
	}

	public GridFsTool(String mongoIp) throws UnknownHostException {
		this(mongoIp, 27017);
	}

	@SuppressWarnings("deprecation")
	public GridFsTool(String mongoIp, int mongoPort) throws UnknownHostException {
		this.mongo = new Mongo(mongoIp, mongoPort);
		this.db = mongo.getDB("wisdombudfile");
		this.gfs = new GridFS(this.db, "fs");
	}

	public List<String> getAllNames() {
		List<String> ret = new ArrayList<String>();
		DBCursor dbc = this.gfs.getFileList();
		for (DBObject dbo : dbc) {
			ret.add(dbo.get("filename").toString());
		}
		return ret;
	}

	public Map<String, String> getAllIdNames() {
		Map<String, String> ret = new HashMap<String, String>();
		DBCursor dbc = this.gfs.getFileList();
		for (DBObject dbo : dbc) {
			ret.put(dbo.get("_id").toString(), dbo.get("filename").toString());
		}
		return ret;
	}

	public void deleteAll() {
		Map<String, String> ret = this.getAllIdNames();
		for (String inst : ret.keySet()) {
			this.gfs.remove(new ObjectId(inst));
		}
	}

	public void deleteFileByName(String filename) {
		this.gfs.remove(filename);
	}

	public void deleteFileById(String id) {
		ObjectId oid = new ObjectId(id);
		this.gfs.remove(oid);
	}

	public boolean downloadFileByName(String filename) throws IOException {
		return this.downloadFileByName(filename, filename);
	}

	public boolean downloadFileByName(String filename, OutputStream os) throws IOException {
		GridFSDBFile file = this.gfs.findOne(filename);
		if (file == null) {
			return false;
		}
		file.writeTo(os);
		return true;
	}

	public boolean downloadFileById(String id, OutputStream os) throws IOException {
		ObjectId oid = new ObjectId(id);
		GridFSDBFile file = this.gfs.findOne(oid);
		if (file == null) {
			return false;
		}
		file.writeTo(os);
		return true;
	}

	public boolean downloadFileByName(String filename, String newFilePath) throws IOException {
		GridFSDBFile file = this.gfs.findOne(filename);
		if (file == null) {
			return false;
		}
		file.writeTo(newFilePath);
		return true;
	}

	public boolean downloadFileById(String id, String newFilePath) throws IOException {
		ObjectId oid = new ObjectId(id);
		GridFSDBFile file = this.gfs.findOne(oid);
		if (file == null) {
			return false;
		}
		file.writeTo(newFilePath);
		return true;
	}

	public String uploadFile(InputStream is, String filename) {
		GridFSInputFile gfsFile = this.gfs.createFile(is);
		gfsFile.setFilename(filename);
		gfsFile.save();
		return gfsFile.getId().toString();
	}

	public String uploadFile(String filePath, String filename) throws IOException {
		File file = new File(filePath);
		GridFSInputFile gfsFile = this.gfs.createFile(file);
		gfsFile.setFilename(filename);
		gfsFile.save();
		return gfsFile.getId().toString();
	}

	public String uploadFile(String filePath) throws IOException {
		File file = new File(filePath);
		return this.uploadFile(filePath, file.getName());
	}

}
