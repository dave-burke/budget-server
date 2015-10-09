import static ch.qos.logback.classic.Level.*
import static ch.qos.logback.core.spi.FilterReply.*
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.status.OnConsoleStatusListener
import ch.qos.logback.classic.turbo.MarkerFilter
import ch.qos.logback.core.ConsoleAppender

statusListener(OnConsoleStatusListener)

def logPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger{0}: %msg%n"

def logLevel = DEBUG
def appenders = []

appender("CONSOLE", ConsoleAppender){
	encoder(PatternLayoutEncoder){
		pattern = logPattern
	}
}
	
appenders = ["CONSOLE"]

logger("org", WARN)
//logger("org.springframework.web.servlet.DispatcherServlet", DEBUG) //Show incoming URL (without querystring)
//logger("org.springframework.web.method.HandlerMethod", TRACE) //Show controller invocation with all arguments (including querystring)
//logger("org.hibernate.SQL", DEBUG) //Show SQL queries
//logger("org.hibernate.type", TRACE) //Show SQL parameters
root(logLevel, appenders)

