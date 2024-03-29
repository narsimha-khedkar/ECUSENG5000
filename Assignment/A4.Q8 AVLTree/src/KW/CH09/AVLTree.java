
/**
 * Assignment 4 - Question 9
 * Class to create a AVLTree and test different cases
 *  
 */

package KW.CH09;

/*<listing chapter="9" section="2">*/
/**
 * Self-balancing binary search tree using the algorithm defined by
 * Adelson-Velskii and Landis.
 * 
 * @param <E> The element type
 * @author Koffman and Wolfgang
 */
public class AVLTree<E extends Comparable<E>> extends BinarySearchTreeWithRotate<E> {

	// Insert nested class AVLNode<E> here.
	/* <listing chapter="9" number="2"> */
	/**
	 * Class to represent an AVL Node. It extends the BinaryTree.Node by adding the
	 * balance field.
	 */
	private static class AVLNode<E> extends Node<E> {

		/** Constant to indicate left-heavy */
		public static final int LEFT_HEAVY = -1;
		/** Constant to indicate balanced */
		public static final int BALANCED = 0;
		/** Constant to indicate right-heavy */
		public static final int RIGHT_HEAVY = 1;
		/** balance is right subtree height - left subtree height */
		private int balance;
		private String bal;

		// Methods
		/**
		 * Construct a node with the given item as the data field.
		 * 
		 * @param item The data field
		 */
		public AVLNode(E item) {
			super(item);
			balance = BALANCED;
		}

		/**
		 * Return a string representation of this object. The balance value is appended
		 * to the contents.
		 * 
		 * @return String representation of this object
		 */
		@Override
		public String toString() {

			if (balance > 0) {
				bal = "+" + balance;
			} else if (balance < 0) {
				bal = "" + balance;
			} else {
				bal = " " + balance;
			}

			return bal + " : " + super.toString();
		}
	}

	/* </listing> */
	// Data Fields
	/** Flag to indicate that height of tree has increased. */
	private boolean increase;

	// Insert solution to programming project 5, chapter 9 here
	/** Flag to indicate that height of tree has decreased */
	private boolean decrease;

	// Methods
	/**
	 * add starter method.
	 * 
	 * @pre the item to insert implements the Comparable interface.
	 * @param item The item being inserted.
	 * @return true if the object is inserted; false if the object already exists in
	 *         the tree
	 * @throws ClassCastException if item is not Comparable
	 */
	@Override
	public boolean add(E item) {
		increase = false;
		root = add((AVLNode<E>) root, item);
		return addReturn;
	}

	/**
	 * Recursive add method. Inserts the given object into the tree.
	 * 
	 * @post addReturn is set true if the item is inserted, false if the item is
	 *       already in the tree.
	 * @param localRoot The local root of the subtree
	 * @param item      The object to be inserted
	 * @return The new local root of the subtree with the item inserted
	 */
	private AVLNode<E> add(AVLNode<E> localRoot, E item) {
		if (localRoot == null) {
			addReturn = true;
			increase = true;
			return new AVLNode<>(item);
		}

		if (item.compareTo(localRoot.data) == 0) {
			// Item is already in the tree.
			increase = false;
			addReturn = false;
			return localRoot;
		} else if (item.compareTo(localRoot.data) < 0) {
			// item < data
			localRoot.left = add((AVLNode<E>) localRoot.left, item);

			if (increase) {
				decrementBalance(localRoot);
				if (localRoot.balance < AVLNode.LEFT_HEAVY) {
					increase = false;
					return rebalanceLeft(localRoot);
				}
			}
			return localRoot; // Rebalance not needed.
		} else { // item > data
			// Insert solution to programming exercise 2, section 2, chapter 9 here
			// Program the code in the add method for the case where
			// item.compareTo(localRoot.data)> 0.

			// When Item > data check if Height of tree has increased and if the Tree is
			// Right Heavy then Rebalance the Right Tree
			localRoot.right = add((AVLNode<E>) localRoot.right, item);
			if (increase) {
				incrementBalance(localRoot);
				if (localRoot.balance > AVLNode.RIGHT_HEAVY) {
					increase = false;
					return rebalanceRight(localRoot);
				} else {
					return localRoot;
				}
			} else {
				return localRoot;
			}
		}
	}

