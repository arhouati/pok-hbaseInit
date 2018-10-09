package pfe.extraction.saveBigData;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import pfe.extraction.utils.Util;

public class SaveFacebookData {

	public static HBaseAdmin admin;
	public static Configuration config;
	public static String SERVER_IP = "10.42.7.102";
	public static String SERVER_PORT = "6000";
	public static String CLIENT_PORT = "2181";
	public static String DB_NAME = "pfe";
	public static String COLUMN_FAMILY_POST = "post";
	public static String COLUMN_FAMILY_COMMENT = "comment";
	public static String COLUMN_POST_ID = "Post_Id";
	public static String COLUMN_POST_CONTENT = "Content";
	public static String COLUMN_COMMENT_CONTENT = "Content";

	public void savePostComments(String postContent, List<String> comments)
			throws MasterNotRunningException, ZooKeeperConnectionException,
			IOException {
		config = Util.getConfiguration(SERVER_IP, SERVER_PORT, CLIENT_PORT);
		HBaseAdmin admin = Util.connectToHbase(config);

		@SuppressWarnings("resource")
		HTable table = new HTable(config, DB_NAME);
		Scan scan = new Scan();
		String row = "row";
		String a = "id";
		Integer s = 0;
		ResultScanner scanner = table.getScanner(scan);
		for (Result result = scanner.next(); result != null; result = scanner
				.next()) {

			s++;
		}

		int k = 1;
		Integer id = s + k;
		Put put = new Put(Bytes.toBytes((row + id.toString())));

		put.add(Bytes.toBytes(COLUMN_FAMILY_POST),
				Bytes.toBytes(COLUMN_POST_ID), Bytes.toBytes(a + id.toString()));
		put.add(Bytes.toBytes(COLUMN_FAMILY_POST),
				Bytes.toBytes(COLUMN_POST_CONTENT), Bytes.toBytes(postContent));
		table.put(put);
		if (comments != null) {
			for (String com : comments) {
				Put put2 = new Put(Bytes.toBytes((row + id.toString())));
				put2.add(Bytes.toBytes(COLUMN_FAMILY_COMMENT),
						Bytes.toBytes(COLUMN_POST_ID),
						Bytes.toBytes(a + id.toString()));
				put2.add(Bytes.toBytes(COLUMN_FAMILY_COMMENT),
						Bytes.toBytes(COLUMN_COMMENT_CONTENT),
						Bytes.toBytes(com));
				k++;
				id = s + k;
				table.put(put2);
			}
		}
	}

}