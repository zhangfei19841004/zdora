package com.zf.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhangfei on 2019/7/14.
 */
public class FilesUtil {

	public static void getAllFilePaths(List<String> paths, String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			paths.add(file.getAbsolutePath());
		} else {
			File[] files = file.listFiles(t -> !t.isHidden() && (t.isFile() || (t.isDirectory() && !t.getName().startsWith("."))));
			for (File f : files) {
				if (f.isFile()) {
					paths.add(f.getAbsolutePath());
				} else {
					getAllFilePaths(paths, f.getAbsolutePath());
				}
			}
		}
	}

	public static List<Map<String, String>> getAllFilePaths(String rootPath) {
		File rootFile = new File(rootPath);
		Collection<File> files = FileUtils.listFiles(rootFile, null, true);
		List<String> dirs = new ArrayList<>();
		List<Map<String, String>> result = new ArrayList<>();
		files.forEach(t -> {
			String tempPath = t.getAbsolutePath().substring(rootFile.getAbsolutePath().length() + 1);
			Map<String, String> tmap = new HashMap<>();
			tmap.put("fn", tempPath);
			tmap.put("type", "file");
			result.add(tmap);
			dirs.add(tempPath);
			List<String> names = CollectionUtils.arrayToList(tempPath.split(Matcher.quoteReplacement(File.separator)));
			Stream.iterate(1, i -> i + 1).limit(names.size() - 1).forEach(t1 -> {
				String subPath = String.join(File.separator, names.subList(0, t1));
				if (!dirs.contains(subPath)) {
					Map<String, String> tmap1 = new HashMap<>();
					tmap1.put("fn", subPath);
					tmap1.put("type", "dir");
					result.add(tmap1);
					dirs.add(subPath);
				}
			});
		});
		return result.stream().sorted(Comparator.comparing(o -> o.get("fn"))).collect(Collectors.toList());
	}

}
