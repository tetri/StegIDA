package src;

/**
 * 
 */

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author tetri.mesquita
 * 
 */
public class Form {
	protected OggComments ogg = null;

	protected SQLiteFile sql = null;

	protected Shell sShell = null;

	protected Text text = null;

	private Composite compositeBasicInfo = null;

	private Composite compositeAdvanced = null;

	private Composite compositeCtrl = null;

	private Link linkSourceForge = null;

	private Button buttonUpdate = null;

	private Button buttonCancel = null;

	private Group groupMetadata = null;

	private Group groupFormatInfo = null;

	private Label labelTrack = null;

	private Text textTrack = null;

	private Label labelDisc = null;

	private Text textDisc = null;

	private Label labelBPM = null;

	private Text textBPM = null;

	private Label labelTitle = null;

	private Text textTitle = null;

	private Label labelArtist = null;

	private Text textArtist = null;

	private Label labelAlbum = null;

	private Text textAlbum = null;

	private Label labelYear = null;

	private Text textYear = null;

	private Label labelGenre = null;

	private CCombo cComboGenre = null;

	private Label labelComment = null;

	private Text textComment = null;

	private Label labelComposer = null;

	private Text textComposer = null;

	private Label labelPublisher = null;

	private Text textPublisher = null;

	private Label labelLength = null;

	private Label labelAvgBitrate = null;

	private Label labelFileSize = null;

	private Label labelNomBitrate = null;

	private Label labelMaxBitrate = null;

	private Label labelChannels = null;

	private Label labelSampeRate = null;

	private Label labelSerialNumber = null;

	private Label labelVersion = null;

	private Label labelVendor = null;

	private Group groupAdvanced = null;

	private Table tableAdvanced = null;

	private Composite compositeAdvancedForm = null;

	private Label labelAdvancedTag = null;

	private Label labelAdvancedValue = null;

	private Text textAdvancedValue = null;

	private Button buttonNewTag = null;

	private Button buttonDeleteTag = null;

	private Button buttonDeleteAll = null;

	private CCombo cComboTag = null;

	private boolean isClosing = false;

	private TabFolder tabFolder = null;

