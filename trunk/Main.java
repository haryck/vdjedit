
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.ajmm.vdj.database.Database;
import org.ajmm.vdj.gui.Table;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.01
 */
public class Main extends JFrame
{
	private static final long serialVersionUID = 4648172894076113183L;
	private static final String APPLICATION_NAME = "Virtual DJ Database Editor";

	private final Table table;
	private final JMenu fileMenu;
	private final JMenuItem loadMenuItem;
	private final JMenuItem saveMenuItem;
	private final JMenuItem exitMenuItem;
	private final JMenu viewMenu;
	private final JMenuItem viewAudioMenuItem;
	private final JMenuItem viewVideoMenuItem;
	private final JMenuItem viewKaraokeMenuItem;
	private final JMenuItem viewHiddenMenuItem;
	private final JTextField searchTextField;
	private final JLabel statusBar;

	private Database database;

	public Main()
	{
		super(APPLICATION_NAME);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		{
			/*
			 * create the menu bar for the application
			 */
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);

			/*
			 *
			 */
			{
				fileMenu = new JMenu("File");
				menuBar.add(fileMenu);

				loadMenuItem = new JMenuItem("Reload Database(s)");
				loadMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							database = loadAllDatabases();
						}
						catch (Exception e1) {
							JOptionPane.showMessageDialog(null,
									"At least one of your databases contains " +
									"an error, please use Virtual DJ's fix "+
									"option", "Error", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
					}

				});
				fileMenu.add(loadMenuItem);

				saveMenuItem = new JMenuItem("Save Database(s)");
				saveMenuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (database != null)
						{
							try
							{
								database.save();
							}
							catch (Exception e1)
							{
								JOptionPane.showMessageDialog(null,
										"The save operation has encountered an error",
										"Error", JOptionPane.ERROR_MESSAGE);
								e1.printStackTrace();
							}
						}
					}

				});
				fileMenu.add(saveMenuItem);

				fileMenu.add(new JSeparator());
				exitMenuItem = new JMenuItem("Exit");
				exitMenuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}

				});
				fileMenu.add(exitMenuItem);
			}

			/*
			 *
			 */
			{
				viewMenu = new JMenu("View");
				menuBar.add(viewMenu);

				viewAudioMenuItem = new JMenuItem("Hide Audio Files");
				viewAudioMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleViewType(viewAudioMenuItem);
					}

				});
				viewMenu.add(viewAudioMenuItem);

				viewVideoMenuItem = new JMenuItem("Hide Video Files");
				viewVideoMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleViewType(viewVideoMenuItem);
					}

				});
				viewMenu.add(viewVideoMenuItem);

				viewKaraokeMenuItem = new JMenuItem("Hide Karaoke Files");
				viewKaraokeMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleViewType(viewKaraokeMenuItem);
					}

				});
				viewMenu.add(viewKaraokeMenuItem);

				viewMenu.add(new JSeparator());
				viewHiddenMenuItem = new JMenuItem("View Hidden Files");
				viewHiddenMenuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						toggleViewType(viewHiddenMenuItem);
					}

				});
				viewMenu.add(viewHiddenMenuItem);
			}
		}

		{
			/*
			 * create search field to search through entries in the database
			 * matching the entered string
			 */
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			add(panel, BorderLayout.NORTH);

			JLabel label = new JLabel("Search");
			panel.add(label);

			searchTextField = new JTextField(36);
			searchTextField.setToolTipText("Search for songs with a matching title, artist, album, genre or year");
			searchTextField.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						searchTextField.setText(null);
						table.filter(null);
					}
				}
			});
			searchTextField.addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(KeyEvent e)
				{
					// no action if no databases have been loaded
					if (database == null) return;

					String filter = searchTextField.getText();
					table.filter(filter);
				}

			});
			panel.add(searchTextField);
		}

		{
			/*
			 * create the table to display the loaded database
			 */
			JScrollPane scrollPane = new JScrollPane(table = new Table());
			add(scrollPane, BorderLayout.CENTER);
		}

		{
			/*
			 *
			 */
			statusBar = new JLabel("Ready");
			add(statusBar, BorderLayout.SOUTH);
		}

		/*
		 * initialization code to display splash screen and load databases
		 */
		{
			try {
				database = loadAllDatabases();
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"At least one of your databases contains " +
						"an error, please use Virtual DJ's fix "+
						"option", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}

		setPreferredSize(new Dimension(1024, 680));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		/*
		 * try to set the user interface to the 'nimbus' look and feel if it's
		 * available - otherwise attempt to use the system's default look and
		 * feel
		 */
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (info.getName().equals("Nimbus")) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e1) { e1.printStackTrace(); }
		}

		new Main(); // start the application

	}

	private Database loadAllDatabases() throws Exception
	{
		File[] files = Database.getDbFiles();
		Database systemDb = Database.load(files[0]);

		for (int i = 1; i < files.length; i++)
		{
			Database localDb = Database.load(files[i]);
			systemDb.merge(localDb);
		}

		table.setTableData(systemDb);
		statusBar.setText(systemDb.getSongCount() + " entries loaded");

		return systemDb;
	}

	private void toggleViewType(JMenuItem menuItem)
	{
		String[] split = menuItem.getText().split(" ");
		split[0] = (split[0].equals("Hide")) ? "Show" : "Hide";
		menuItem.setText(split[0] + " " + split[1] + " " + split[2]);

		if (split[1].equals("Audio"))	table.toggleShowAudio();	else
		if (split[1].equals("Video"))	table.toggleShowVideo();	else
		if (split[1].equals("Karaoke"))	table.toggleShowKaraoke();	else
		if (split[1].equals("Hidden"))	table.toggleShowHidden();
	}

}
