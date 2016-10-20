package ng.kingsley.android.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author ADIO Kingsley O.
 * @since 20 Oct, 2016
 */

public class CacheStoreTest {
    private static File cacheDir = new File("test_dir");
    private static CacheStore cacheStore;

    @BeforeClass
    public static void setup() {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
            System.out.println("Created test cache dir at: " + cacheDir.getAbsolutePath());
        }
        CacheParams params = new CacheParams(1, cacheDir, 1024 * 1024L);
        cacheStore = new CacheStore(params, new Gson());
    }

    @Test
    public void testCacheContainsKey() {
        String key = "test_key";
        assertFalse(cacheStore.containsKey(key));

        cacheStore.putString(key, "simple_value");
        assertTrue(cacheStore.containsKey(key));
        cacheStore.remove(key);
        assertFalse(cacheStore.containsKey(key));

        cacheStore.put(key, new SimpleEntry(1, "Some name", new ArrayList<String>()), SimpleEntry.class);
        assertTrue(cacheStore.containsKey(key));
        cacheStore.remove(key);
        assertFalse(cacheStore.containsKey(key));
    }

    @Test
    public void testCachePut() {
        String key = "test_key";
        assertTrue(cacheStore.putString(key, "simple_value"));

        String key2 = "test_key_2";
        SimpleEntry entry = new SimpleEntry(1, "Appcommons", null);
        assertTrue(cacheStore.put(key2, entry, SimpleEntry.class));

        String key3 = "test_key_3";
        assertTrue(cacheStore.putByteArray(key3, new byte[]{(byte) 1, (byte) 255, (byte) 128}));

        String key4 = "test_key_4";
        assertTrue(cacheStore.put(key4, new LinkedList<SimpleEntry>(), new TypeToken<LinkedList<SimpleEntry>>() {
        }));

        String key5 = "test_key_5";
        InputStream stream = new ByteArrayInputStream("Kingsley".getBytes());
        assertTrue(cacheStore.putInputStream(key5, stream));

        cacheStore.remove(key);
        cacheStore.remove(key2);
        cacheStore.remove(key3);
        cacheStore.remove(key4);
        cacheStore.remove(key5);
    }

    @Test
    public void testCacheGet() throws IOException {
        String key = "test_key";
        cacheStore.putString(key, "simple_value");
        assertEquals("simple_value", cacheStore.getString(key));
        cacheStore.remove(key);

        String key2 = "test_key_2";
        List<String> comments = new ArrayList<>();
        comments.add("Kingsley");
        comments.add("Test");
        comments.add("Library");
        comments.add("Another name");
        comments.add("Final");
        SimpleEntry simpleEntry = new SimpleEntry(1, "King", comments);
        cacheStore.put(key2, simpleEntry, SimpleEntry.class);
        SimpleEntry resultSimpleEntry = cacheStore.get(key2, SimpleEntry.class);
        assertEquals(simpleEntry, resultSimpleEntry);

        String key3 = "test_key_3";
        Converter<SimpleEntry> simpleEntryConverter = new SimpleEntryConverter();
        cacheStore.put(key3, simpleEntry, simpleEntryConverter);
        SimpleEntry converterSimpleEntry = cacheStore.get(key3, simpleEntryConverter);
        assertEquals(simpleEntry, converterSimpleEntry);
        assertEquals(resultSimpleEntry, converterSimpleEntry);

        String key4 = "test_key_4";
        byte[] bytes = {(byte) 1, (byte) 255, (byte) 128};
        cacheStore.putByteArray(key4, bytes);
        byte[] resultBytes = cacheStore.getByteArray(key4);
        assertArrayEquals(bytes, resultBytes);

        String key5 = "test_key_5";
        cacheStore.put(key5, comments, new TypeToken<List<String>>() {
        });
        List<String> resultComments = cacheStore.get(key5, new TypeToken<List<String>>() {
        });
        assertEquals(comments, resultComments);

        String key6 = "test_key_6";
        InputStream stream = new ByteArrayInputStream("Kingsley".getBytes("UTF-8"));
        cacheStore.putInputStream(key6, stream);
        InputStream resultStream = cacheStore.getInputStream(key6);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DefaultConverters.moveStream(resultStream, baos);
        assertEquals("Kingsley", baos.toString("UTF-8"));

        cacheStore.remove(key2);
        cacheStore.remove(key3);
        cacheStore.remove(key4);
        cacheStore.remove(key5);
        cacheStore.remove(key6);
    }

    @Test
    public void testCacheRemove() {
        String key = "test_key";
        cacheStore.putString(key, "simple_value");
        String key2 = "test_key_2";
        SimpleEntry entry = new SimpleEntry(1, "Appcommons", null);
        cacheStore.put(key2, entry, SimpleEntry.class);

        assertTrue(cacheStore.remove(key));
        assertTrue(cacheStore.remove(key2));
    }

    @AfterClass
    public static void destroy() {
        try {
            cacheStore.clear();
            cacheStore.internalCache().delete();
            cacheDir.delete();
            System.out.println("Cleaned up test cache dir at: " + cacheDir.getAbsolutePath());
        } catch (IOException ignored) {
        }
    }

    static class SimpleEntry {
        final int id;
        final String name;
        final List<String> comments;

        SimpleEntry(int id, String name, List<String> comments) {
            this.id = id;
            this.name = name;
            this.comments = comments;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleEntry)) return false;

            SimpleEntry that = (SimpleEntry) o;

            if (id != that.id) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            return comments != null ? comments.equals(that.comments) : that.comments == null;

        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (comments != null ? comments.hashCode() : 0);
            return result;
        }
    }

    static class SimpleEntryConverter implements Converter<SimpleEntry> {

        @Override
        public void writeToStream(OutputStream stream, SimpleEntry content) throws IOException {
            DataOutputStream dos = new DataOutputStream(stream);
            dos.writeInt(content.id);
            if (content.name == null) {
                dos.writeInt(-1);
            } else {
                dos.writeInt(1);
                dos.writeUTF(content.name);
            }
            if (content.comments == null) {
                dos.writeInt(-1);
            } else {
                dos.writeInt(1);
                dos.writeInt(content.comments.size());
                for (String comment : content.comments) {
                    dos.writeUTF(comment);
                }
            }
        }

        @Override
        public SimpleEntry readFromStream(InputStream stream) throws IOException {
            DataInputStream dis = new DataInputStream(stream);
            int id = dis.readInt();
            String name = (dis.readInt() == 1) ? dis.readUTF() : null;
            List<String> comments = (dis.readInt() == 1) ? new ArrayList<String>() : null;
            if (comments != null) {
                int size = dis.readInt();
                for (int i = 0; i < size; i++) {
                    comments.add(dis.readUTF());
                }
            }

            return new SimpleEntry(id, name, comments);
        }
    }

}
