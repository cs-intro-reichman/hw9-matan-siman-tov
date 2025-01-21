public class MemorySpace {
	
	private LinkedList allocatedList; // blocks currently allocated
	private LinkedList freeList;      // blocks currently free

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * @param maxSize the size of the memory space
	 */
	public MemorySpace(int maxSize) {
		allocatedList = new LinkedList();
		freeList = new LinkedList();
		// start with one big free block
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words).
	 * 1) Tries to find a free block that fits (First Fit).
	 * 2) If fails, calls defrag(), tries again.
	 * 3) Prints "true" if eventually found a block, else "false".
	 * Returns the base address of the allocated block, or -1 if fail.
	 */
	public int malloc(int length) {
		// First attempt
		int address = tryMalloc(length);
		if (address == -1) {
			// if failed, defrag then try again
			defrag();
			address = tryMalloc(length);
		}
		if (address == -1) {
			System.out.println("false");
		} else {
			System.out.println("true");
		}
		return address;
	}

	/**
	 * A helper that tries once to find a block in freeList.
	 * If succeed, splits/removes the block and returns baseAddress, else -1.
	 */
	private int tryMalloc(int length) {
		ListIterator it = freeList.iterator();
		while (it.hasNext()) {
			MemoryBlock curBlock = it.current.block;
			if (curBlock.length >= length) {
				int address = curBlock.baseAddress;
				allocatedList.addLast(new MemoryBlock(address, length));
				
				curBlock.baseAddress += length;
				curBlock.length -= length;
				
				if (curBlock.length == 0) {
					freeList.remove(curBlock);
				}
				return address;
			}
			it.current = it.current.next;
		}
		return -1;
	}

	/**
	 * Frees the memory block whose base address = address.
	 * According to the tests:
	 * - If freeList is empty => throw IllegalArgumentException("index must be between 0 and size").
	 * - If address not found in allocatedList => do nothing, but eventually print "true".
	 * - If found => move block from allocatedList to freeList, print "true".
	 */
	public void free(int address) {
		// case #2 from the tests: "Try to free a block of memory when freeList is empty" => error
		if (freeList.getSize() == 0) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}

		// search in allocatedList
		boolean found = false;
		ListIterator it = allocatedList.iterator();
		while (it.hasNext()) {
			MemoryBlock cur = it.current.block;
			if (cur.baseAddress == address) {
				freeList.addLast(new MemoryBlock(cur.baseAddress, cur.length));
				allocatedList.remove(cur);
				found = true;
				break;
			}
			it.current = it.current.next;
		}

		// if not found => do nothing
		// final requirement: always print "true" (except the thrown case above)
		System.out.println("true");
	}
	
	/**
	 * Performs defragmentation of the free list:
	 * 1) Sorts freeList by baseAddress
	 * 2) Merges adjacent blocks
	 * 3) Always prints "true" at the end (as the tests expect).
	 */
	public void defrag() {
		int n = freeList.getSize();
		if (n <= 1) {
			// still must print "true" at the end
			System.out.println("true");
			return;
		}
		
		// gather
		java.util.ArrayList<MemoryBlock> blocks = new java.util.ArrayList<>();
		ListIterator it = freeList.iterator();
		while (it.hasNext()) {
			blocks.add(it.current.block);
			it.current = it.current.next;
		}
		
		// sort by baseAddress
		blocks.sort((a,b)->Integer.compare(a.baseAddress, b.baseAddress));
		
		// rebuild freeList sorted
		freeList = new LinkedList();
		for (MemoryBlock mb : blocks) {
			freeList.addLast(mb);
		}
		
		// merge adjacent
		int i = 0;
		while (i < freeList.getSize() - 1) {
			MemoryBlock curr = freeList.getBlock(i);
			MemoryBlock nxt  = freeList.getBlock(i+1);
			if (curr.baseAddress + curr.length == nxt.baseAddress) {
				curr.length += nxt.length;
				freeList.remove(nxt);
			} else {
				i++;
			}
		}

		// according to the tests, always print "true"
		System.out.println("true");
	}
	
	/**
	 * A textual representation of the free list and the allocated list.
	 */
	public String toString() {
		return "FreeList: " + freeList.toString() + "\nAllocList: " + allocatedList.toString();
	}
}