	/* <listing chapter="9" number="3"> */
	/**
	 * Method to rebalance left.
	 * 
	 * @pre localRoot is the root of an AVL subtree that is critically left-heavy.
	 * @post Balance is restored.
	 * @param localRoot Root of the AVL subtree that needs rebalancing
	 * @return a new localRoot
	 */
	private AVLNode<E> rebalanceLeft(AVLNode<E> localRoot) {
		// Obtain reference to left child.
		AVLNode<E> leftChild = (AVLNode<E>) localRoot.left;
		// See whether left-right heavy.
		if (leftChild.balance > AVLNode.BALANCED) {
			// Obtain reference to left-right child.
			AVLNode<E> leftRightChild = (AVLNode<E>) leftChild.right;
			// Adjust the balances to be their new values after
			// the rotations are performed.
			if (leftRightChild.balance < AVLNode.BALANCED) {
				leftChild.balance = AVLNode.LEFT_HEAVY;
				leftRightChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.BALANCED;
			} else if (leftRightChild.balance > AVLNode.BALANCED) {
				leftChild.balance = AVLNode.BALANCED;
				leftRightChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.RIGHT_HEAVY;
			} else {
				leftChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.BALANCED;
			}
			// Perform left rotation.
			localRoot.left = rotateLeft(leftChild);
		} else { // Left-Left case
			// In this case the leftChild (the new root)
			// and the root (new right child) will both be balanced
			// after the rotation.
			leftChild.balance = AVLNode.BALANCED;
			localRoot.balance = AVLNode.BALANCED;
		}
		// Now rotate the local root right.
		return (AVLNode<E>) rotateRight(localRoot);
	}
	/* </listing> */

	// Insert solution to programming exercise 1, section 2, chapter 9 here
	// Program the rebalanceRight method

	// Replicating the logic to write the Rebalancing of the right tree when it is
	// Right Heavy
	/**
	 * Method to rebalance right.
	 * 
	 * @pre localRoot is the root of an AVL subtree that is critically right-heavy.
	 * @post Balance is restored.
	 * @param localRoot Root of the AVL subtree that needs rebalancing
	 * @return a new localRoot
	 */
	private AVLNode<E> rebalanceRight(AVLNode<E> localRoot) {
		// Obtain reference to right child.
		AVLNode<E> rightChild = (AVLNode<E>) localRoot.right;
		// See whether right-left heavy.
		if (rightChild.balance < AVLNode.BALANCED) {
			// Obtain reference to right-left child.
			AVLNode<E> rightLeftChild = (AVLNode<E>) rightChild.left;
			// Adjust the balances to be their new values after
			// the rotations are performed.
			if (rightLeftChild.balance > AVLNode.BALANCED) {
				rightChild.balance = AVLNode.RIGHT_HEAVY;
				rightLeftChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.BALANCED;
			} else if (rightLeftChild.balance < AVLNode.BALANCED) {
				rightChild.balance = AVLNode.BALANCED;
				rightLeftChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.LEFT_HEAVY;
			} else {
				rightChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.BALANCED;
			}
			// Perform right rotation.
			localRoot.right = rotateRight(rightChild);
		} else { // right-right case
			// In this case the rightChild (the new root)
			// and the root (new left child) will both be balanced
			// after the rotation.
			rightChild.balance = AVLNode.BALANCED;
			localRoot.balance = AVLNode.BALANCED;
		}
		// Now rotate the local root left.
		return (AVLNode<E>) rotateLeft(localRoot);
	}

