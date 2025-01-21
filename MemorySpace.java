public class MemorySpace {
	
	private LinkedList allocatedList; // blocks currently allocated
	private LinkedList freeList;      // blocks currently free

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
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
	 * Returns the base address, or -1 if unable.
	 * 
	 * @param length the length of the block needed
	 * @return the base address, or -1 if fail
	 */
	public int malloc(int length) {
		// סריקה פשוטה של freeList
		ListIterator it = freeList.iterator();
		while (it.hasNext()) {
			MemoryBlock curBlock = it.current.block;
			// אם הבלוק מספיק גדול
			if (curBlock.length == length) {
				// הבלוק מתאים בול
				int base = curBlock.baseAddress;
				allocatedList.addLast(new MemoryBlock(base, length));
				freeList.remove(curBlock); 
				return base;
			} else if (curBlock.length > length) {
				int base = curBlock.baseAddress;
				// מוסיפים ל־allocatedList
				allocatedList.addLast(new MemoryBlock(base, length));
				// מעדכנים את הבלוק החופשי שיישאר
				curBlock.baseAddress += length;
				curBlock.length -= length;
				return base;
			}
			// אחרת, ממשיכים לבלוק הבא
			it.current = it.current.next;
		}
		// אם לא מצאנו בלוק מתאים
		return -1;
	}

	/**
	 * Frees the memory block whose base address = address.
	 * 
	 * @param address the address of the block to free
	 * @throws IllegalArgumentException if the address doesn't exist in allocatedList
	 */
	public void free(int address) {
		ListIterator it = allocatedList.iterator();
		while (it.hasNext()) {
			MemoryBlock cur = it.current.block;
			if (cur.baseAddress == address) {
				// מצאנו את הבלוק – משחררים
				freeList.addLast(new MemoryBlock(cur.baseAddress, cur.length));
				allocatedList.remove(cur);
				return;
			}
			it.current = it.current.next;
		}
		// אם הגענו לפה, לא מצאנו בלוק כזה ב־allocatedList
		throw new IllegalArgumentException("index must be between 0 and size");
	}
	
	/**
	 * Performs defragmentation: merges adjacent free blocks, sorted by baseAddress.
	 */
	public void defrag() {
		// 1. ניצור רשימה זמנית חדשה ממוין לפי baseAddress
		if (freeList.getSize() < 2) {
			// אין מה למזג
			return;
		}
		// שליפת כל הבלוקים לתוך ArrayList למיון
		java.util.ArrayList<MemoryBlock> blocks = new java.util.ArrayList<>();
		ListIterator it = freeList.iterator();
		while (it.hasNext()) {
			blocks.add(it.current.block);
			it.current = it.current.next;
		}
		// מסדרים לפי הכתובת
		blocks.sort((a,b) -> Integer.compare(a.baseAddress, b.baseAddress));
		// מוחקים את הקודמת ובונים חדשה ממוזגת
		freeList = new LinkedList();
		for (MemoryBlock block : blocks) {
			freeList.addLast(block);
		}
		// 2. מעבר למזג
		int i = 0;
		while (i < freeList.getSize() - 1) {
			MemoryBlock current = freeList.getBlock(i);
			MemoryBlock next = freeList.getBlock(i+1);
			// אם גבול הסיום של current הוא בדיוק baseAddress של next
			if (current.baseAddress + current.length == next.baseAddress) {
				// מיזוג
				current.length += next.length;
				freeList.remove(next);
				// לא מקדמים i כדי לבדוק שוב אם ניתן למזג את current עם הבא בתור
			} else {
				i++;
			}
		}
	}
	
	public String toString() {
		return "FreeList: " + freeList.toString() + "\nAllocList: " + allocatedList.toString();
	}
}