	/**
	 * This method initializes sShell
	 */
	protected void createSShell() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		sShell = new Shell(SWT.TITLE | SWT.CLOSE);
		sShell.setLayout(gridLayout1);
		sShell.setSize(new Point(555, 404));
		sShell.setText("Informações do Arquivo");
		sShell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				if (!isClosing) {
					e.doit = doExit();
				}
			}
		});
		text = new Text(sShell, SWT.BORDER | SWT.READ_ONLY);
		text.setLayoutData(gridData);
		text.setEditable(false);
		createTabFolder();
		createCompositeCtrl();
	}

	/**
	 * @return the sShell
	 */
	protected Shell getSShell() {
		return sShell;
	}

	/**
	 * This method initializes compositeBasicInfo
	 * 
	 */
	private void createCompositeBasicInfo() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		compositeBasicInfo = new Composite(tabFolder, SWT.NONE);
		compositeBasicInfo.setLayout(gridLayout2);
		createGroupMetadata();
		createGroupFormatInfo();
	}

	/**
	 * This method initializes compositeAdvanced
	 * 
	 */
	private void createCompositeAdvanced() {
		compositeAdvanced = new Composite(tabFolder, SWT.NONE);
		compositeAdvanced.setLayout(new GridLayout());
		createGroupAdvanced();
	}

	/**
	 * This method initializes compositeCtrl
	 * 
	 */
	private void createCompositeCtrl() {
		GridData gridData39 = new org.eclipse.swt.layout.GridData();
		gridData39.widthHint = 72;
		gridData39.heightHint = 24;
		GridData gridData38 = new org.eclipse.swt.layout.GridData();
		gridData38.widthHint = 72;
		gridData38.heightHint = 24;
		GridData gridData3 = new GridData();
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		compositeCtrl = new Composite(sShell, SWT.NONE);
		compositeCtrl.setLayoutData(gridData2);
		compositeCtrl.setLayout(gridLayout);
		linkSourceForge = new Link(compositeCtrl, SWT.NONE);
		linkSourceForge
				.setText("StegIDA v.0.1a, <a>http://stegida.sourceforge.net/</a>");
		linkSourceForge.setLayoutData(gridData3);
		linkSourceForge
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						try {
							Desktop.getDesktop().browse(
									new URI("http://stegida.sourceforge.net/"));
						} catch (IOException e1) {
							// TODO tratamento da exceção IOException
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO tratamento da exceção URISyntaxException
							e1.printStackTrace();
						}
					}
				});
		buttonUpdate = new Button(compositeCtrl, SWT.NONE);
		buttonUpdate.setText("OK");
		buttonUpdate.setLayoutData(gridData38);
		buttonUpdate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int err = 0;
				try {
					// escrever comments no arquivo oggaudio.db (banco de dados)
					sql.setContent(ogg.comments);

					// recupera conteúdo da marca d'água e define como
					// comentários do arquivo ogg
					Properties wmark = new Properties();
					wmark.load(new FileInputStream("data/watermark.ini"));
					ogg.setComments(wmark);

					// serializar arquivo oggaudio.db
					String sqlite_bytes = sql.serialize();
					ogg.comments.put("__DB__", sqlite_bytes);
					
					// salvar arquivo oggaudio.file
					ogg.pack();

					// excluir arquivo oggaudio.db
					sql.getFile().delete();
					// salvar arquivo oggaudio.file
				} catch (FileNotFoundException fnf) {
					MessageBox mb = new MessageBox(sShell, SWT.OK
							| SWT.ICON_WARNING);
					mb.setText("Arquivo não encontrado");
					mb
							.setMessage("Não foi possível encontrar o arquivo de configuração.\r\n"
									+ fnf.getMessage());
					mb.open();
					err = 1;
				} catch (IOException ioe) {
					MessageBox mb = new MessageBox(sShell, SWT.OK
							| SWT.ICON_WARNING);
					mb.setText("Arquivo não encontrado");
					mb
							.setMessage("Não foi possível encontrar o arquivo de configuração.\r\n"
									+ ioe.getMessage());
					mb.open();
					err = 1;
				} catch (SQLException se) {
					MessageBox mb = new MessageBox(sShell, SWT.OK
							| SWT.ICON_WARNING);
					mb.setText("Erro de SQL");
					mb
							.setMessage("Não foi possível executar a instrução SQL.\r\n"
									+ se.getMessage());
					mb.open();
					err = 1;
				} catch (ClassNotFoundException cnf) {
					MessageBox mb = new MessageBox(sShell, SWT.OK
							| SWT.ICON_WARNING);
					mb.setText("Classe não encontrada");
					mb
							.setMessage("Não foi possível encontrar a classe especificada.\r\n"
									+ cnf.getMessage());
					mb.open();
					err = 1;
				}
				sShell.close();
				sShell.dispose();
				System.exit(err);
			}
		});
		sShell.setDefaultButton(buttonUpdate);
		buttonCancel = new Button(compositeCtrl, SWT.NONE);
		buttonCancel.setText("Cancelar");
		buttonCancel.setLayoutData(gridData39);
		buttonCancel
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					@Override
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						e.doit = doExit();
					}
				});
	}

	/**
	 * This method initializes groupMetadata
	 * 
	 */
	private void createGroupMetadata() {
		GridData gridData27 = new GridData();
		gridData27.grabExcessHorizontalSpace = true;
		gridData27.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData27.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData26 = new GridData();
		gridData26.grabExcessHorizontalSpace = true;
		gridData26.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData26.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData25 = new GridData();
		gridData25.grabExcessHorizontalSpace = true;
		gridData25.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData25.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData24 = new GridData();
		gridData24.grabExcessHorizontalSpace = true;
		gridData24.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData24.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData23 = new GridData();
		gridData23.grabExcessHorizontalSpace = true;
		gridData23.horizontalAlignment = GridData.FILL;
		GridData gridData22 = new GridData();
		gridData22.grabExcessHorizontalSpace = true;
		gridData22.verticalAlignment = GridData.BEGINNING;
		gridData22.horizontalAlignment = GridData.END;
		GridData gridData21 = new GridData();
		gridData21.grabExcessHorizontalSpace = true;
		gridData21.verticalAlignment = GridData.BEGINNING;
		gridData21.horizontalAlignment = GridData.END;
		GridData gridData20 = new GridData();
		gridData20.grabExcessHorizontalSpace = true;
		gridData20.verticalAlignment = GridData.BEGINNING;
		gridData20.horizontalAlignment = GridData.END;
		GridData gridData19 = new GridData();
		gridData19.grabExcessHorizontalSpace = true;
		gridData19.verticalAlignment = GridData.BEGINNING;
		gridData19.horizontalAlignment = GridData.END;
		GridData gridData18 = new GridData();
		gridData18.grabExcessHorizontalSpace = true;
		gridData18.horizontalAlignment = GridData.FILL;
		GridData gridData17 = new GridData();
		gridData17.grabExcessHorizontalSpace = true;
		gridData17.verticalAlignment = GridData.BEGINNING;
		gridData17.horizontalAlignment = GridData.END;
		GridData gridData16 = new GridData();
		gridData16.grabExcessHorizontalSpace = true;
		gridData16.horizontalAlignment = GridData.FILL;
		GridData gridData15 = new GridData();
		gridData15.grabExcessHorizontalSpace = true;
		gridData15.verticalAlignment = GridData.BEGINNING;
		gridData15.horizontalAlignment = GridData.END;
		GridData gridData14 = new GridData();
		gridData14.grabExcessHorizontalSpace = true;
		gridData14.verticalAlignment = GridData.BEGINNING;
		gridData14.horizontalAlignment = GridData.END;
		GridData gridData13 = new GridData();
		gridData13.horizontalAlignment = GridData.FILL;
		gridData13.grabExcessHorizontalSpace = true;
		GridData gridData12 = new GridData();
		gridData12.horizontalSpan = 5;
		gridData12.horizontalAlignment = GridData.FILL;
		gridData12.grabExcessHorizontalSpace = true;
		GridData gridData11 = new GridData();
		gridData11.horizontalSpan = 5;
		gridData11.horizontalAlignment = GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		GridData gridData10 = new GridData();
		gridData10.horizontalSpan = 5;
		gridData10.horizontalAlignment = GridData.FILL;
		gridData10.grabExcessVerticalSpace = true;
		gridData10.verticalAlignment = GridData.FILL;
		gridData10.grabExcessHorizontalSpace = true;
		GridData gridData9 = new GridData();
		gridData9.horizontalSpan = 3;
		gridData9.horizontalAlignment = GridData.FILL;
		gridData9.grabExcessHorizontalSpace = true;
		GridData gridData8 = new GridData();
		gridData8.horizontalSpan = 5;
		gridData8.horizontalAlignment = GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		GridData gridData7 = new GridData();
		gridData7.horizontalSpan = 5;
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.grabExcessHorizontalSpace = true;
		GridData gridData6 = new GridData();
		gridData6.horizontalSpan = 5;
		gridData6.horizontalAlignment = GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 6;
		GridData gridData4 = new GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.verticalAlignment = GridData.FILL;
		gridData4.grabExcessVerticalSpace = true;
		groupMetadata = new Group(compositeBasicInfo, SWT.NONE);
		groupMetadata.setLayoutData(gridData4);
		groupMetadata.setLayout(gridLayout3);
		groupMetadata.setText("Meta dados");
		labelTrack = new Label(groupMetadata, SWT.NONE);
		labelTrack.setText("Faixa #");
		labelTrack.setLayoutData(gridData14);
		textTrack = new Text(groupMetadata, SWT.BORDER);
		textTrack.setLayoutData(gridData13);
		textTrack.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (textTrack.getText().equals(""))
					ogg.comments.remove("TRACKNUMBER");
				else
					ogg.comments.put("TRACKNUMBER", textTrack.getText());
				showComments();
			}
		});
		labelDisc = new Label(groupMetadata, SWT.NONE);
		labelDisc.setText("Disco #");
		labelDisc.setLayoutData(gridData15);
		textDisc = new Text(groupMetadata, SWT.BORDER);
		textDisc.setLayoutData(gridData16);
		textDisc.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (textDisc.getText().equals(""))
					ogg.comments.remove("DISCNUMBER");
				else
					ogg.comments.put("DISCNUMBER", textDisc.getText());
				showComments();
			}
		});
		labelBPM = new Label(groupMetadata, SWT.NONE);
		labelBPM.setText("BPM");
		labelBPM.setLayoutData(gridData17);
		textBPM = new Text(groupMetadata, SWT.BORDER);
		textBPM.setLayoutData(gridData18);
		textBPM.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (textBPM.getText().equals(""))
					ogg.comments.remove("BPM");
				else
					ogg.comments.put("BPM", textBPM.getText());
				showComments();
			}
		});
		labelTitle = new Label(groupMetadata, SWT.NONE);
		labelTitle.setText("Título");
		labelTitle.setLayoutData(gridData19);
		textTitle = new Text(groupMetadata, SWT.BORDER);
		textTitle.setLayoutData(gridData6);
		textTitle.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (textTitle.getText().equals(""))
					ogg.comments.remove("TITLE");
				else
					ogg.comments.put("TITLE", textTitle.getText());
				showComments();
			}
		});
		labelArtist = new Label(groupMetadata, SWT.NONE);
		labelArtist.setText("Artista");
		labelArtist.setLayoutData(gridData20);
		textArtist = new Text(groupMetadata, SWT.BORDER);
		textArtist.setLayoutData(gridData7);
		textArtist.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (textArtist.getText().equals(""))
					ogg.comments.remove("ARTIST");
				else
					ogg.comments.put("ARTIST", textArtist.getText());
				showComments();
			}
		});
		labelAlbum = new Label(groupMetadata, SWT.NONE);
		labelAlbum.setText("Álbum");
		labelAlbum.setLayoutData(gridData21);
		textAlbum = new Text(groupMetadata, SWT.BORDER);
		textAlbum.setLayoutData(gridData8);
		textAlbum.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (textAlbum.getText().equals(""))
					ogg.comments.remove("ALBUM");
				else
					ogg.comments.put("ALBUM", textAlbum.getText());
				showComments();
			}
		});
		labelYear = new Label(groupMetadata, SWT.NONE);
		labelYear.setText("Ano");
		labelYear.setLayoutData(gridData22);
		textYear = new Text(groupMetadata, SWT.BORDER);
		textYear.setLayoutData(gridData23);
		textYear.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (textYear.getText().equals(""))
					ogg.comments.remove("DATE");
				else
					ogg.comments.put("DATE", textYear.getText());
				showComments();
			}
		});
		labelGenre = new Label(groupMetadata, SWT.NONE);
		labelGenre.setText("Gênero");
		labelGenre.setLayoutData(gridData24);
		cComboGenre = new CCombo(groupMetadata, SWT.BORDER);
		cComboGenre.setVisibleItemCount(14);
		cComboGenre.setLayoutData(gridData9);
		cComboGenre.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (cComboGenre.getText().equals(""))
					ogg.comments.remove("GENRE");
				else
					ogg.comments.put("GENRE", cComboGenre.getText());
				showComments();
			}
		});
		cComboGenre.setItems(getAllGenresSuggestions());
		labelComment = new Label(groupMetadata, SWT.NONE);
		labelComment.setText("Comentário");
		labelComment.setLayoutData(gridData25);
		textComment = new Text(groupMetadata, SWT.MULTI | SWT.WRAP
				| SWT.V_SCROLL | SWT.BORDER);
		textComment.setLayoutData(gridData10);
		textComment.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (textComment.getText().equals(""))
					ogg.comments.remove("COMMENT");
				else
					ogg.comments.put("COMMENT", textComment.getText());
				showComments();
			}
		});
		labelComposer = new Label(groupMetadata, SWT.NONE);
		labelComposer.setText("Compositor");
		labelComposer.setLayoutData(gridData26);
		textComposer = new Text(groupMetadata, SWT.BORDER);
		textComposer.setLayoutData(gridData11);
		textComposer
				.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
					public void focusLost(org.eclipse.swt.events.FocusEvent e) {
						if (textComposer.getText().equals(""))
							ogg.comments.remove("COMPOSER");
						else
							ogg.comments
									.put("COMPOSER", textComposer.getText());
						showComments();
					}
				});
		labelPublisher = new Label(groupMetadata, SWT.NONE);
		labelPublisher.setText("Editor");
		labelPublisher.setLayoutData(gridData27);
		textPublisher = new Text(groupMetadata, SWT.BORDER);
		textPublisher.setLayoutData(gridData12);
		textPublisher
				.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
					public void focusLost(org.eclipse.swt.events.FocusEvent e) {
						if (textPublisher.getText().equals(""))
							ogg.comments.remove("PUBLISHER");
						else
							ogg.comments.put("PUBLISHER", textPublisher
									.getText());
						showComments();
					}
				});
	}

	/**
	 * This method initializes groupFormatInfo
	 * 
	 */
	private void createGroupFormatInfo() {
		GridData gridData37 = new GridData();
		gridData37.widthHint = 128;
		GridData gridData36 = new GridData();
		gridData36.widthHint = 128;
		GridData gridData35 = new GridData();
		gridData35.widthHint = 128;
		GridData gridData34 = new GridData();
		gridData34.widthHint = 128;
		GridData gridData33 = new GridData();
		gridData33.widthHint = 128;
		GridData gridData32 = new GridData();
		gridData32.widthHint = 128;
		GridData gridData31 = new GridData();
		gridData31.widthHint = 128;
		GridData gridData30 = new GridData();
		gridData30.widthHint = 128;
		GridData gridData29 = new GridData();
		gridData29.widthHint = 128;
		GridData gridData28 = new GridData();
		gridData28.widthHint = 128;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.verticalSpacing = 2;
		GridData gridData5 = new GridData();
		gridData5.grabExcessHorizontalSpace = false;
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.verticalAlignment = GridData.FILL;
		gridData5.grabExcessVerticalSpace = true;
		groupFormatInfo = new Group(compositeBasicInfo, SWT.NONE);
		groupFormatInfo.setText("Informações do formato");
		groupFormatInfo.setLayout(gridLayout4);
		groupFormatInfo.setLayoutData(gridData5);
		labelLength = new Label(groupFormatInfo, SWT.WRAP);
		labelLength.setText("Duração: ");
		labelLength.setLayoutData(gridData37);
		labelAvgBitrate = new Label(groupFormatInfo, SWT.WRAP);
		labelAvgBitrate.setText("Bitrate médio: ");
		labelAvgBitrate.setLayoutData(gridData36);
		labelFileSize = new Label(groupFormatInfo, SWT.WRAP);
		labelFileSize.setText("Tamanho: ");
		labelFileSize.setLayoutData(gridData35);
		labelNomBitrate = new Label(groupFormatInfo, SWT.WRAP);
		labelNomBitrate.setText("Bitrate nominal: ");
		labelNomBitrate.setLayoutData(gridData34);
		labelMaxBitrate = new Label(groupFormatInfo, SWT.LEFT | SWT.WRAP);
		labelMaxBitrate.setText("Bitrate máximo: ");
		labelMaxBitrate.setLayoutData(gridData33);
		labelChannels = new Label(groupFormatInfo, SWT.WRAP);
		labelChannels.setText("Canais: ");
		labelChannels.setLayoutData(gridData32);
		labelSampeRate = new Label(groupFormatInfo, SWT.WRAP);
		labelSampeRate.setText("Amostragem: ");
		labelSampeRate.setLayoutData(gridData31);
		labelSerialNumber = new Label(groupFormatInfo, SWT.WRAP);
		labelSerialNumber.setText("Número serial: ");
		labelSerialNumber.setLayoutData(gridData30);
		labelVersion = new Label(groupFormatInfo, SWT.WRAP);
		labelVersion.setText("Versão: ");
		labelVersion.setLayoutData(gridData29);
		labelVendor = new Label(groupFormatInfo, SWT.WRAP);
		labelVendor.setText("Proprietário: ");
		labelVendor.setLayoutData(gridData28);
	}

	/**
	 * This method initializes groupAdvanced
	 * 
	 */
	private void createGroupAdvanced() {
		GridData gridData49 = new GridData();
		gridData49.grabExcessHorizontalSpace = true;
		gridData49.horizontalAlignment = GridData.FILL;
		gridData49.verticalAlignment = GridData.FILL;
		gridData49.grabExcessVerticalSpace = true;
		GridData gridData48 = new GridData();
		gridData48.grabExcessHorizontalSpace = true;
		gridData48.horizontalAlignment = GridData.FILL;
		gridData48.verticalAlignment = GridData.FILL;
		gridData48.grabExcessVerticalSpace = true;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 2;
		groupAdvanced = new Group(compositeAdvanced, SWT.NONE);
		groupAdvanced.setText("Avançado");
		groupAdvanced.setLayout(gridLayout5);
		groupAdvanced.setLayoutData(gridData48);
		tableAdvanced = new Table(groupAdvanced, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION);
		tableAdvanced.setHeaderVisible(true);
		tableAdvanced.setLinesVisible(false);
		tableAdvanced.setLayoutData(gridData49);
		tableAdvanced
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						TableItem[] titem = tableAdvanced.getSelection();
						cComboTag.setText(titem[0].getText());
						textAdvancedValue.setEnabled(true);
						textAdvancedValue.setText(ogg.comments
								.getProperty(titem[0].getText()));
						textAdvancedValue.setFocus();
						buttonDeleteTag.setEnabled(true);
					}
				});
		createCompositeAdvancedForm();
		TableColumn tableColumnTag = new TableColumn(tableAdvanced, SWT.NONE);
		tableColumnTag.setWidth(72);
		tableColumnTag.setResizable(false);
		tableColumnTag.setText("Rótulo");
		TableColumn tableColumnValue = new TableColumn(tableAdvanced, SWT.NONE);
		tableColumnValue.setWidth(160);
		tableColumnValue.setResizable(false);
		tableColumnValue.setText("Valor");
	}

	/**
	 * This method initializes compositeAdvancedForm
	 * 
	 */
	private void createCompositeAdvancedForm() {
		GridData gridData41 = new GridData();
		gridData41.grabExcessHorizontalSpace = true;
		gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData40 = new GridData();
		gridData40.grabExcessHorizontalSpace = true;
		gridData40.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData51 = new GridData();
		gridData51.horizontalSpan = 4;
		gridData51.horizontalAlignment = GridData.FILL;
		GridData gridData55 = new GridData();
		gridData55.grabExcessHorizontalSpace = true;
		gridData55.horizontalSpan = 2;
		gridData55.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData54 = new GridData();
		gridData54.horizontalSpan = 4;
		GridData gridData53 = new GridData();
		gridData53.horizontalSpan = 4;
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.numColumns = 4;
		gridLayout6.marginWidth = 0;
		gridLayout6.marginHeight = 0;
		GridData gridData52 = new GridData();
		gridData52.grabExcessHorizontalSpace = true;
		gridData52.horizontalAlignment = GridData.FILL;
		gridData52.verticalAlignment = GridData.FILL;
		gridData52.horizontalSpan = 4;
		gridData52.grabExcessVerticalSpace = true;
		GridData gridData50 = new GridData();
		gridData50.grabExcessHorizontalSpace = true;
		gridData50.horizontalAlignment = GridData.FILL;
		gridData50.verticalAlignment = GridData.FILL;
		gridData50.grabExcessVerticalSpace = true;
		compositeAdvancedForm = new Composite(groupAdvanced, SWT.NONE);
		compositeAdvancedForm.setLayoutData(gridData50);
		compositeAdvancedForm.setLayout(gridLayout6);
		labelAdvancedTag = new Label(compositeAdvancedForm, SWT.NONE);
		labelAdvancedTag.setText("Rótulo");
		labelAdvancedTag.setLayoutData(gridData53);
		cComboTag = new CCombo(compositeAdvancedForm, SWT.BORDER);
		cComboTag.setEnabled(false);
		cComboTag.setLayoutData(gridData51);
		cComboTag.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				if (!cComboTag.getText().equals("")) {
					ogg.comments.put(cComboTag.getText(), textAdvancedValue
							.getText());
				} else {
					buttonDeleteTag.setEnabled(false);
					textAdvancedValue.setEnabled(false);
				}
				cComboTag.setEnabled(false);
				showComments();

			}
		});
		cComboTag.setItems(getAllTagsSuggestions());
		labelAdvancedValue = new Label(compositeAdvancedForm, SWT.NONE);
		labelAdvancedValue.setText("Valor");
		labelAdvancedValue.setLayoutData(gridData54);
		textAdvancedValue = new Text(compositeAdvancedForm, SWT.MULTI
				| SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		textAdvancedValue.setEnabled(false);
		textAdvancedValue.setLayoutData(gridData52);
		textAdvancedValue
				.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {
					public void focusLost(org.eclipse.swt.events.FocusEvent e) {
						if (!textAdvancedValue.getText().equals(""))
							ogg.comments.put(cComboTag.getText(),
									textAdvancedValue.getText());
						showComments();
					}
				});
		buttonNewTag = new Button(compositeAdvancedForm, SWT.NONE);
		buttonNewTag.setText("Novo Rótulo");
		buttonNewTag.setFont(new Font(Display.getDefault(), buttonNewTag
				.getFont().toString(), 7, SWT.BOLD));
		buttonNewTag.setLayoutData(gridData55);
		buttonNewTag
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						cComboTag.setText("");
						cComboTag.setEnabled(true);
						textAdvancedValue.setEnabled(true);
						textAdvancedValue.setText("");
						buttonDeleteTag.setEnabled(true);
						cComboTag.setFocus();
					}
				});
		buttonDeleteTag = new Button(compositeAdvancedForm, SWT.NONE);
		buttonDeleteTag.setText("Excluir");
		buttonDeleteTag.setEnabled(false);
		buttonDeleteTag.setLayoutData(gridData40);
		buttonDeleteTag.setFont(new Font(Display.getDefault(), buttonDeleteTag
				.getFont().toString(), 7, SWT.NORMAL));
		buttonDeleteTag
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						ogg.comments.remove(cComboTag.getText());
						cComboTag.setText("");
						cComboTag.setEnabled(false);
						textAdvancedValue.setText("");
						textAdvancedValue.setEnabled(false);
						buttonDeleteTag.setEnabled(false);
						showComments();
					}
				});
		buttonDeleteAll = new Button(compositeAdvancedForm, SWT.NONE);
		buttonDeleteAll.setText("Excluir Todos");
		buttonDeleteAll.setLayoutData(gridData41);
		buttonDeleteAll.setFont(new Font(Display.getDefault(), buttonDeleteAll
				.getFont().toString(), 7, SWT.NORMAL));
		buttonDeleteAll
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						ogg.comments.clear();
						showComments();
					}
				});
	}

	/**
	 * @return
	 */
	private String[] getAllTagsSuggestions() {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:data/suggestions.db");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT tag FROM tags ORDER BY tag");
			while (rs.next())
				result.add(rs.getString("tag"));
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * @return
	 */
	private String[] getAllGenresSuggestions() {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:data/suggestions.db");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT genre FROM genres ORDER BY genre");
			while (rs.next())
				result.add(rs.getString("genre"));
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toArray(new String[result.size()]);
	}

	private boolean doExit() {
		isClosing = true;
		sShell.close();
		sShell.dispose();
		return true;
	}

	/**
	 * This method initializes tabFolder
	 * 
	 */
	private void createTabFolder() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = GridData.FILL;
		tabFolder = new TabFolder(getSShell(), SWT.NONE);
		createCompositeBasicInfo();
		tabFolder.setLayoutData(gridData1);
		createCompositeAdvanced();
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Informações Básicas");
		tabItem.setControl(compositeBasicInfo);
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
		tabItem1.setText("Avançado");
		tabItem1.setControl(compositeAdvanced);
	}

	public void showFormatInfo() {
		labelLength.setText("Duração: "
				+ (ogg.info.containsKey("length") ? ogg.info
						.getProperty("length") : "?"));
		labelAvgBitrate.setText("Bitrate médio: "
				+ (ogg.info.containsKey("average_bitrate") ? ogg.info
						.getProperty("average_bitrate") : "?"));
		labelFileSize.setText("Tamanho: "
				+ (ogg.info.containsKey("file_size") ? ogg.info
						.getProperty("file_size") : "?"));
		labelNomBitrate.setText("Bitrate nominal: "
				+ (ogg.info.containsKey("nominal_bitrate") ? ogg.info
						.getProperty("nominal_bitrate") : "?"));
		labelMaxBitrate.setText("Bitrate máximo: "
				+ (ogg.info.containsKey("max_bitrate") ? ogg.info
						.getProperty("max_bitrate") : "?"));
		labelChannels.setText("Canais: "
				+ (ogg.info.containsKey("channels") ? ogg.info
						.getProperty("channels") : "?"));
		labelSampeRate.setText("Amostragem: "
				+ (ogg.info.containsKey("sampling_rate") ? ogg.info
						.getProperty("sampling_rate") : "?"));
		labelSerialNumber.setText("Número serial: "
				+ (ogg.info.containsKey("serial_number") ? ogg.info
						.getProperty("serial_number") : "?"));
		labelVersion.setText("Versão: "
				+ (ogg.info.containsKey("version") ? ogg.info
						.getProperty("version") : "?"));
		labelVendor.setText("Proprietário: "
				+ (ogg.info.containsKey("vendor") ? ogg.info
						.getProperty("vendor") : "?"));
	}

	public void showComments() {
		textTrack.setText((ogg.comments.containsKey("TRACKNUMBER") ? ogg.comments.getProperty("TRACKNUMBER"): ""));
		textDisc.setText((ogg.comments.containsKey("DISCNUMBER") ? ogg.comments.getProperty("DISCNUMBER") : ""));
		textBPM.setText((ogg.comments.containsKey("BPM") ? ogg.comments.getProperty("BPM") : ""));
		textTitle.setText((ogg.comments.containsKey("TITLE") ? ogg.comments.getProperty("TITLE") : ""));
		textArtist.setText((ogg.comments.containsKey("ARTIST") ? ogg.comments.getProperty("ARTIST") : ""));
		textAlbum.setText((ogg.comments.containsKey("ALBUM") ? ogg.comments.getProperty("ALBUM") : ""));
		textYear.setText((ogg.comments.containsKey("DATE") ? ogg.comments.getProperty("DATE") : ""));
		cComboGenre.setText((ogg.comments.containsKey("GENRE") ? ogg.comments.getProperty("GENRE") : ""));
		textComment.setText((ogg.comments.containsKey("COMMENT") ? ogg.comments.getProperty("COMMENT") : ""));
		textComposer.setText((ogg.comments.containsKey("COMPOSER") ? ogg.comments.getProperty("COMPOSER") : ""));
		textPublisher.setText((ogg.comments.containsKey("PUBLISHER") ? ogg.comments.getProperty("PUBLISHER") : ""));

		List<String> listKeys = new LinkedList<String>();
		Enumeration en = ogg.comments.keys();
		while (en.hasMoreElements())
			listKeys.add((String) en.nextElement());
		Collections.sort(listKeys);

		tableAdvanced.removeAll();

		for (Iterator it = listKeys.iterator(); it.hasNext();) {
			String key = (String) it.next();
			TableItem item = new TableItem(tableAdvanced, SWT.NONE);
			item.setText(new String[] { key, (ogg.comments.getProperty(key).length()<64?ogg.comments.getProperty(key):ogg.comments.getProperty(key).substring(0,64)+"...") });
		}
	}
	
	public byte[] decodeString(String s) {
		String[] intArray = s.split(" ");
		byte[] bytes = new byte[intArray.length];
		for(int i=0; i<intArray.length; i++) {
			bytes[i] = (byte) Integer.parseInt(intArray[i]);
		}
		return bytes;
	}
}
