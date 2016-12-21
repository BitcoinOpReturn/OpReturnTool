package explorer.engine;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.MainNetParams;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import explorer.protocol.Protocol;


public class MainEngine {
	private final int THREADS;
	private ExecutorService exec;
	private Sha256Hash[] firstBlock;
	private Sha256Hash[] lastBlock;
	HikariConfig config;

	public MainEngine() throws Exception{
		String inputFile; 
		Protocol protocol; 
		int firstChunk; 
		int lastChunk;
		String dbName;
		String dbUser;
		String dbPassword;
		
		Properties prop = new Properties();
		try(InputStream local = MainEngine.class.getClassLoader().getResourceAsStream("local.properties")){
			// load local properties file
			prop.load(local);

		} catch(IOException e){
			InputStream global = MainEngine.class.getClassLoader().getResourceAsStream("global.properties");
			// load global properties file
			prop.load(global);
		}
		
		dbName = prop.getProperty("dbname");
		dbUser = prop.getProperty("dbuser");
		dbPassword = prop.getProperty("dbpassword");
		firstChunk = Integer.valueOf(prop.getProperty("firstChunk"));
		lastChunk = Integer.valueOf(prop.getProperty("lastChunk"));
		protocol = Protocol.getProtocol(prop.getProperty("protocol"));
		inputFile = prop.getProperty("inputFile");
		
		THREADS = lastChunk-firstChunk+1;							// Setting number of workers
		this.exec = Executors.newFixedThreadPool(THREADS);			// Initializing thread executor service
		this.firstBlock = new Sha256Hash[THREADS];					// For each thread indicates the first (younger) block to explore
		this.lastBlock = new Sha256Hash[THREADS];					// For each thread indicates the last (older) block to explore

		this.config = new HikariConfig();
	    config.setMaximumPoolSize(100);
	    config.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    config.setJdbcUrl("jdbc:mysql://localhost:3306/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false");
		config.addDataSourceProperty("databaseName", dbName);
		config.addDataSourceProperty("user", dbUser);
		config.addDataSourceProperty("password", dbPassword);

		// Open input file (containing chunks) and set first block and last block for each thread.
		try(Scanner s = new Scanner(MainEngine.class.getClassLoader().getResourceAsStream(inputFile))){
			int indexFile = 0;
			int indexThread = 0;

			// Skip chunks not requested
			while(indexFile < firstChunk){
				s.nextLine();
				indexFile++;
			}

			// Load data for requested chunks
			while(indexFile <= lastChunk){
				this.firstBlock[indexThread] = new Sha256Hash(s.next());
				this.lastBlock[indexThread] = new Sha256Hash(s.next());
				s.nextLine();
				indexFile++;
				indexThread ++;
			}

			try(HikariDataSource ds = new HikariDataSource(config)){
				// Creating workers
				for(int i=0; i<THREADS; i++){
					exec.execute(new Engine(MainNetParams.get(), ds, i, protocol, firstBlock[i], lastBlock[i]));		
				}

				// Waiting threads
				exec.shutdown();
				while(! exec.awaitTermination(1, TimeUnit.MINUTES))
					System.out.println("Waiting another minute");
			
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception{
		
		System.out.println("Starting Analysis: " + new Timestamp(new java.util.Date().getTime()));
		
		MainEngine e = new MainEngine();
		
		System.out.println("Analysis concluded: " + new Timestamp(new java.util.Date().getTime()));
	}
}