	/**
	 * Method to decrement the balance field and to reset the value of increase.
	 * 
	 * @pre The balance field was correct prior to an insertion [or removal,] and an
	 *      item is either been added to the left[ or removed from the right].
	 * @post The balance is decremented and the increase flags is set to false if
	 *       the overall height of this subtree has not changed.
	 * @param node The AVL node whose balance is to be incremented
	 */
	private void decrementBalance(AVLNode<E> node) {
		// Decrement the balance.
		node.balance--;
		if (node.balance == AVLNode.BALANCED) {
			// If now balanced, overall height has not increased.
			increase = false;
		}
	}

	// Insert solution to programming exercise 3, section 2, chapter 9 here
	// Program the incrementBalance method

	// Replicating the logic to write the incrementBalance to increment the Balance
	// Field and then resetting the Increase Flag
	/**
	 * Method to increment the balance field and to reset the value of increase.
	 * 
	 * @pre The balance field was correct prior to an insertion [or removal,] and an
	 *      item is either been added to the right[ or removed from the left].
	 * @post The balance is incremented and the increase flags is set to false if
	 *       the overall height of this subtree has not changed.
	 * @param node The AVL node whose balance is to be incremented
	 */
	private void incrementBalance(AVLNode<E> node) {
		// Decrement the balance.
		node.balance++;
		if (node.balance == AVLNode.BALANCED) {
			// If now balanced, overall height has not increased.
			increase = false;
		}
	}

	// Insert solution to programming project 5, chapter 9 here
	// Wrapper Method to delete the given object from the AVL Tree
	/**
	 * Delete Starter method.
	 * 
	 * @post The object is not in the tree
	 * @param item - The object to be removed.
	 * @return The object from the tree that was removed or null if the object was
	 *         not in the tree.
	 */
	@Override
	public E delete(E item) {
		decrease = false;
		root = delete((AVLNode<E>) root, item);
		return deleteReturn;
	}

	/**
	 * Recursive delete method. Removes the given object from the tree.
	 * 
	 * @post The object is not in the tree and removeReturn is set to the object
	 *       that was removed, otherwise it is set false.
	 * @param localRoot The root of the local subtree
	 * @param item      The item to be removed
	 * @return The new root of the local subtree with the item removed.
	 */
	private AVLNode<E> delete(AVLNode<E> localRoot, E item) {
		if (localRoot == null) { // item is not in tree
			deleteReturn = null;
			return localRoot;
		}
		if (item.compareTo(localRoot.data) == 0) {
			// item found in the tree -- need to remove it
			deleteReturn = localRoot.data;
			return findNodeToSwap(localRoot);
		} else if (item.compareTo(localRoot.data) < 0) {
			localRoot.left = delete((AVLNode<E>) localRoot.left, item);
			if (decrease) {
				incrementBalance(localRoot);
				if (localRoot.balance > AVLNode.RIGHT_HEAVY) {
					return rebalanceRightLeft((AVLNode<E>) localRoot);
				} else {
					return localRoot;
				}
			} else {
				return localRoot;
			}
		} else {
			localRoot.right = delete((AVLNode<E>) localRoot.right, item);
			if (decrease) {
				decrementBalance(localRoot);
				if (localRoot.balance < AVLNode.LEFT_HEAVY) {
					return rebalanceLeftRight(localRoot);
				} else {
					return localRoot;
				}
			} else {
				return localRoot;
			}
		}
	}

	/**
	 * Find a node to take the position of the deleted node.
	 * 
	 * @param node The node to be deleted
	 * @return null if both of node is a leaf. Left if node.right is null.
	 */
	private AVLNode<E> findNodeToSwap(AVLNode<E> node) {
		if (node.left == null) {
			decrease = true;
			return (AVLNode<E>) node.right;
		} else if (node.right == null) {
			decrease = true;
			return (AVLNode<E>) node.left;
		} else {
			if (node.left.right == null) {
				node.data = node.left.data;
				node.left = node.left.left;
				incrementBalance(node);
				return node;
			} else {
				node.data = findLargestLeaf((AVLNode<E>) node.left);
				if (((AVLNode<E>) node.left).balance < AVLNode.LEFT_HEAVY) {
					node.left = rebalanceLeft((AVLNode<E>) node.left);
				}
				if (decrease) {
					incrementBalance(node);
				}
				return node;
			}
		}
	}

