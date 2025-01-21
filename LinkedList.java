public class LinkedList {
	
	private Node first; // pointer to the first element of this list
	private Node last;  // pointer to the last element of this list
	private int size;   // number of elements in this list
	
	/**
	 * Constructs a new list.
	 */ 
	public LinkedList () {
		first = null;
		last = null;
		size = 0;
	}
	
	/**
	 * Gets the first node of the list
	 * @return The first node of the list.
	 */		
	public Node getFirst() {
		return this.first;
	}

	/**
	 * Gets the last node of the list
	 * @return The last node of the list.
	 */		
	public Node getLast() {
		return this.last;
	}
	
	/**
	 * Gets the current size of the list
	 * @return The size of the list.
	 */		
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Gets the node located at the given index in this list. 
	 * 
	 * @param index the index of the node to retrieve, between 0 and size-1
	 * @throws IllegalArgumentException if index is out of range
	 * @return the node at the given index
	 */		
	public Node getNode(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		Node current = this.first;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		return current;
	}

	/**
	 * Creates a new Node object that points to the given memory block, 
	 * and inserts the node at the given index in this list.
	 * <p>
	 * If the given index is 0, the new node becomes the first node.
	 * <p>
	 * If the given index equals the list's size, the new node becomes the last.
	 * <p>
	 * The method implementation is optimized for index=0 or index=size => O(1).
	 * 
	 * @param block the memory block to be inserted
	 * @param index the index before which the memory block should be inserted
	 * @throws IllegalArgumentException if index < 0 or index > size
	 */
	public void add(int index, MemoryBlock block) {
		if (index < 0 || index > size) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
	
		Node newNode = new Node(block);
	
		// Case 1: Add at beginning
		if (index == 0) {
			newNode.next = this.first;
			this.first = newNode;
			if (this.size == 0) {
				this.last = newNode;
			}
			this.size++;
			return;
		}
		// Case 2: Add at the end
		if (index == this.size) {
			this.last.next = newNode;
			this.last = newNode;
			this.size++;
			return;
		}
		// Case 3: Add in the middle
		Node current = this.first;
		for (int i = 0; i < index - 1; i++) {
			current = current.next;
		}
		newNode.next = current.next;
		current.next = newNode;
		this.size++;
	}

	/**
	 * Creates a new node that points to the given memory block, and adds it
	 * to the end of this list.
	 * 
	 * @param block the given memory block
	 */
	public void addLast(MemoryBlock block) {
		Node newNode = new Node(block);
		if (this.size == 0) {
			this.first = newNode;
			this.last = newNode;
		} else {
			this.last.next = newNode;
			this.last = newNode;
		}
		this.size++;
	}
	
	/**
	 * Creates a new node that points to the given memory block, and adds it 
	 * to the beginning of this list.
	 * 
	 * @param block the given memory block
	 */
	public void addFirst(MemoryBlock block) {
		Node newNode = new Node(block);
		if (this.size == 0) {
			this.first = newNode;
			this.last = newNode;
		} else {
			newNode.next = this.first;
			this.first = newNode;
		}
		this.size++;
	}

	/**
	 * Gets the memory block located at the given index in this list.
	 * 
	 * @param index the index of the retrieved memory block
	 * @return the memory block at the given index
	 * @throws IllegalArgumentException if index out of range
	 */
	public MemoryBlock getBlock(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		Node current = this.first;
		for(int i = 0 ; i < index ; i++){
			current = current.next;
		}
		return current.block;
	}	

	/**
	 * Gets the index of the node pointing to the given memory block.
	 * 
	 * @param block the given memory block
	 * @return the index of the block, or -1 if not in this list
	 */
	public int indexOf(MemoryBlock block) {
		Node current = this.first;
		int index = 0;
		while (current != null) {
			if (current.block.equals(block)) {
				return index;
			}
			current = current.next;
			index++;
		}
		return -1;
	}

	/**
	 * Removes the given node from this list.	
	 * 
	 * @param node the node that will be removed
	 * @throws NullPointerException if node == null (as per the tests)
	 */
	public void remove(Node node) {
		// אם node == null => לפי הטסטים צריכים לזרוק NullPointerException עם הודעה מסוימת
		if (node == null) {
			throw new NullPointerException("NullPointerException!");
		}
		if (size == 0) {
			return; // list empty, nothing to remove
		}
		// אם הנוד הראשון הוא זה שיש להסיר
		if (this.first == node) {
			this.first = this.first.next;
			if (this.size == 1) {
				this.last = null;
			}
			this.size--;
			return;
		}
		// חיפוש הנוד
		Node current = this.first;
		while (current.next != null && current.next != node) {
			current = current.next;
		}
		// אם מצאנו
		if (current.next == node) {
			current.next = node.next;
			if (node == this.last) {
				this.last = current;
			}
			this.size--;
		}
	}

	/**
	 * Removes from this list the node which is located at the given index.
	 * 
	 * @param index the location of the node
	 * @throws IllegalArgumentException if index is out of range
	 */
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		if (index == 0) {
			this.first = this.first.next;
			if (this.size == 1) {
				this.last = null;
			}
			this.size--;
			return;
		}
		Node current = this.first;
		for(int i = 0 ; i < index - 1 ; i++){
			current = current.next;
		}
		Node nodeToRemove = current.next;
		current.next = nodeToRemove.next;
		if (nodeToRemove == this.last) {
			this.last = current;
		}
		this.size--;
	}

	/**
	 * Removes from this list the node pointing to the given memory block.
	 * 
	 * @param block the memory block to remove
	 * @throws IllegalArgumentException if block == null or block is not in the list
	 *         (the tests expect the same message in both cases)
	 */
	public void remove(MemoryBlock block) {
		if (block == null) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		if (size == 0) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		// אם הנוד הראשון מכיל את הבלוק
		if (this.first.block.equals(block)) {
			this.first = this.first.next;
			if (this.size == 1) {
				this.last = null;
			}
			this.size--;
			return;
		}
		// חיפוש הבלוק
		Node current = this.first;
		while (current.next != null && !current.next.block.equals(block)) {
			current = current.next;
		}
		if (current.next == null) {
			// not found
			throw new IllegalArgumentException("index must be between 0 and size");
		}
		// הסרה
		Node toRemove = current.next;
		current.next = toRemove.next;
		if (toRemove == this.last) {
			this.last = current;
		}
		this.size--;
	}

	/**
	 * Returns an iterator over this list, starting with the first element.
	 */
	public ListIterator iterator(){
		return new ListIterator(first);
	}
	
	/**
	 * A textual representation of this list, for debugging.
	 */
	public String toString() {
		if (size == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		Node current = this.first;
		while (current != null) {
			sb.append(current.toString());
			if (current.next != null) {
				sb.append(", ");
			}
			current = current.next;
		}
		sb.append("]");
		return sb.toString();
	}
}