package src;

import java.io.InputStream;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.Info;

public class State {
	private static int CHUNKSIZE = 4096;

	SyncState oy;

	StreamState os;

	Comment vc;

	Info vi;

	InputStream in;

	int serial;

	byte[] mainbuf;

	byte[] bookbuf;

	int mainlen;

	int booklen;

	String lasterror;

	int prevW;

	int blocksize(Packet p) {
		int _this = vi.blocksize(p);
		int ret = (_this + prevW) / 4;

		if (prevW == 0) {
			prevW = _this;
			return 0;
		}

		prevW = _this;
		return ret;
	}

	Page og = new Page();

	int fetch_next_packet(Packet p) {
		int result;
		byte[] buffer;
		int index;
		int bytes;

		result = os.packetout(p);

		if (result > 0) {
			return 1;
		}

		while (oy.pageout(og) <= 0) {
			index = oy.buffer(CHUNKSIZE);
			buffer = oy.data;
			try {
				bytes = in.read(buffer, index, CHUNKSIZE);
			} catch (Exception e) {
				System.err.println(e);
				return 0;
			}
			if (bytes > 0)
				oy.wrote(bytes);
			if (bytes == 0 || bytes == -1) {
				return 0;
			}
		}
		os.pagein(og);

		return fetch_next_packet(p);
	}
}
