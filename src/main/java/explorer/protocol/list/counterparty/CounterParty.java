package explorer.protocol.list.counterparty;

import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Transaction;

import explorer.db.ExplorerAsset;
import explorer.db.ExplorerHolder;
import explorer.protocol.IPlatformTestable;

public class CounterParty implements IPlatformTestable{
	private String script;

	@Override
	public Boolean init(Block block, Transaction tx){
		return false;
	}

	@Override
	public String getOpReturnBytes(){
		return script;
	}

	@Override
	public List<ExplorerAsset> getAssets() {
		List<ExplorerAsset> empty = new ArrayList<ExplorerAsset>();
		return empty;
	}

	@Override
	public List<ExplorerHolder> getHolders() {
		List<ExplorerHolder> empty = new ArrayList<ExplorerHolder>();
		return empty;
	}
}