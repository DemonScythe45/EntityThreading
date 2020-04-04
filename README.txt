# EntityThreading
This is a minecraft 1.12.2 forge coremod for threading the mob entity tick in the game

Currently uses 12 threads in a thread pool and groups entities by chunk.
One chunks worth of entities is given to any thread at one time.
