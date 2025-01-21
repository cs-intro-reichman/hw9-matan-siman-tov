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
	 * Prints "true" if succeeded, "false" if failed.
	 * Returns the base address, or -1 if fail.
	 */
	public int malloc(int length) {
		int address = -1;

		// חיפוש בלוק מתאים ב-freeList
		ListIterator it = freeList.iterator();
		while (it.hasNext()) {
			MemoryBlock curBlock = it.current.block;
			if (curBlock.length >= length) {
				address = curBlock.baseAddress;
				allocatedList.addLast(new MemoryBlock(address, length));
				
				// עדכון הבלוק החופשי
				curBlock.baseAddress += length;
				curBlock.length -= length;
				
				// אם הבלוק ב-freeList התרוקן (length נהיה 0)
				if (curBlock.length == 0) {
					freeList.remove(curBlock);
				}
				break;
			}
			it.current = it.current.next;
		}

		if (address != -1) {
			// הצלחה
			System.out.println("true");
		} else {
			// כישלון
			System.out.println("false");
		}
		return address;
	}

	/**
	 * Frees the memory block whose base address = address.
	 * Always prints "true", even if address not found.
	 */
	public void free(int address) {
		ListIterator it = allocatedList.iterator();
		while (it.hasNext()) {
			MemoryBlock cur = it.current.block;
			if (cur.baseAddress == address) {
				// משחררים
				freeList.addLast(new MemoryBlock(cur.baseAddress, cur.length));
				allocatedList.remove(cur);
				break;
			}
			it.current = it.current.next;
		}
		// לפי הטסטים: לא זורקים חריגה, תמיד מדפיסים "true"
		System.out.println("true");
	}
	
	/**
	 * Performs defragmentation of the free list.
	 * Always prints "true" at the end.
	 */
	public void defrag() {
		// אם יש 0 או 1 בלוקים חופשיים - אין מה למזג
		if (freeList.getSize() <= 1) {
			System.out.println("true");
			return;
		}
		
		// שליפת כל הבלוקים
		java.util.ArrayList<MemoryBlock> blocks = new java.util.ArrayList<>();
		ListIterator it = freeList.iterator();
		while (it.hasNext()) {
			blocks.add(it.current.block);
			it.current = it.current.next;
		}
		
		// מיון לפי baseAddress
		blocks.sort((a, b) -> Integer.compare(a.baseAddress, b.baseAddress));
		
		// בניית freeList מחדש
		freeList = new LinkedList();
		for (MemoryBlock mb : blocks) {
			freeList.addLast(mb);
		}
		
		// מיזוג רציפים
		int i = 0;
		while (i < freeList.getSize() - 1) {
			MemoryBlock current = freeList.getBlock(i);
			MemoryBlock next = freeList.getBlock(i + 1);
			if (current.baseAddress + current.length == next.baseAddress) {
				// מיזוג
				current.length += next.length;
				freeList.remove(next);
			} else {
				i++;
			}
		}

		// לפי הטסטים: בסוף defrag תמיד "true"
		System.out.println("true");
	}
	
	/**
	 * A textual representation of the free list and the allocated list.
	 */
	public String toString() {
		return "FreeList: " + freeList.toString() + "\nAllocList: " + allocatedList.toString();
	}
}