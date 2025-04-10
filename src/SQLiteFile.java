package src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

public class SQLiteFile {
	public final static long serialVersionUID = 0;

	private File file = null;

	public SQLiteFile(String path) {
		this.file = new File(path);
		try {
			this.copy(new File("data/tagcontainer.db"), this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return this.file;
	}

	public SQLiteFile(byte[] bytes) {
		this.file = deserialize(bytes);
	}

	// Copies src file to dst file.
	// If the dst file does not exist, it is created
	void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public String serialize() throws IOException {
		
		// Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutput out = new ObjectOutputStream(bos) ;
        out.writeObject(this.file);
        out.close();

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray();
        String string = "";
        for(int i=0; i<bytes.length; i++) {
        	string += bytes[i] + " ";
        }
        
        return string;
    }

	public File deserialize(byte[] bytes) {
		File file = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
			file = (File) in.readObject();
			in.close();
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
		return file;
	}

	public void setContent(Properties content) throws SQLException,
	ClassNotFoundException {
		// Driver to Use
		// http://www.zentus.com/sqlitejdbc/index.html
		Class.forName("org.sqlite.JDBC");

		// Create Connection Object to SQLite Database
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"
				+ this.file.getPath());

		// Prepare a statement to insert a record
		String sql = "INSERT INTO comments (key, value) VALUES(?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		Enumeration en = content.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			// Set the value
			pstmt.setString(1, key);
			pstmt.setString(2, content.getProperty(key));

			// Insert the row
			pstmt.executeUpdate();
		}
		conn.close();
	}

	public Properties getContent() throws SQLException, ClassNotFoundException {
		Properties content = new Properties();

		// Create Connection Object to SQLite Database
		Connection conn = DriverManager.getConnection("jdbc:sqlite:"
				+ this.file.getPath());

		// Create a Statement object for the database connection, dunno what
		// this stuff does though.
		Statement stmt = conn.createStatement();

		// Create a result set object for the statement
		ResultSet rs = stmt
		.executeQuery("SELECT key,value FROM comments ORDER BY key ASC");

		// Iterate the result set, printing each column
		while (rs.next()) {
			String key = rs.getString("key");
			String value = rs.getString("value");
			content.put(key, value);
		}

		// Close the connection
		conn.close();

		return content;
	}

}
