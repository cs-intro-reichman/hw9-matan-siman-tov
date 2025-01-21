public class LinkedList {

    private Node first; // pointer to the first element of this list
    private Node last;  // pointer to the last element of this list
    private int size;   // number of elements in this list

    /**
     * Constructs a new list.
     */ 
    public LinkedList() {
        first = null;
        last = null;
        size = 0;
    }

    /** Returns the first node in this list (may be null if list is empty). */
    public Node getFirst() {
        return first;
    }

    /** Returns the last node in this list (may be null if list is empty). */
    public Node getLast() {
        return last;
    }

    /** Returns the current size (number of elements) of this list. */
    public int getSize() {
        return size;
    }

    /**
     * Gets the node located at the given index in this list.
     * @param index the index of the node to retrieve, [0..size-1]
     * @return the node at that index
     * @throws IllegalArgumentException if index is out of range
     */
    public Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("index must be between 0 and size");
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    /**
     * Creates a new Node object that points to 'block',
     * and inserts it at the given index in this list.
     * @param block the memory block
     * @param index the position in [0..size]
     * @throws IllegalArgumentException if index is out of [0..size]
     */
    public void add(int index, MemoryBlock block) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("index must be between 0 and size");
        }
        Node newNode = new Node(block);

        // Insert at head
        if (index == 0) {
            newNode.next = first;
            first = newNode;
            if (size == 0) {
                last = newNode;
            }
            size++;
            return;
        }
        // Insert at tail
        if (index == size) {
            last.next = newNode;
            last = newNode;
            size++;
            return;
        }
        // Insert in the middle
        Node prev = getNode(index - 1);
        newNode.next = prev.next;
        prev.next = newNode;
        size++;
    }

    /** Adds 'block' at the end of this list. */
    public void addLast(MemoryBlock block) {
        add(size, block);  // just reuse
    }

    /** Adds 'block' at the beginning of this list. */
    public void addFirst(MemoryBlock block) {
        add(0, block);  // reuse
    }

    /**
     * Gets the memory block located at the given index in this list.
     * @param index the index [0..size-1]
     * @return the MemoryBlock
     * @throws IllegalArgumentException if index is out of range
     */
    public MemoryBlock getBlock(int index) {
        return getNode(index).block;
    }

    /**
     * Returns the index of 'block' in this list, or -1 if not found.
     */
    public int indexOf(MemoryBlock block) {
        Node current = first;
        int i = 0;
        while (current != null) {
            if (current.block.equals(block)) {
                return i;
            }
            current = current.next;
            i++;
        }
        return -1;
    }

    /**
     * Removes the given node from this list.
     * If node == null => must throw NullPointerException("NullPointerException!")
     */
    public void remove(Node node) {
        if (node == null) {
            throw new NullPointerException("NullPointerException!");
        }
        if (size == 0) {
            return; // nothing to do
        }
        // If removing the first node
        if (first == node) {
            first = first.next;
            size--;
            if (size == 0) {
                last = null;
            }
            return;
        }
        // Otherwise, find node
        Node current = first;
        while (current.next != null && current.next != node) {
            current = current.next;
        }
        if (current.next == node) {
            current.next = node.next;
            size--;
            if (node == last) {
                last = current;
            }
        }
    }

    /**
     * Removes the node at the given index.
     * @param index the index [0..size-1]
     * @throws IllegalArgumentException if index is out of range
     */
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("index must be between 0 and size");
        }
        if (index == 0) {
            Node oldFirst = first;
            first = first.next;
            size--;
            if (size == 0) {
                last = null;
            }
            return;
        }
        Node prev = getNode(index - 1);
        Node toRemove = prev.next;
        prev.next = toRemove.next;
        size--;
        if (toRemove == last) {
            last = prev;
        }
    }

    /**
     * Removes from this list the node that points to 'block'.
     * If block == null or block not found => throw IAE("index must be between 0 and size")
     */
    public void remove(MemoryBlock block) {
        if (block == null) {
            throw new IllegalArgumentException("index must be between 0 and size");
        }
        if (size == 0) {
            throw new IllegalArgumentException("index must be between 0 and size");
        }
        // If first is the block
        if (first.block.equals(block)) {
            first = first.next;
            size--;
            if (size == 0) {
                last = null;
            }
            return;
        }
        Node current = first;
        while (current.next != null && !current.next.block.equals(block)) {
            current = current.next;
        }
        if (current.next == null) {
            // not found
            throw new IllegalArgumentException("index must be between 0 and size");
        }
        Node toRemove = current.next;
        current.next = toRemove.next;
        size--;
        if (toRemove == last) {
            last = current;
        }
    }

    /** Returns an iterator over this list. */
    public ListIterator iterator(){
        return new ListIterator(first);
    }

    /**
     * toString(): The tests require each Node (MemoryBlock) in the format
     * "(baseAddress , length) " with a space at the end, no brackets or commas.
     * If the list is empty, return "" (empty string).
     * Example for two blocks: "(0 , 20) (20 , 30) "
     */
    public String toString() {
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            MemoryBlock mb = current.block;
            sb.append("(")
              .append(mb.baseAddress)
              .append(" , ")
              .append(mb.length)
              .append(") ");
            current = current.next;
        }
        return sb.toString();
    }
}