import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;


public class Q4Test {
    static Path data_folder = Paths.get("src", "Q4", "data/");


    @Test(timeout = 1000)
    public void Q4a_1_read_simple() {
        Map<String, Integer> populations = LocationLoader.loadPopulationInfo(new File(data_folder.resolve("a_flat_easy.xml").toUri()));
        assertEquals(Map.of("Western Australia", 3008700, "New South Wales", 8545100, "Victoria", 7011100, "Tasmania", 575800, "Australian Capital Territory", 481700, "Queensland", 5618800, "South Australia", 1891700), populations);
    }

    @Test(timeout = 1000)
    public void Q4b_1_top_location() {
        File f = new File(data_folder.resolve("b_tree_basic.xml").toUri());
        Set<Location> locs = LocationLoader.loadLocations(f);

        assertNotNull(locs);
        assertEquals(locs.size(), 1);
        Location au = find(locs, "Country", "Australia");
        assertEquals(Integer.valueOf(26_000_000), au.population);
    }

    @Test(timeout = 1000)
    public void Q4b_2_hierarchies_exist() {
        File f = new File(data_folder.resolve("b_tree_basic.xml").toUri());
        Set<Location> locs = LocationLoader.loadLocations(f);

        assertNotNull(locs);

        assertPathExists(locs, new String[][]{
            {"Country","Australia"},
            {"State","Tasmania"},
            {"Region","South"},
            {"City","Hobart"}
        });
        assertPathExists(locs, new String[][]{
            {"Country","Australia"},
            {"State","Tasmania"},
            {"Region","North"},
            {"City","Launceston"}
        });


        assertPathExists(locs, new String[][]{
            {"Country","Australia"},
            {"State","New South Wales"},
            {"Region","Greater Sydney"},
            {"City","Sydney"}
        });
        assertPathExists(locs, new String[][]{
            {"Country","Australia"},
            {"State","New South Wales"},
            {"Region","Greater Sydney"},
            {"City","Parramatta"}
        });
        assertPathExists(locs, new String[][]{
            {"Country","Australia"},
            {"State","New South Wales"},
            {"Region","Greater Sydney"},
            {"City","Liverpool"}
        });

    }


    private static Location find(Set<Location> locs, String type, String name) {
        for (Location c : locs) {
            if (type.equals(c.type) && name.equals(c.name)) return c;
            Location got = find(c.children, type, name);
            if (got != null) return got;
        }
        return null;
    }

    private static void assertPathExists(Set<Location> locs, String[][] path) {
        for (String[] step : path) {
            String type = step[0];
            String name = step[1];
            var cur = find(locs, type, name);
            assertNotNull("Missing node " + type + ":" + name, cur);
        }
    }

}
