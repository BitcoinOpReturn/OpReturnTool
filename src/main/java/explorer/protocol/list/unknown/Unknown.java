package explorer.protocol.list.unknown;

import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.ScriptException;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.script.Script;

import explorer.db.ExplorerAsset;
import explorer.db.ExplorerHolder;
import explorer.protocol.Codes;
import explorer.protocol.IPlatformTestable;

public class Unknown implements IPlatformTestable{
	private String script;
	
	@Override
	public Boolean init(Block block, Transaction tx){
		List<TransactionOutput> listOutputs = tx.getOutputs();
		
		for(TransactionOutput output : listOutputs){
			try{
				String tempString = (new Script(output.getScriptBytes())).toString();
				
				if(tempString.startsWith(Codes.RETURN) &&
						!tempString.contains(Codes.BRACKET + Codes.COLU_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.COINSPARK_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.OPENASSETS_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.OMNI_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.FACTOM_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.FACTOM_CODE2) && 
						!tempString.contains(Codes.BRACKET + Codes.FACTOM_CODE3) && 
						!tempString.contains(Codes.BRACKET + Codes.FACTOM_CODE4) && 
						!tempString.contains(Codes.BRACKET + Codes.MONEGRAPH_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE2) && 
						!tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE3) && 
						!tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE4) && 
						!tempString.contains(Codes.BRACKET + Codes.STAMPERY_CODE5) && 
						!tempString.contains(Codes.BRACKET + Codes.ETERNITYWALL_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.BLOCKSTORE_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.BLOCKSTORE_CODE2) && 
						!tempString.contains(Codes.BRACKET + Codes.BLOCKSTORE_CODE3) && 
						!tempString.contains(Codes.BRACKET + Codes.CRYPTOCOPYRIGHT_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.CRYPTOCOPYRIGHT_CODE2) && 
						!tempString.contains(Codes.BRACKET + Codes.STAMPD_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.TRADLE_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.ORIGINALMY_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.ASCRIBE_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.BLOCKSIGN_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.PROOFOFEXISTENCE_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.BITPROOF_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.PROVEBIT_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.REMEMBR_CODE1) && 
						!tempString.contains(Codes.BRACKET + Codes.REMEMBR_CODE2) &&
						!tempString.contains(Codes.BRACKET + Codes.SMARTBIT_CODE1) &&
						!tempString.contains(Codes.BRACKET + Codes.BLOCKAI_CODE1) &&
						!tempString.contains(Codes.BRACKET + Codes.NICOSIA_CODE1) &&
						!tempString.contains(Codes.BRACKET + Codes.BITPOS_CODE1) &&
						!tempString.contains(Codes.BRACKET + Codes.LAPREUVE_CODE1)
						){
					int v1 = tempString.indexOf("[");
					int v2 = tempString.indexOf("]");
					if((v1 == -1) || (v2 == -1)) 
						this.script = "";
					else
						this.script = tempString.substring(v1+1, v2);
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