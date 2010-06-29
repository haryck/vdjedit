package org.ajmm.vdj.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.ajmm.framework.xml.XmlNode;
import org.ajmm.vdj.database.exception.DuplicateSongException;
import org.ajmm.vdj.xml.VDJXmlReader;
import org.ajmm.vdj.xml.VDJXmlWriter;

/**
 * 
 * 
 * @author	Andrew Mackrodt
 * @version	2010.06.28
 */
public class Database extends XmlNode
{
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddhhmmss");
	
	private final Set<FavoriteFolder> favorites;
	private final Set<FilterFolder> filters;
	private final Set<VirtualFolder> virtual;
	private final Set<Song> songs;
	private File file;
	
	public Database()
	{
		/* 
		 * use a small value for childCapacity as only unknown elements are
		 * passed to the super's xmlnode set - normally there will be 0
		 * unknown elements so this should be enough
		 */
		super("VirtualDJ_Database", 16);
		
		favorites	= new TreeSet<FavoriteFolder>();
		filters		= new TreeSet<FilterFolder>();
		virtual		= new TreeSet<VirtualFolder>();
		songs		= new TreeSet<Song>();
	}
	
	public File getSuggestedFile() {
		return file;
	}
	
	public void setSuggestedFile(File file) {
		this.file = file;
	}
	
	public int getVersion() {
		return getAttributeAsInteger("Version");
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

	public boolean addFolder(AbstractFolder folder) {
		return addChild(folder);
	}
	
	public boolean removeFolder(AbstractFolder folder) {
		return removeChild(folder);
	}
	
	public int getSongCount() {
		return songs.size();
	}
	
	public Set<Song> getSongs() {
		return Collections.unmodifiableSet(songs);
	}

	public boolean addSong(Song song) throws DuplicateSongException
	{
		if (!addChild(song)) throw new DuplicateSongException();

		return true;
	}
	
	public boolean removeSong(Song song) {
		return removeChild(song);
	}

	public boolean merge(Database database)
	{
		/* cannot merge a database with null or itself */
		if (database == null || database == this) return false;
		
		favorites.addAll(database.favorites);
		filters.addAll(database.filters);
		virtual.addAll(database.virtual);
		songs.addAll(database.songs);
		
		return true;
	}
	
	@Override
	public boolean hasChildren() {
		return getChildCount() > 0;
	}
	
	@Override
	public boolean hasChild(XmlNode element)
	{
		if (element instanceof Song)			return songs.contains(element);		else
		if (element instanceof FavoriteFolder)	return favorites.contains(element);	else
		if (element instanceof FilterFolder)	return filters.contains(element);	else
		if (element instanceof VirtualFolder)	return virtual.contains(element);
		
		return super.hasChild(element);
	}
	
	@Override
	public boolean addChild(XmlNode element)
	{
		if (element instanceof Song)			return songs.add((Song)element);				else
		if (element instanceof FavoriteFolder)	return favorites.add((FavoriteFolder)element);	else
		if (element instanceof FilterFolder)	return filters.add((FilterFolder)element);		else
		if (element instanceof VirtualFolder)	return virtual.add((VirtualFolder)element);
		
		return super.addChild(element); 
	}
	
	@Override
	public boolean removeChild(XmlNode element)
	{
		if (element instanceof Song)			return songs.remove(element);		else
		if (element instanceof FavoriteFolder)	return favorites.remove(element);	else
		if (element instanceof FilterFolder)	return filters.remove(element);		else
		if (element instanceof VirtualFolder)	return virtual.remove(element);
			
		return super.removeChild(element);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<XmlNode> getChildren()
	{
		Set<XmlNode> nodes = new LinkedHashSet<XmlNode>();
		Set<XmlNode>[] s = new Set[] {
			favorites, filters, virtual, songs, super.getChildren()
		};
		for (int i = 0; i < s.length; i++)
		{
			Iterator<XmlNode> itr = s[i].iterator();
			while (itr.hasNext()) {
				nodes.add(itr.next());
			}
		}
		return Collections.unmodifiableSet(nodes);
	}
	
	@Override
	public int getChildCount() {
		return
				super.getChildCount() +
				favorites.size() +
				filters.size() +
				virtual.size() +
				songs.size();
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
			
			VDJXmlWriter.save(database, database.getSuggestedFile().getAbsolutePath());
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
		Database database = VDJXmlReader.parse(file.getAbsolutePath());
		database.setSuggestedFile(file);
		database.fixFilePaths();
		return database;
	}
	
	public static Database load(String location) throws Exception {
		return VDJXmlReader.parse(location);
	}
	
	private Database[] split() throws Exception
	{
		Map<String, Database> databases = new TreeMap<String, Database>();
		for (Song song : songs)
		{
			int off = song.getFilePath().indexOf('\\');
			String drive = song.getFilePath().substring(0, off);
			Database tmp = databases.get(drive);
			
			if (tmp == null) {
				tmp = Database.copyStructure(this, drive);
				databases.put(drive, tmp);
			}
			
			tmp.addChild(song);
		}
		
		File systemDbFile = getSystemDbFile();
		int off = systemDbFile.getAbsolutePath().indexOf('\\');
		String drive = systemDbFile.getAbsolutePath().substring(0, off);
		
		Database[] databaseArray = new Database[databases.size()];
		if ((databaseArray[0] = databases.remove(drive)) == null)
		{
			databaseArray = new Database[databaseArray.length+1];
			databaseArray[0] = Database.copyStructure(this, drive);
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
	
	public static File getSystemDbFile() throws Exception
	{
		File file = null;
		
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
			file = new File(query, "VirtualDJ Database v6.xml");
		}

		return file;
	}
	
	private static Database copyStructure(Database database, String drive)
	{
		Database db = new Database();
		db.setSuggestedFile(new File(drive, "VirtualDJ Local Database v6.xml"));
		
		Map<String, String> attributes = database.getAttributes();
		for (String attribute : attributes.keySet()) {
			db.setAttribute(attribute, attributes.get(attribute));
		}

		return db;
	}
	
}
