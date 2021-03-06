package javamods;

import java.io.File;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;


public class Filesystem {

	private final static int ZOMBOID_GAME_ID = 108600;
	private final static String MOD_INFO = "mod.info";
	private final static String MODS = "mods";

	private static File userProfileDir;
	private static File steamInstallDir;
	private static File steamWorkshopDir;
	private static List<File> modFolders;

	public static File getLogFile() {
		return new File(getUserProfileDir(), "javamods-console.txt");
	}

	public static File getSteamInstallDir() {

		if (steamInstallDir != null)
			return steamInstallDir;

		URI jarURI = null;
		try {
			jarURI = Filesystem.class.getProtectionDomain().getCodeSource().getLocation().toURI();
		}
		catch (URISyntaxException error) {
			throw new RuntimeException(error);
		}

		File installDir = Paths.get(jarURI).toFile().getParentFile();

		if (!installDir.exists() || !installDir.isDirectory())
			throw new RuntimeException("Couldn't find Steam install folder!");

		steamInstallDir = installDir;
		return steamInstallDir;
	}

	public static File getSteamWorkshopDir() {

		if (steamWorkshopDir != null)
			return steamWorkshopDir;

		File workshopDir = null;
		File dir = getSteamInstallDir();
		if (Core.server)
			dir = new File(dir.getParentFile(), "steamapps");
		else
			dir = dir.getParentFile().getParentFile();

		for (int i = 0; i < 3; i++) {

			File subdir = new File(dir, "workshop");
			if (subdir.exists()) {
				workshopDir = new File(subdir, "content" + File.separator + ZOMBOID_GAME_ID);
				break;
			}

			dir = dir.getParentFile();
		}

		if (workshopDir == null || !workshopDir.exists() || !workshopDir.isDirectory())
			System.err.println("Couldn't find Steam workshop folder!");

		steamWorkshopDir = workshopDir;
		return steamWorkshopDir;
	}

	public static File getSteamModDir() {
		File modsDir = new File(getSteamInstallDir(), "steamapps" + File.separator + "workshop" + File.separator + "content" + File.separator + "108600");
		return modsDir;
	}

	public static File getUserProfileDir() {

		if (userProfileDir != null)
			return userProfileDir;

		String userProfilePath = System.getProperty("deployment.user.cachedir");
		if (userProfilePath == null) {
			userProfilePath = System.getProperty("user.home");
			if (userProfilePath == null)
				throw new RuntimeException("Couldn't find user home folder!");
		}

		userProfileDir = new File(userProfilePath + File.separator + "Zomboid");
		return userProfileDir;
	}

	public static File getUserProfileModDir() {
		return new File(getUserProfileDir(), MODS);
	}

	public static File getUserProfileSaveDir() {
		return new File(getUserProfileDir(), "Saves");
	}

	public static File getUserProfilesWorkshopDir() {
		return new File(getUserProfileDir(), "Workshop");
	}

	public static File getUserProfileSubdir(String subdir) {
		return new File(getUserProfileDir(), subdir);
	}

	private static boolean isModFolder(File modFolder) {
		File modInfo = new File(modFolder, MOD_INFO);
		return modInfo.exists() && modInfo.isFile();
	}

	private static File isSteamWorkshopMod(File modFolder) {
		File dir = new File(modFolder, MODS);
		return dir.exists() && dir.isDirectory() ? dir : null;
	}

	private static File isUserProfileWorkshopMod(File modFolder) {
		File dir = new File(modFolder, "Contents" + File.separator + MODS);
		return dir.exists() && dir.isDirectory() ? dir : null;
	}

	private static void scanModsHelper(File modLocation) {

		for (String modId : modLocation.list()) {
			File modFolder = new File(modLocation, modId);
			if (isModFolder(modFolder))
				modFolders.add(modFolder);
		}
	}

	private static void scanMods(File modLocation) {

		File modDir;

		if (modLocation == null)
			return;

		if (!modLocation.exists()) {
			Log.warn("Mod folder does not exist: " + modLocation + ", skipping.");
			return;
		}

		for (String modId : modLocation.list()) {

			File modFolder = new File(modLocation, modId);
			if (isModFolder(modFolder))
				modFolders.add(modFolder);

			else if ((modDir = isSteamWorkshopMod(modFolder)) != null)
				scanModsHelper(modDir);

			else if ((modDir = isUserProfileWorkshopMod(modFolder)) != null)
				scanModsHelper(modDir);
		}
	}

	public static File[] getModLocations() {

		return new File[] {
			getUserProfilesWorkshopDir(),
			getUserProfileModDir(),
			getSteamWorkshopDir(),
			getSteamModDir(),
		};
	}

	public static List<File> getMods() {

		if (modFolders != null)
			return modFolders;

		modFolders = new ArrayList<>();

		for (File modLocation : getModLocations())
			scanMods(modLocation);

		return modFolders;
	}
}
