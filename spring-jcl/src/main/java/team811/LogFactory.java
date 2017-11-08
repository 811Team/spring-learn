package team811;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;

import java.io.Serializable;

/**
 * @create: 2017-11-08
 * @description:
 */
public abstract class LogFactory {

    /**
     * 日志类型
     */
    private static LogApi logApi = LogApi.JUL;

    public static Log getLog(Class<?> clazz) {
        return getLog(clazz.getName());
    }

    /**
     * 获取日志
     *
     * @param name
     * @return
     */
    public static Log getLog(String name) {
        switch (logApi) {
            case LOG4J:
                return Log4jDelegate.createLog(name);
            case SLF4J_LAL:
                return Slf4jDelegate.createLocationAwareLog(name);
            case SLF4J:
                return Slf4jDelegate.createLog(name);
            default:
                return JavaUtilDelegate.createLog(name);
        }
    }

    /**
     * 日志类型
     */
    private enum LogApi {
        LOG4J, SLF4J_LAL, SLF4J, JUL
    }

    private static class Log4jDelegate {
        public static Log createLog(String name) {
            return new Log4jLog(name);
        }
    }

    private static class Log4jLog implements Log, Serializable {

        /**
         * 全类名
         */
        private static final String FQCN = Log4jLog.class.getName();

        private final ExtendedLogger logger;

        /**
         * LoggerContext负责Log4j的配置
         */
        private static final LoggerContext loggerContext =
                LogManager.getContext(Log4jLog.class.getClassLoader(), false);

        public Log4jLog(String name) {
            this.logger = loggerContext.getLogger(name);
        }

        @Override
        public boolean isFatalEnabled() {
            return logger.isEnabled(Level.FATAL, null, null);
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.isEnabled(Level.ERROR, null, null);
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.isEnabled(Level.WARN, null, null);
        }

        @Override
        public boolean isInfoEnabled() {
            return logger.isEnabled(Level.INFO, null, null);
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.isEnabled(Level.DEBUG, null, null);
        }

        @Override
        public boolean isTraceEnabled() {
            return logger.isEnabled(Level.TRACE, null, null);
        }

        @Override
        public void fatal(Object message) {
            logger.logIfEnabled(FQCN, Level.FATAL, null, message, null);
        }

        @Override
        public void fatal(Object message, Throwable exception) {
            logger.logIfEnabled(FQCN, Level.FATAL, null, message, exception);
        }

        @Override
        public void error(Object message) {
            logger.logIfEnabled(FQCN, Level.ERROR, null, message, null);
        }

        @Override
        public void error(Object message, Throwable exception) {
            logger.logIfEnabled(FQCN, Level.ERROR, null, message, exception);
        }

        @Override
        public void warn(Object message) {
            logger.logIfEnabled(FQCN, Level.WARN, null, message, null);
        }

        @Override
        public void warn(Object message, Throwable exception) {
            logger.logIfEnabled(FQCN, Level.WARN, null, message, exception);
        }

        @Override
        public void info(Object message) {
            logger.logIfEnabled(FQCN, Level.INFO, null, message, null);
        }

        @Override
        public void info(Object message, Throwable exception) {
            logger.logIfEnabled(FQCN, Level.INFO, null, message, exception);
        }

        @Override
        public void debug(Object message) {
            logger.logIfEnabled(FQCN, Level.DEBUG, null, message, null);
        }

        @Override
        public void debug(Object message, Throwable exception) {
            logger.logIfEnabled(FQCN, Level.DEBUG, null, message, exception);
        }

        @Override
        public void trace(Object message) {
            logger.logIfEnabled(FQCN, Level.TRACE, null, message, null);
        }

        @Override
        public void trace(Object message, Throwable exception) {
            logger.logIfEnabled(FQCN, Level.TRACE, null, message, exception);
        }
    }
}