package explorer.protocol;


public enum Protocol {
	COLU,
	COINSPARK,
	OPENASSETS,
	OMNI,
	FACTOM,
	MONEGRAPH,
	STAMPERY,
	ETERNITYWALL,
	BLOCKSTORE,
	CRYPTOCOPYRIGHT,
	STAMPD,
	TRADLE,
	ORIGINALMY,
	ASCRIBE,
	BLOCKSIGN,
	PROOFOFEXISTENCE,
	BITPROOF,
	PROVEBIT,
	REMEMBR,
	SMARTBIT,
	OPENBAZZAR,
	COUNTERPARTY,
	BLOCKAI,
	NICOSIA,
	BITPOS,
	LAPREUVE,
	EMPTY,
	UNKNOWN,
	ASSETS,
	DOCUMENT,
	MEDIAMARKET,
	OTHER,
	ALL;
	
	public static Protocol getPlatform(String platform) throws UnsupportedProtocolException{
		if(platform.toLowerCase().equals("colu")) return COLU;
		if(platform.toLowerCase().equals("coinspark")) return COINSPARK;
		if(platform.toLowerCase().equals("openassets")) return OPENASSETS;
		if(platform.toLowerCase().equals("omni")) return OMNI;
		if(platform.toLowerCase().equals("factom")) return FACTOM;
		if(platform.toLowerCase().equals("monegraph")) return MONEGRAPH;
		if(platform.toLowerCase().equals("stampery")) return STAMPERY;
		if(platform.toLowerCase().equals("eternitywall")) return ETERNITYWALL;
		if(platform.toLowerCase().equals("blockstore")) return BLOCKSTORE;
		if(platform.toLowerCase().equals("cryptocopyright")) return CRYPTOCOPYRIGHT;
		if(platform.toLowerCase().equals("stampd")) return STAMPD;
		if(platform.toLowerCase().equals("tradle")) return TRADLE;
		if(platform.toLowerCase().equals("originalmy")) return ORIGINALMY;
		if(platform.toLowerCase().equals("ascribe")) return ASCRIBE;
		if(platform.toLowerCase().equals("blocksign")) return BLOCKSIGN;
		if(platform.toLowerCase().equals("proofofexistence")) return PROOFOFEXISTENCE;
		if(platform.toLowerCase().equals("bitproof")) return BITPROOF;
		if(platform.toLowerCase().equals("provebit")) return PROVEBIT;
		if(platform.toLowerCase().equals("remembr")) return REMEMBR;
		if(platform.toLowerCase().equals("smartbit")) return SMARTBIT;
		if(platform.toLowerCase().equals("openbazzar")) return OPENBAZZAR;
		if(platform.toLowerCase().equals("counterparty")) return COUNTERPARTY;
		if(platform.toLowerCase().equals("blockai")) return BLOCKAI;
		if(platform.toLowerCase().equals("nicosia")) return NICOSIA;
		if(platform.toLowerCase().equals("bitpos")) return BITPOS;
		if(platform.toLowerCase().equals("lapreuve")) return LAPREUVE;
		if(platform.toLowerCase().equals("empty")) return EMPTY;
		if(platform.toLowerCase().equals("unknown")) return UNKNOWN;
		if(platform.toLowerCase().equals("assets")) return ASSETS;
		if(platform.toLowerCase().equals("document")) return DOCUMENT;
		if(platform.toLowerCase().equals("mediamarket")) return MEDIAMARKET;
		if(platform.toLowerCase().equals("other")) return OTHER;
		if(platform.toLowerCase().equals("all")) return ALL;
		throw new UnsupportedProtocolException("The requested platform is not supported");
	}
}