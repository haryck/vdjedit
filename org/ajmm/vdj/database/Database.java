package org.ajmm.vdj.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 *
 * @author	Andrew Mackrodt
 * @version	2010.07.12
 */
public class Database implements SongContainer
{
	public static final String ELEMENT_NAME = "VirtualDJ_Database";
	public static final String ATTRIB_VERSION = "Version";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddhhmmss");
	private static File systemDbFile;

	private final Set<FavoriteFolder> favorites;
	private final Set<FilterFolder> filters;
	private final Set<VirtualFolder> virtual;
	private final Set<Song> songs;
	private int version;
	private File file;

	public Database()
	{
		favorites = new TreeSet<FavoriteFolder>();
		filters	= new TreeSet<FilterFolder>();
		virtual	= new TreeSet<VirtualFolder>();
		songs = new TreeSet<Song>();
	}

	public File getSuggestedFile() {
		return file;
	}

	public void setSuggestedFile(File file) {
		this.file = file;
	}

	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		if (version > -1) this.version = version;
	}

	public Set<FavoriteFolder> getFavoriteFolders() {
		return Collections.unmodifiableSet(favorites);
	}

	public Set<FilterFolder> getFilterFolders() {
		return Collections.unmodifiableSet(filters);
	}

	public Set<VirtualFolder> getVirtualFolders() {
		return Collections.unmodifiableSet(virtual);
	}

	public boolean addFolder(AbstractFolder folder)
	{
		if (folder instanceof FavoriteFolder) return favorites.add((FavoriteFolder)folder);
		if (folder instanceof FilterFolder) return filters.add((FilterFolder)folder);
		if (folder instanceof VirtualFolder) return virtual.add((VirtualFolder)folder);
		return false;
	}

	public boolean removeFolder(AbstractFolder folder)
	{
		if (folder instanceof FavoriteFolder) return favorites.remove((FavoriteFolder)folder);
		if (folder instanceof FilterFolder) return filters.remove((FilterFolder)folder);
		if (folder instanceof VirtualFolder) return virtual.remove((VirtualFolder)folder);
		return false;
	}

	public int getSongCount() {
		return songs.size();
	}

	public Set<Song> getSongs() {
		return Collections.unmodifiableSet(songs);
	}

	public boolean addSong(Song song) {
		return songs.add(song);
	}

	public boolean removeSong(Song song) {
		return songs.remove(song);
	}

	public boolean merge(Database database)
	{
		// cannot merge a database with null or itself
		if (database == null || database == this) return false;

		favorites.addAll(database.favorites);
		filters.addAll(database.filters);
		virtual.addAll(database.virtual);
		songs.addAll(database.songs);

		return true;
	}

	public void save() throws Exception
	{
		File backupFolder = new File(System.getProperty("user.dir"));
		backupFolder = new File(backupFolder, ".settings");
		backupFolder = new File(backupFolder, "backup");
		backupFolder = new File(backupFolder, SDF.format(Calendar.getInstance().getTime()));

		for (Database database : split())
		{
			File output = database.getSuggestedFile();
			if (output.exists())
			{
				String drive = (""+output.getAbsolutePath().charAt(0)).toLowerCase();
				File tmpBackupFolder = new File(backupFolder, drive);

				/*
				 * try to create the backup folder if it does not exist
				 * otherwise throw an exception as no backups can be created
				 */
				if (!tmpBackupFolder.exists()) {
					if (!tmpBackupFolder.mkdirs())
						throw new Exception("unable to create backup folder");
				}

				File backupFile = new File(tmpBackupFolder, output.getName());
				if (!output.renameTo(backupFile)) {
					throw new Exception(
							"operation aborted as the process has failed to " +
							"backup one or more databases, manual recovery " +
							"may be required");
				}
			}

//			VDJXmlWriter.save(database, database.getSuggestedFile().getAbsolutePath());
		}
	}

	private void fixFilePaths()
	{
		if (file == null || !file.getName().toLowerCase().contains("local")) return;

		String drive = ""+file.getAbsolutePath().charAt(0);
		for (Song song : songs) {
			if (!(""+song.getFilePath().charAt(0)).equals(drive)) {
				song.setFilePath(drive+song.getFilePath().substring(1));
			}
		}
	}

	public static Database load(File file) throws Exception
	{
		final Database database = new Database();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(file.getAbsolutePath(), new DefaultHandler()
		{
			private StringBuilder sb;
			private Song song;

			@Override
			public void endElement(String uri, String localName, String qName)
			{
				if (sb != null) // only act on comments text
				{
					String comment = sb.toString();
					song.comment().set(comment);
					sb = null; // clear the buffer so it's not processed again
				}
			}

			@Override
			public void startElement(String uri, String localName, String qName, Attributes atts)
			{
				sb = null;
				SongContainer container = database;
				if (qName.equals("Song")) song = Song.parse(atts, container);				   else
				if (qName.equals("VirtualFolder")) VirtualFolder.parse(atts, database); 	   else
				if (qName.equals("FavoriteFolder")) FavoriteFolder.parse(atts, database);	   else
				if (qName.equals("FilterFolder")) FilterFolder.parse(atts, database);		   else
				if (song != null)
				{			
					if (qName.equals(Display.ELEMENT_NAME)) song.display().parse(atts); else
					if (qName.equals(Infos.ELEMENT_NAME))	song.infos().parse(atts);	else
					if (qName.equals(BPM.ELEMENT_NAME))		song.bpm().parse(atts);	    else
					if (qName.equals(FAME.ELEMENT_NAME))	song.fame().parse(atts);    else
					if (qName.equals(Automix.ELEMENT_NAME)) song.automix().parse(atts); else
					if (qName.equals(Link.ELEMENT_NAME))	song.link().parse(atts);	else
					if (qName.equals(Cue.ELEMENT_NAME))		Cue.parse(atts, song);	    else
					if (qName.equals(Comment.ELEMENT_NAME)) sb = new StringBuilder();
				}
			}

			@Override
			public void characters(char[] ch, int start, int length)
			{
				// no action on empty string or non-comment text
				if (length == 0 || sb == null) return;

				int off = start;
				int len = length;

				for (int i = 0; i < length; i++)
				{
					if (ch[off] != ' ')
						break;
					off++;
					len--;
				}

				for (int i = start + length - 1; i > start; i--)
				{
					if (ch[off] != ' ')
						break;
					len--;
				}

				if (len > 0) sb.append(new String(ch, off, len));
			}

		});

		database.setSuggestedFile(file);
		database.fixFilePaths();
		return database;
	}

	private Database[] split() throws Exception
	{
		Map<String, File> dbFileMap = getDbFileMap();
		Map<File, Database> databases = new TreeMap<File, Database>();

		for (Song song : songs)
		{
			int off = song.getFilePath().indexOf('\\');
			String drive = song.getFilePath().substring(0, off);

			/*
			 * get the database file the current entry belongs to - if no
			 * mapping is found then specify that we want to write the entry
			 * to the system database instead
			 */
			File dbFile = dbFileMap.get(drive);
			if (dbFile == null) dbFile = getSystemDbFile();

			/*
			 * get the database to add the current entry to or create it
			 * if it does not exist
			 */
			Database tmp = databases.get(dbFile);
			if (tmp == null) {
				tmp = Database.copyStructure(this, dbFile);
				databases.put(dbFile, tmp);
			}

			addSong(song);
		}

		Database[] databaseArray = new Database[databases.size()];
		if ((databaseArray[0] = databases.remove(getSystemDbFile())) == null)
		{
			databaseArray = new Database[databaseArray.length+1];
			databaseArray[0] = Database.copyStructure(this, getSystemDbFile());
		}
		databaseArray[0].setSuggestedFile(Database.getSystemDbFile());
		databaseArray[0].favorites.addAll(this.favorites);
		databaseArray[0].filters.addAll(this.filters);
		databaseArray[0].virtual.addAll(this.virtual);

		int i = 1;
		for (Database localDb : databases.values()) {
			databaseArray[i++] = localDb;
		}

		return databaseArray;
	}

	private static String getDrive(File file) {
		return file.getAbsolutePath().substring(
				0, file.getAbsolutePath().indexOf('\\'));
	}

	public static File[] getDbFiles() throws Exception
	{
		List<File> dbFiles = new LinkedList<File>();
		File systemDbFile = Database.getSystemDbFile();
		String systemDbDrive = getDrive(systemDbFile);
		dbFiles.add(systemDbFile);

		for (File root : File.listRoots())
		{
			// ignore unmounted drives and drive hosting the system database
			if (!root.canRead() || systemDbDrive == getDrive(root)) continue;

			File localDbFile = new File(root, "VirtualDJ Local Database v6.xml");
			if (localDbFile.exists() && localDbFile.canWrite()) {
				dbFiles.add(localDbFile);
			}
		}

		return dbFiles.toArray(new File[dbFiles.size()]);
	}

	private static Map<String, File> getDbFileMap() throws Exception
	{
		Map<String, File> dbFileMap = new TreeMap<String, File>();
		File systemDbFile = Database.getSystemDbFile();
		String systemDbDrive = getDrive(systemDbFile);
		dbFileMap.put(systemDbDrive, systemDbFile);

		for (File root : File.listRoots()) {
			if (systemDbDrive != getDrive(root)) {
				File localDbFile = new File(root, "VirtualDJ Local Database v6.xml");
				if (localDbFile.exists() && localDbFile.canWrite()) {
					String drive = getDrive(localDbFile);
					dbFileMap.put(drive, localDbFile);
				}
			}
		}

		return dbFileMap;
	}

	public static File getSystemDbFile() throws Exception
	{
		// return cached system database file if available
		if (systemDbFile != null) return systemDbFile;

		String os = System.getProperty("os.name");
		if (os != null && os.toLowerCase().contains("windows"))
		{
			String query = System.getenv("programfiles");
			query =
				"reg query " + ((query.endsWith("(x86)"))
					? "hklm\\software\\wow6432node\\virtualdj"
					: "hklm\\software\\virtualdj") +
				" /v homefolder";

			Process process = Runtime.getRuntime().exec(query);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			query = br.readLine(); // empty line
			query = br.readLine(); // line containing registry location
			query = br.readLine(); // line containing registry value
			query = query.substring(query.indexOf(':')-1);
			systemDbFile = new File(query, "VirtualDJ Database v6.xml");
		}

		return systemDbFile;
	}

	private static Database copyStructure(Database db, File file)
	{
		Database copy = new Database();
		copy.setVersion(db.getVersion());
		copy.setSuggestedFile(file);
		return copy;
	}
	
}
