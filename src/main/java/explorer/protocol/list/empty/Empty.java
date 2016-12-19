package explorer.protocol.list.empty;

import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.script.Script;

import explorer.db.ExplorerAsset;
import explorer.db.ExplorerHolder;
import explorer.protocol.IPlatformTestable;

public class Empty implements IPlatformTestable{
	private String script;

	@Override
	public Boolean init(Block block, Transaction tx){
		List<TransactionOutput> listOutputs = tx.getOutputs();
		
		for(TransactionOutput output : listOutputs){
			try{
				String tempString = (new Script(output.getScriptBytes())).toString();
				if(tempString.startsWith("RETURN") && !tempString.contains("[")){
					this.script = "";
					return true;
				}
			} catch(ScriptException e){}
		}

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