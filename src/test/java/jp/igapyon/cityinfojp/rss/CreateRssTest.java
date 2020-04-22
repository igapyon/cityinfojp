package jp.igapyon.cityinfojp.rss;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CreateRssTest {

    @Test
    void test() throws IOException {
        MyRssGenerator.main(new String[] { "this" });
    }
}