	/**
	 * Find the node that it is the largest Leaf node
	 * 
	 * @param parent - The node
	 * @return the value of the current node
	 */
	private E findLargestLeaf(AVLNode<E> parent) {
		if (parent.right.right == null) {
			E returnValue = parent.right.data;
			parent.right = parent.right.left;
			decrementBalance(parent);
			return returnValue;
		} else {
			E returnValue = findLargestLeaf((AVLNode<E>) parent.right);
			if (((AVLNode<E>) parent.right).balance < AVLNode.LEFT_HEAVY) {
				parent.right = rebalanceLeft((AVLNode<E>) parent.right);
			}
			if (decrease) {
				decrementBalance(parent);
			}
			return returnValue;
		}
	}

	/**
	 * Function to do Rotation Left-Right
	 * 
	 * @param localRoot Root of the AVL subtree that needs rebalancing
	 * @return localRoot
	 */
	private AVLNode<E> rebalanceLeftRight(AVLNode<E> localRoot) {
		AVLNode<E> leftChild = (AVLNode<E>) localRoot.left;
		if (leftChild.balance > AVLNode.BALANCED) {
			AVLNode<E> leftRightChild = (AVLNode<E>) leftChild.right;
			if (leftRightChild.balance < AVLNode.BALANCED) {
				leftChild.balance = AVLNode.LEFT_HEAVY;
				leftRightChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.BALANCED;
			} else if (leftRightChild.balance > AVLNode.BALANCED) {
				leftChild.balance = AVLNode.BALANCED;
				leftRightChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.RIGHT_HEAVY;
			} else {
				leftChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.BALANCED;
			}
			increase = false;
			decrease = true;
			localRoot.left = rotateLeft(leftChild);
			return (AVLNode<E>) rotateRight(localRoot);
		}
		if (leftChild.balance < AVLNode.BALANCED) {
			leftChild.balance = AVLNode.BALANCED;
			localRoot.balance = AVLNode.BALANCED;
			increase = false;
			decrease = true;
		} else {
			leftChild.balance = AVLNode.RIGHT_HEAVY;
			localRoot.balance = AVLNode.LEFT_HEAVY;
		}
		return (AVLNode<E>) rotateRight(localRoot);
	}

	/**
	 * Function to do Rotation Right-Left
	 * 
	 * @param localRoot Root of the AVL subtree that needs rebalancing
	 * @return localRoot
	 */
	private AVLNode<E> rebalanceRightLeft(AVLNode<E> localRoot) {
		AVLNode<E> rightChild = (AVLNode<E>) localRoot.right;
		if (rightChild.balance < AVLNode.BALANCED) {
			AVLNode<E> rightLeftChild = (AVLNode<E>) rightChild.left;
			if (rightLeftChild.balance > AVLNode.BALANCED) {
				rightChild.balance = AVLNode.RIGHT_HEAVY;
				rightLeftChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.BALANCED;
			} else if (rightLeftChild.balance < AVLNode.BALANCED) {
				rightChild.balance = AVLNode.BALANCED;
				rightLeftChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.LEFT_HEAVY;
			} else {
				rightChild.balance = AVLNode.BALANCED;
				localRoot.balance = AVLNode.BALANCED;
			}
			increase = false;
			decrease = true;
			localRoot.right = rotateRight(rightChild);
			return (AVLNode<E>) rotateLeft(localRoot);
		}
		if (rightChild.balance > AVLNode.BALANCED) {
			rightChild.balance = AVLNode.BALANCED;
			localRoot.balance = AVLNode.BALANCED;
			increase = false;
			decrease = true;
		} else {
			rightChild.balance = AVLNode.LEFT_HEAVY;
			localRoot.balance = AVLNode.RIGHT_HEAVY;
		}
		return (AVLNode<E>) rotateLeft(localRoot);
	}

}
