package src;

/**
 * 
 */
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

/**
 * @author tetri.mesquita
 * 
 */
public class Launcher {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Form thisClass = new Form();
		thisClass.createSShell();

		Program p = Program.findProgram(".ogg");
		if (p != null) {
			ImageData data = p.getImageData();
			if (data != null) {
				Image icon = new Image(display, data);
				thisClass.sShell.setImage(icon);
			}
		}

		FileDialog dialog = new FileDialog(thisClass.sShell, SWT.OPEN);
		dialog.setFilterNames(new String[] { "Arquivo de �udio Ogg Vorbis (*.ogg)" });
		dialog.setFilterExtensions(new String[] { "*.ogg" });
		String f = dialog.open();
		if (f == null)
			System.exit(1);
		try {
			thisClass.text.setText(f);
			thisClass.ogg = new OggComments(f);
			if (thisClass.ogg.comments.containsKey("__ID__")) {
				MessageBox mb = new MessageBox(thisClass.sShell, SWT.YES
						| SWT.NO | SWT.ICON_QUESTION);
				mb.setText("Recupera��o de informa��es");
				mb.setMessage("Este arquivo aparentemente cont�m informa��es esteganografadas em sua �rea de dados.\r\nDeseja tentar recuperar estas informa��es?");
				if (mb.open() == SWT.YES) {
					// TODO recuperar arquivo de banco de dados
					thisClass.sql = new SQLiteFile(thisClass.decodeString(thisClass.ogg.comments.getProperty("__DB__")));
					// TODO recuperar coment�rios (oog.comments) a partir do
					// arquivo de banco de dados
					System.out.println(thisClass.sql.getContent());
					thisClass.ogg.setComments(thisClass.sql.getContent());
				} else {
					thisClass.sql = new SQLiteFile(f.substring(0, f.length() - 3) + "db");
				}
			} else {
				thisClass.sql = new SQLiteFile(f.substring(0, f.length() - 3) + "db");
			}
			thisClass.showComments();
			thisClass.showFormatInfo();
			thisClass.sShell.open();
		} catch (SQLException se) {
			// TODO tratamento de exce��o SQL
		} catch (ClassNotFoundException cnf) {
			// TODO tratamento de exce��o ClassNotFound
		} catch (NullPointerException npe) {
			MessageBox mb = new MessageBox(thisClass.sShell, SWT.OK
					| SWT.ICON_ERROR);
			mb.setText("Erro de execu��o");
			mb.setMessage("Ocorreu o seguinte erro de execu��o:\r\n"
					+ npe.getMessage());
			mb.open();
			npe.printStackTrace();
			System.exit(1);
		}

		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}