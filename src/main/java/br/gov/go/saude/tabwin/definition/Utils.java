package br.gov.go.saude.tabwin.definition;

import com.google.common.collect.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class Utils {
    public static File resolveCaseInsensitivePath(File baseDir, String path) throws FileNotFoundException {
        String[] parts = path.split("[\\\\/]");

        File file = baseDir;
        for (String part : parts) {
            if (part.equals("..")) {
                file = file.getParentFile();
                continue;
            }

            File child = findChild(file, part);
            if (child == null)
                throw new FileNotFoundException(String.format("Couldn't find file %s in %s", path, baseDir));
            file = child;
        }

        return file;
    }

    public static File findChild(File file, String name) {
        return Arrays.stream(file.listFiles())
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static String stripComments(String line) {
        return line.replaceFirst(";.*$", "");
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static <K, V> Map<K, V> indexBy(Iterator<V> iterator, Function<? super V, K> keyExtractor) {
        return Maps.uniqueIndex(iterator, keyExtractor::apply);
    }

    public static <K, V> Multimap<K, V> indexMultimapBy(V[] array, Function<? super V, K> keyExtractor) {
        return indexMultimapBy(Arrays.stream(array), keyExtractor);
    }

    public static <K, V> Multimap<K, V> indexMultimapBy(Iterator<V> iterator, Function<? super V, K> keyExtractor) {
        return indexMultimapBy(Streams.stream(iterator), keyExtractor);
    }

    public static <K, V> Multimap<K, V> indexMultimapBy(Stream<V> stream, Function<? super V, K> keyExtractor) {
        Multimap<K, V> result = LinkedHashMultimap.create();
        stream.forEach(v -> result.put(keyExtractor.apply(v), v));
        return result;
    }

    public static <K, V> Map<K, V> indexBy(V[] array, Function<? super V, K> keyExtractor) {
        return indexBy(Iterators.forArray(array), keyExtractor);
    }

    public static <K, V> Map<K, V> indexBy(Stream<V> stream, Function<? super V, K> keyExtractor) {
        return indexBy(stream.iterator(), keyExtractor);
    }

}
