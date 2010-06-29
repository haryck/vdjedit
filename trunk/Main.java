
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
import java.io.FilenameFilter;

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
import org.ajmm.vdj.gui.VDJTable;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.06.28
 */
public class Main extends JFrame
{
	private static final long serialVersionUID = 4648172894076113183L;
	private static final String APPLICATION_NAME = "Virtual DJ Database Editor";
	private static final FilenameFilter FILE_NAME_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.equalsIgnoreCase("VirtualDJ Local Database v6.xml");
		}
	};

	private final VDJTable table;
	private final JMenuItem viewAudioMenuItem;
	private final JMenuItem viewVideoMenuItem;
	private final JMenuItem viewKaraokeMenuItem;
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
				JMenu menu = new JMenu("File");
				menuBar.add(menu);

				JMenuItem menuItem = new JMenuItem("Reload Database(s)");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							loadAllDatabases();
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
				menu.add(menuItem);

				menuItem = new JMenuItem("Save Database(s)");
				menuItem.addActionListener(new ActionListener()
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
				menu.add(menuItem);

				menu.add(new JSeparator());
				menuItem = new JMenuItem("Exit");
				menuItem.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}

				});
				menu.add(menuItem);
			}

			/*
			 *
			 */
			{
				JMenu menu = new JMenu("View");
				menuBar.add(menu);

				viewAudioMenuItem = new JMenuItem("Hide Audio Files");
				viewAudioMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleViewType(viewAudioMenuItem);
					}

				});
				menu.add(viewAudioMenuItem);

				viewVideoMenuItem = new JMenuItem("Hide Video Files");
				viewVideoMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleViewType(viewVideoMenuItem);
					}

				});
				menu.add(viewVideoMenuItem);

				viewKaraokeMenuItem = new JMenuItem("Hide Karaoke Files");
				viewKaraokeMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleViewType(viewKaraokeMenuItem);
					}

				});
				menu.add(viewKaraokeMenuItem);
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
				public void keyPressed(KeyEvent e)
				{
					if (database != null)
					{
						String filter = searchTextField.getText();
						table.filter(filter);
					}
				}

			});
			panel.add(searchTextField);
		}

		{
			/*
			 * create the table to display the loaded database
			 */
			JScrollPane scrollPane = new JScrollPane(table = new VDJTable());
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

		/* start the application */
		new Main();

	}

	private Database loadAllDatabases() throws Exception
	{
		/* temporary variable to store the parent database */
		Database database = null;

		File systemDbFile = Database.getSystemDbFile();
		if (systemDbFile.exists()) database = Database.load(systemDbFile);

		for (File drive : File.listRoots())
		{
			/* skip unreadable devices, e.g. empty optical media */
			if (!drive.canRead()) continue;

			File[] files = drive.listFiles(FILE_NAME_FILTER);
			if (files.length == 1)
			{
				Database tmp = Database.load(files[0]);

				if (database != null) database.merge(tmp);
				else database = tmp;
			}
		}

		table.setTableData(database);
		statusBar.setText(database.getSongCount() + " entries loaded");

		return database;
	}

	private void toggleViewType(JMenuItem menuItem)
	{
		String[] split = menuItem.getText().toLowerCase().split(" ");
		split[0] = (split[0].equals("hide")) ? "Show" : "Hide";
		menuItem.setText(split[0] + " " + split[1]);

		if (split[1].equals("audio"))	table.toggleShowAudio();	else
		if (split[1].equals("video"))	table.toggleShowVideo();	else
		if (split[1].equals("karaoke"))	table.toggleShowKaraoke();
	}

}
