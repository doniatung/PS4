/**
 * Implementation of a queue using a singly-linked list
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Scot Drysdale: testing code
 * @author CBK, Spring 2016, cleaned up inner class
 * @see SimpleQueue
 */
public class SLLQueue<T> implements SimpleQueue<T> {
	private Element head;	// front of the linked list
	private Element tail;	// tail of the linked list

	/**
	 * The linked elements
	 */
	private class Element {
		private T data;
		private Element next;

		public Element(T data) {
			this.data = data;
			this.next = null;
		}
	}

	/**
	 *  Creates an empty queue
	 */
	public SLLQueue()  {
		head = null;
		tail = null;
	}

	public void enqueue(T item) {
		if (isEmpty()) {
			// first item
			head = new Element(item);
			tail = head;
		}
		else {
			tail.next = new Element(item);
			tail = tail.next;
		}
	}

	public T dequeue() throws Exception {
		if (isEmpty()) throw new Exception("empty queue");
		T item = head.data;
		head = head.next;
		return item;
	}

	public T front() throws Exception {
		if (isEmpty()) throw new Exception("empty queue");
		return head.data;
	}

	public boolean isEmpty() {
		return head == null;
	}

}
