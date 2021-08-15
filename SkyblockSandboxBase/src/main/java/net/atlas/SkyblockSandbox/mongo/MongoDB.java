package net.atlas.SkyblockSandbox.mongo;

import org.bson.Document;

import java.util.List;
import java.util.UUID;

public interface MongoDB
{
	boolean isConnected();

	void connect();

	void setData(UUID uuid, String key, Object value);

	Object getData(UUID uuid, String key);

	boolean remove(UUID uuid);

	List<Document> getAllDocuments();
}
