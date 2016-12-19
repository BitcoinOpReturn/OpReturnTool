package explorer.protocol;

import java.util.List;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Transaction;

import explorer.db.ExplorerAsset;
import explorer.db.ExplorerHolder;

public interface IPlatformTestable {	
	/** Initializes data processing transaction. If the transaction belongs to the current protocol, the method returns true, otherwise false. */
	Boolean init(Block block, Transaction tx);
	
	/** Return the OP_RETURN script. */
	String getOpReturnBytes();

	/** Returns a list of assets sent/received via the current transaction. */
	List<ExplorerAsset> getAssets();
	
	/** Returns a list of holders who send/receive assets via the current transaction. */
	List<ExplorerHolder> getHolders();
}
