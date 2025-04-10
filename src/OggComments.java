package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.Info;

class OggComments {
	State state = null;

	File file = null;

	Properties comments = new Properties();

	Properties info = new Properties();

	public OggComments(String path) {
		super();
		file = new File(path);
		state = new State();
		try {
			this.read(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		comments = state.vc.getCommentsAsProperties();

		info = state.vi.getInfoAsProperties();
		info.put("file_size", String.valueOf(file.length()));
		info.put("serial_number", String.valueOf(state.serial));
		info.put("vendor", state.vc.getVendor());
	}

	public void pack() {
		this.state.vc.clear();
		Enumeration en = this.comments.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			this.state.vc.add_tag(key, comments.getProperty(key));
		}

		File tmp = new File(this.file.getPath() + ".tmp");
		try {
			OutputStream out = new FileOutputStream(tmp);
			this.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tmp.renameTo(this.file);
	}

	private static int CHUNKSIZE = 4096;

	public OggComments(State state) {
		super();
		this.state = state;
	}

	private void read(InputStream in) {
		state.in = in;

		Page og = new Page();

		int index;
		byte[] buffer;
		int bytes = 0;

		state.oy = new SyncState();
		state.oy.init();

		index = state.oy.buffer(CHUNKSIZE);
		buffer = state.oy.data;
		try {
			bytes = state.in.read(buffer, index, CHUNKSIZE);
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
		state.oy.wrote(bytes);

		if (state.oy.pageout(og) != 1) {
			if (bytes < CHUNKSIZE) {
				System.err.println("Input truncated or empty.");
			} else {
				System.err.println("Input is not an Ogg bitstream.");
			}
			// goto err;
			return;
		}
		state.serial = og.serialno();
		state.os = new StreamState();
		state.os.init(state.serial);
		// os.reset();

		state.vi = new Info();
		state.vi.init();

		state.vc = new Comment();
		state.vc.init();

		if (state.os.pagein(og) < 0) {
			System.err.println("Error reading first page of Ogg bitstream data.");
			// goto err
			return;
		}

		Packet header_main = new Packet();

		if (state.os.packetout(header_main) != 1) {
			System.err.println("Error reading initial header packet.");
			// goto err
			return;
		}

		if (state.vi.synthesis_headerin(state.vc, header_main) < 0) {
			System.err.println("This Ogg bitstream does not contain Vorbis data.");
			// goto err
			return;
		}

		state.mainlen = header_main.bytes;
		state.mainbuf = new byte[state.mainlen];
		System.arraycopy(header_main.packet_base, header_main.packet,
				state.mainbuf, 0, state.mainlen);

		int i = 0;
		Packet header;
		Packet header_comments = new Packet();
		Packet header_codebooks = new Packet();

		header = header_comments;
		while (i < 2) {
			while (i < 2) {
				int result = state.oy.pageout(og);
				if (result == 0)
					break; /* Too little data so far */
				else if (result == 1) {
					state.os.pagein(og);
					while (i < 2) {
						result = state.os.packetout(header);
						if (result == 0)
							break;
						if (result == -1) {
							System.out.println("Corrupt secondary header.");
							// goto err;
							return;
						}
						state.vi.synthesis_headerin(state.vc, header);
						if (i == 1) {
							state.booklen = header.bytes;
							state.bookbuf = new byte[state.booklen];
							System.arraycopy(header.packet_base, header.packet,
									state.bookbuf, 0, header.bytes);
						}
						i++;
						header = header_codebooks;
					}
				}
			}

			index = state.oy.buffer(CHUNKSIZE);
			buffer = state.oy.data;
			try {
				bytes = state.in.read(buffer, index, CHUNKSIZE);
			} catch (Exception e) {
				System.err.println(e);
				return;
			}

			if (bytes == 0 && i < 2) {
				System.out.println("EOF before end of vorbis headers.");
				// goto err;
				return;
			}
			state.oy.wrote(bytes);
		}

		// System.out.println(state.vi);
	}

	int write(OutputStream out) {
		StreamState streamout = new StreamState();
		Packet header_main = new Packet();
		Packet header_comments = new Packet();
		Packet header_codebooks = new Packet();

		Page ogout = new Page();
		
		Packet op = new Packet();
		long granpos = 0;

		int result;

		int index;
		byte[] buffer;

		int bytes, eosin = 0;
		int needflush = 0, needout = 0;

		header_main.bytes = state.mainlen;
		header_main.packet_base = state.mainbuf;
		header_main.packet = 0;
		header_main.b_o_s = 1;
		header_main.e_o_s = 0;
		header_main.granulepos = 0;

		header_codebooks.bytes = state.booklen;
		header_codebooks.packet_base = state.bookbuf;
		header_codebooks.packet = 0;
		header_codebooks.b_o_s = 0;
		header_codebooks.e_o_s = 0;
		header_codebooks.granulepos = 0;

		streamout.init(state.serial);

		state.vc.header_out(header_comments);

		streamout.packetin(header_main);
		streamout.packetin(header_comments);
		streamout.packetin(header_codebooks);

		//System.out.println("%1");

		while ((result = streamout.flush(ogout)) != 0) {
			try {
				out.write(ogout.header_base, ogout.header, ogout.header_len);
				out.flush();
				/*System.out.println("ogout.header_base.length="+ogout.header_base.length+
						", ogout.header="+ogout.header+
						", ogout.header_len="+ogout.header_len);*/
			} catch (Exception e) {
				// goto cleanup;
				break;
			}
			try {
				out.write(ogout.body_base, ogout.body, ogout.body_len);
				out.flush();
				/*System.out.println("ogout.body_base.length="+ogout.body_base.length+
						", ogout.body="+ogout.body+
						", ogout.body_len="+ogout.body_len);*/
			} catch (Exception e) {
				// goto cleanup;
				break;
			}
		}

		//System.out.println("%2");

		while (state.fetch_next_packet(op) != 0) {
			int size = state.blocksize(op);
			granpos += size;
			// System.out.println("#1");
			if (needflush != 0) {
				// System.out.println("##1");
				if (streamout.flush(ogout) != 0) {
					try {
						out.write(ogout.header_base, ogout.header, ogout.header_len);
						out.flush();
						/*System.out.println("ogout.header_base.length="+ogout.header_base.length+
								", ogout.header="+ogout.header+
								", ogout.header_len="+ogout.header_len);*/
					} catch (Exception e) {
						e.printStackTrace();
						// goto cleanup;
						return -1;
					}
					try {
						out.write(ogout.body_base, ogout.body, ogout.body_len);
						out.flush();
						/*System.out.println("ogout.body_base.length="+ogout.body_base.length+
								", ogout.body="+ogout.body+
								", ogout.body_len="+ogout.body_len);*/
					} catch (Exception e) {
						e.printStackTrace();
						// System.out.println("ogout.body_base.length="+ogout.body_base.length+
						// ", ogout.body="+ogout.body+
						// ", ogout.body_len="+ogout.body_len);
						// goto cleanup;
						return -1;
					}
				}
			}
			// System.out.println("%2 eosin="+eosin);
			else if (needout != 0) {
				// System.out.println("##2");
				if (streamout.pageout(ogout) != 0) {
					try {
						out.write(ogout.header_base, ogout.header, ogout.header_len);
						out.flush();
						/*System.out.println("ogout.header_base.length="+ogout.header_base.length+
								", ogout.header="+ogout.header+
								", ogout.header_len="+ogout.header_len);*/
					} catch (Exception e) {
						e.printStackTrace();
						// goto cleanup;
						return -1;
					}
					try {
						out.write(ogout.body_base, ogout.body, ogout.body_len);
						out.flush();
						/*System.out.println("ogout.body_base.length="+ogout.body_base.length+
								", ogout.body="+ogout.body+
								", ogout.body_len="+ogout.body_len);*/
					} catch (Exception e) {
						e.printStackTrace();
						// System.out.println("ogout.body_base.length="+ogout.body_base.length+
						// ", ogout.body="+ogout.body+
						// ", ogout.body_len="+ogout.body_len);
						// goto cleanup;
						return -1;
					}
				}
			}

			// System.out.println("#2");

			needflush = needout = 0;

			if (op.granulepos == -1) {
				op.granulepos = granpos;
				streamout.packetin(op);
			} else {
				if (granpos > op.granulepos) {
					granpos = op.granulepos;
					streamout.packetin(op);
					needflush = 1;
				} else {
					streamout.packetin(op);
					needout = 1;
				}
			}
			// System.out.println("#3");
		}

		//System.out.println("%3");

		streamout.e_o_s = 1;
		while (streamout.flush(ogout) != 0) {
			try {
				out.write(ogout.header_base, ogout.header, ogout.header_len);
				out.flush();
				/*System.out.println("ogout.header_base.length="+ogout.header_base.length+
						", ogout.header="+ogout.header+
						", ogout.header_len="+ogout.header_len);*/
			} catch (Exception e) {
				e.printStackTrace();
				// goto cleanup;
				return -1;
			}
			try {
				out.write(ogout.body_base, ogout.body, ogout.body_len);
				out.flush();
				/*System.out.println("ogout.body_base.length="+ogout.body_base.length+
						", ogout.body="+ogout.body+
						", ogout.body_len="+ogout.body_len);*/
			} catch (Exception e) {
				e.printStackTrace();
				// System.out.println("ogout.body_base.length="+ogout.body_base.length+
				// ", ogout.body="+ogout.body+
				// ", ogout.body_len="+ogout.body_len);
				// goto cleanup;
				return -1;
			}
		}

		//System.out.println("%4");

		state.vi.clear();
		// System.out.println("%3 eosin="+eosin);

		//System.out.println("%5");

		eosin = 0; /* clear it, because not all paths to here do */
		while (eosin == 0) { /* We reached eos, not eof */
			/*
			 * We copy the rest of the stream (other logical streams) through, a
			 * page at a time.
			 */
			while (true) {
				result = state.oy.pageout(ogout);
				// System.out.println(" result4="+result);
				if (result == 0)
					break;
				if (result < 0) {
					System.out.println("Corrupt or missing data, continuing...");
				} else {
					/*
					 * Don't bother going through the rest, we can just write
					 * the page out now
					 */
					try {
						out.write(ogout.header_base, ogout.header, ogout.header_len);
						out.flush();
						/*System.out.println("ogout.header_base.length="+ogout.header_base.length+
								", ogout.header="+ogout.header+
								", ogout.header_len="+ogout.header_len);*/
					} catch (Exception e) {
						// goto cleanup;
						return -1;
					}
					try {
						out.write(ogout.body_base, ogout.body, ogout.body_len);
						out.flush();
						/*System.out.println("ogout.body_base.length="+ogout.body_base.length+
								", ogout.body="+ogout.body+
								", ogout.body_len="+ogout.body_len);*/
					} catch (Exception e) {
						// goto cleanup;
						return -1;
					}
				}
			}

			index = state.oy.buffer(CHUNKSIZE);
			buffer = state.oy.data;
			try {
				bytes = state.in.read(buffer, index, CHUNKSIZE);
			} catch (Exception e) {
				System.err.println(e);
				return -1;
			}
			//System.out.println("bytes="+bytes);
			state.oy.wrote(bytes);

			if (bytes == 0 || bytes == -1) {
				eosin = 1;
				break;
			}
		}

		/*
		 * cleanup: ogg_stream_clear(&streamout);
		 * ogg_packet_clear(&header_comments);
		 * 
		 * free(state->mainbuf); free(state->bookbuf);
		 * 
		 * jorbiscomment_clear_internals(state); if(!eosin) { state->lasterror =
		 * "Error writing stream to output. " "Output stream may be corrupted or
		 * truncated."; return -1; }
		 * 
		 * return 0; }
		 */
		return 0;
	}

	public void setComments(Properties comments) {
		this.comments = comments;
	}
}
