package team811.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public interface InputStreamSource {
    /**
     * 获取流
     *
     * @return {@code InputStream}
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;
}
