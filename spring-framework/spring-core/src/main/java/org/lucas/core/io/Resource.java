package org.lucas.core.io;

import org.lucas.lang.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @create: 2018-01-26
 * @description:
 */
public interface Resource extends InputStreamSource {
    /**
     * 确定这个资源是否存在
     */
    boolean exists();

    /**
     * 该内容是否能读取
     */
    default boolean isReadable() {
        return true;
    }

    /**
     * 该资源是否被打开
     */
    default boolean isOpen() {
        return false;
    }

    /**
     * 该资源在文件系统中是否属于一个文件
     *
     * @return 默认返回 {code false}
     */
    default boolean isFile() {
        return false;
    }

    /**
     * 返回该资源的 URL
     *
     * @throws IOException
     */
    URL getURL() throws IOException;

    /**
     * 返回该资源的 URI
     *
     * @throws IOException
     */
    URI getURI() throws IOException;

    /**
     * 通过资源返回文件
     *
     * @throws IOException
     */
    File getFile() throws IOException;

    /**
     * 通过指定 {@code InputStream} 构建字节读取通道
     *
     * @return {@code ReadableByteChannel} 字节读取通道
     * @throws IOException
     */
    default ReadableByteChannel readableChannel() throws IOException {
        return Channels.newChannel(getInputStream());
    }

    /**
     * 返回内容的长度
     *
     * @throws IOException
     */
    long contentLength() throws IOException;

    /**
     * 资源最后修改时间戳
     *
     * @throws IOException
     */
    long lastModified() throws IOException;

    /**
     * 创建资源的 {@code Resource} 对象
     *
     * @param relativePath 资源路径
     * @throws IOException
     */
    Resource createRelative(String relativePath) throws IOException;

    /**
     * 获取资源的名字
     */
    @Nullable
    String getFilename();

    /**
     * 返回该文件的描述
     */
    String getDescription();

}
