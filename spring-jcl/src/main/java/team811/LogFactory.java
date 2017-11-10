package team811;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;

import java.io.Serializable;
import java.util.logging.LogRecord;

/**
 * @create: 2017-11-08
 * @description: 日志创建工厂
 */
public abstract class LogFactory {

    static {
        ClassLoader cl = LogFactory.class.getClassLoader();
        try {
            /**
             * 尝试使用ExtendedLogger加载本类(尝试寻找该二进制class)
             */
            cl.loadClass("org.apache.logging.log4j.spi.ExtendedLogger");
            logApi = LogApi.LOG4J;
        } catch (ClassNotFoundException ex1) {
            // 当其所有父类都没有加载到该类或者该类不存在的时候抛出该异常
            // Keep java.util.logging as default
        }
    }

    /**
     * 日志类型(默认JavaUtilDelegate)
     */
    private static LogApi logApi = LogApi.JUL;

    /**
     * 根据日志名称为系统创建Log
     *
     * @param clazz 日志类class对象
     * @return Log
     */
    public static Log getLog(Class<?> clazz) {
        return getLog(clazz.getName());
    }

    /**
     * 根据日志名称为系统创建Log
     *
     * @param name 日志名称
     * @return Log
     */
    public static Log getLog(String name) {
        switch (logApi) {
            case LOG4J:
                return Log4jDelegate.createLog(name);
            default:
                return JavaUtilDelegate.createLog(name);
        }
    }

    /**
     * 日志类型
     */
    private enum LogApi {
        LOG4J, JUL
    }

    private static class Log4jDelegate {
        /**
         * Log4j日志对象创建
         */
        public static Log createLog(String name) {
            // 根据全类名创建Log对象
            return new Log4jLog(name);
        }
    }

    private static class Log4jLog implements Log, Serializable {

        /**
         * team811.LogFactory$Log4jLog 内部类全名
         */
        private static final String FQCN = Log4jLog.class.getName();

        /**
         * 包含日志配置的对象
         */
        private final ExtendedLogger logger;

        /**
         * 注册日志对象
         */
        private static final LoggerContext loggerContext = LogManager.getContext(Log4jLog.class.getClassLoader(), false);

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

    private static class JavaUtilDelegate {
        public static Log createLog(String name) {
            return new JavaUtilLog(name);
        }
    }

    private static class JavaUtilLog implements Log, Serializable {

        private String name;

        private transient java.util.logging.Logger logger;

        public JavaUtilLog(String name) {
            // Logger的名称
            this.name = name;
            // 为指定子系统查找或创建一个logger
            this.logger = java.util.logging.Logger.getLogger(name);
        }

        @Override
        public boolean isFatalEnabled() {
            return isErrorEnabled();
        }

        @Override
        public boolean isErrorEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.SEVERE);
        }

        @Override
        public boolean isWarnEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.WARNING);
        }

        @Override
        public boolean isInfoEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.INFO);
        }

        @Override
        public boolean isDebugEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.FINE);
        }

        @Override
        public boolean isTraceEnabled() {
            return this.logger.isLoggable(java.util.logging.Level.FINEST);
        }

        @Override
        public void fatal(Object message) {
            error(message);
        }

        @Override
        public void fatal(Object message, Throwable exception) {
            error(message, exception);
        }

        @Override
        public void error(Object message) {
            log(java.util.logging.Level.SEVERE, message, null);
        }

        @Override
        public void error(Object message, Throwable exception) {
            log(java.util.logging.Level.SEVERE, message, exception);
        }

        @Override
        public void warn(Object message) {
            log(java.util.logging.Level.WARNING, message, null);
        }

        @Override
        public void warn(Object message, Throwable exception) {
            log(java.util.logging.Level.WARNING, message, exception);
        }

        @Override
        public void info(Object message) {
            log(java.util.logging.Level.INFO, message, null);
        }

        @Override
        public void info(Object message, Throwable exception) {
            log(java.util.logging.Level.INFO, message, exception);
        }

        @Override
        public void debug(Object message) {
            log(java.util.logging.Level.FINE, message, null);
        }

        @Override
        public void debug(Object message, Throwable exception) {
            log(java.util.logging.Level.FINE, message, exception);
        }

        @Override
        public void trace(Object message) {
            log(java.util.logging.Level.FINEST, message, null);
        }

        @Override
        public void trace(Object message, Throwable exception) {
            log(java.util.logging.Level.FINEST, message, exception);
        }

        private void log(java.util.logging.Level level, Object message, Throwable exception) {
            if (logger.isLoggable(level)) {
                LogRecord rec;
                if (message instanceof LogRecord) {
                    rec = (LogRecord) message;
                } else {
                    rec = new LocationResolvingLogRecord(level, String.valueOf(message));
                    rec.setLoggerName(this.name);
                    rec.setResourceBundleName(logger.getResourceBundleName());
                    rec.setResourceBundle(logger.getResourceBundle());
                    rec.setThrown(exception);
                }
                logger.log(rec);
            }
        }
    }

    private static class LocationResolvingLogRecord extends LogRecord {

        /**
         * 类全名:team811.LogFactory$LocationResolvingLogRecord
         */
        private static final String FQCN = JavaUtilLog.class.getName();

        /**
         * 是否手动设置
         * <p>
         * volatile并发可见性(每次使用该变量的时候会重新去主内存中去读取该变量)
         */
        private volatile boolean resolved;

        public LocationResolvingLogRecord(java.util.logging.Level level, String msg) {
            super(level, msg);
        }

        /**
         * 如果sourceClassName没通过手动设置,
         * 则获取最后一次调用该类的类名
         * 否则返回设置类名
         *
         * @return String 类全名
         */
        @Override
        public String getSourceClassName() {
            if (!this.resolved) {
                resolve();
            }
            return super.getSourceClassName();
        }

        /**
         * 手动设置 sourceClassName
         *
         * @param sourceClassName 类全名
         */
        @Override
        public void setSourceClassName(String sourceClassName) {
            super.setSourceClassName(sourceClassName);
            this.resolved = true;
        }

        /**
         * 如果sourceMethodName没通过手动设置,
         * 则获取最后一次调用该类的类方法名
         * 否则返回设置类名
         *
         * @return String 方法名
         */
        @Override
        public String getSourceMethodName() {
            if (!this.resolved) {
                resolve();
            }
            return super.getSourceMethodName();
        }

        /**
         * 手动设置 sourceMethodName
         *
         * @param sourceMethodName 方法名
         */
        @Override
        public void setSourceMethodName(String sourceMethodName) {
            super.setSourceMethodName(sourceMethodName);
            this.resolved = true;
        }

        /**
         * 查找最后一次调用本方法的类和方法。
         */
        private void resolve() {
            // 获取当前线程的运行栈
            StackTraceElement[] stack = new Throwable().getStackTrace();
            String sourceClassName = null;
            String sourceMethodName = null;
            boolean found = false;
            for (StackTraceElement element : stack) {
                String className = element.getClassName();
                if (FQCN.equals(className)) {
                    found = true;
                } else if (found) {
                    sourceClassName = className;
                    sourceMethodName = element.getMethodName();
                    break;
                }
            }
            setSourceClassName(sourceClassName);
            setSourceMethodName(sourceMethodName);
        }
    }
}