# OpReturnTool

A tool for extracting OP_RETURN metadata from the Bitcoin blockchain, by [Livio Pompianu](http://tcs.unica.it/members/livio-pompianu).  

An analysis performed by using the OpReturnTool is discussed in [An analysis of Bitcoin OP_RETURN metadata](https://arxiv.org/abs/1702.01024).
The paper was presented by Livio Pompianu at the [4th Workshop on Bitcoin and Blockchain Research](http://fc17.ifca.ai/bitcoin/). 

## Background ##
The Bitcoin protocol allows to save arbitrary data on the blockchain through a special instruction of the scripting language, 
called OP RETURN. A growing number of protocols exploit this feature to extend the range of applications of the Bitcoin 
blockchain beyond transfer of currency.

Usually, a protocol is identified by the first few bytes of metadata attached to the OP RETURN, 
the exact number of bytes may vary from protocol to protocol.

We searched the web for known associations between identifiers and protocols.  
The OpReturnTool accordingly classifies the OP_RETURN transactions (stored on our local 
copy of the blockchain) that begin with one of the identifiers discovered.

OpReturnTool is a Java tool exploiting the [BitcoinJ APIs](https://bitcoinj.github.io/).

## Install prerequisites ##
1. [Bitcoin Core](https://bitcoin.org/en/bitcoin-core/) will provide you a copy of the Bitcoin blockchain.
2. [Eclipse](https://eclipse.org/)
3. [MySQL](https://www.mysql.com/)

## Install OpReturnTool ##
1. Clone the [OpReturnTool](https://github.com/BitcoinOpReturn/OpReturnTool) repository.
2. Eclipse: import the project as Existing Maven Project.
3. MySQL: create a new database called "explorer"
4. MySQL: import all the tables of the [OpReturnData](https://github.com/BitcoinOpReturn/OpReturnData) repository.

## First Configuration ##
You must either edit the file [global.properties](https://github.com/BitcoinOpReturn/OpReturnTool/blob/master/src/main/resources/global.properties) 
or create a file local.properties containing the same parameters. Provides the following information:
- dbname=explorer
- dbuser= name of the SQL user authorized to store data in this database.
- dbpassword= password of the user.

The other parameters listed on the global.properties file will be discussed in the next section.

## Input parameters ##
OpReturnTool inspects the blockchains by reading consecutive chunks of blocks: each chunk is analysed by a different thread.
Chunks are defined in the [input.txt](https://github.com/BitcoinOpReturn/OpReturnTool/blob/master/src/main/resources/input.txt) 
file as a couple of values: the hash of the the last block of the chunk (that will NOT be analysed by the current thread) 
and the hash of first block of the chunk.

You can choose which chunks inspect in the next execution of the tool by editing the last 
parameters of the previous properties file. For instance, by setting the following parameters:

- inputFile=input.txt
- firstChunk=0
- lastChunk=3

the tool will produce 4 threads that respectively inspect:

- from block 0 to 10000 (excluded)
- from block 10000 to 20000 (excluded)
- from block 20000 to 30000 (excluded)
- from block 30000 to 40000 (excluded)

For each supported protocol it exists a specific SQL table in which the tool
will save the related op_return transactions found. The 'block' table contains
data about each block containing at least one OP_RETURN transaction.

## Run ##
Run the tool by executing the MainEngine.java file.