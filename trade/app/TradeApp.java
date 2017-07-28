package com.trade.app;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class TradeApp {
	/*
	 * 
	 * limit buy 10 99.00 limit buy 15 100.00 limit buy 3 100.50 limit sell 5
	 * 100.00 limit buy 5 99.50 stop sell 3 99.49 cancel na 2 0.00 market sell 6
	 * 0.00
	 * 
	 */
	public static void main(String args[]) throws Exception {

		int lineNumber = 0;
		File file = new File("/Users/pratikdhage/Documents/orders.txt");
		Order currentOrder = null;

		try {
			//
			// Create a new Scanner object which will read the data from the
			// file passed in. To check if there are more line to read from it
			// we check by calling the scanner.hasNextLine() method. We then
			// read line one by one till all line is read.
			//
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));

			PriorityQueue<Order> minheap = new PriorityQueue<Order>(1, new Comparator<Order>() {

				@Override
				public int compare(Order o1, Order o2) {
					if (Double.compare(o1.getVariable2(), o2.getVariable2()) == 0) {
						return Double.compare(o1.getOrderNumber(), o2.getOrderNumber());
					}
					return Double.compare(o1.getVariable2(), o2.getVariable2());
				}
			});

			PriorityQueue<Order> maxheap = new PriorityQueue<Order>(1, new Comparator<Order>() {

				@Override
				public int compare(Order o1, Order o2) {
					if (Double.compare(o2.getVariable2(), o1.getVariable2()) == 0) {
						return Double.compare(o1.getOrderNumber(), o2.getOrderNumber());
					}
					return Double.compare(o2.getVariable2(), o1.getVariable2());
				}
			});

			while (scanner.hasNext()) {
				String[] inputArray = scanner.nextLine().split(" ");

				lineNumber++;

				String orderType = inputArray[0];
				String orderName = inputArray[1];
				int variable1 = Integer.parseInt(inputArray[2]);
				double variable2 = Double.parseDouble(inputArray[3]);

				currentOrder = new Order(orderType, orderName, variable1, variable2);
				currentOrder.setOrderNumber(lineNumber);

				System.out.println("currently processing : " + currentOrder);
				processOrder(currentOrder, minheap, maxheap);

				System.out.println("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void processOrder(Order currentOrder, PriorityQueue<Order> minheap, PriorityQueue<Order> maxheap) {

		// for sell : maxHeap of buy items
		// for buy : minHeap of sell items

		Order temp = null;
		int tempVolume;
		int currentOrderVolume;
		double threshold = 99.49;

		if (currentOrder.getOrderName().equals("buy")) {
			if (minheap.isEmpty()) {
				System.out.println("Adding into maxheap : ");
				maxheap.add(currentOrder);
			} else {
				System.out.println("yet to work .......\n");
			}

		} else if (currentOrder.getOrderName().equals("sell")) {
			if (maxheap.isEmpty()) {
				minheap.add(currentOrder);
			} else {
				if (currentOrder.getOrderType().equals("limit")) {
					while (currentOrder.getVariable2() <= maxheap.peek().getVariable2() && !maxheap.isEmpty()) {
						if (maxheap.peek().getVariable1() > 0) {
							if (currentOrder.getVariable1() >= maxheap.peek().getVariable1()) {
								currentOrder.setVariable1(currentOrder.getVariable1() - maxheap.peek().getVariable1());
								System.out.println("\nMatch ");
								System.out.println("\nRemoving matching buy entry from maxHeap : \n" + maxheap.peek());
								maxheap.poll();
							} else if (currentOrder.getVariable1() < maxheap.peek().getVariable1()) {
								System.out.println("\nMatch : " + maxheap.peek());
								maxheap.peek()
										.setVariable1(maxheap.peek().getVariable1() - currentOrder.getVariable1());
								System.out.println("\nUpdating limit buy entry into maxHeap : \n" + maxheap);
								break;
							}
						}
						if (currentOrder.getVariable2() > maxheap.peek().getVariable2()) {
							minheap.add(currentOrder);
							break;
						}
					}
					if (maxheap.peek().getVariable2() <= threshold) {
						System.out.println("\nless than threshold ......");
						minheap.remove();
					}

				} else if (currentOrder.getOrderType().equals("market")) {
					System.out.println("found market ...sell");
					while (!maxheap.isEmpty()) {
						if (maxheap.peek().getVariable1() > 0) {
							if (currentOrder.getVariable1() >= maxheap.peek().getVariable1()) {
								currentOrder.setVariable1(currentOrder.getVariable1() - maxheap.peek().getVariable1());
								System.out.println("\nMatch ");
								System.out.println("\nRemoving matching buy entry from maxHeap : \n" + maxheap.peek());
								maxheap.poll();
							} else if (currentOrder.getVariable1() < maxheap.peek().getVariable1()) {
								System.out.println("\nMatch : " + maxheap.peek());
								maxheap.peek()
										.setVariable1(maxheap.peek().getVariable1() - currentOrder.getVariable1());
								System.out.println("\nUpdating buy entry into maxHeap : \n" + maxheap);
								break;
							}
						}
						if (maxheap.peek().getVariable2() <= threshold) {
							System.out.println("\nless than threshold ......");
							System.out.println("\nStopping the sell ....less than threshold");
							minheap.remove();
							break;
						}
					}
				} else if (currentOrder.getOrderType().equals("stop")) {
					while (maxheap.peek().getVariable2() > threshold && !maxheap.isEmpty()) {
						if (maxheap.peek().getVariable1() > 0) {
							if (currentOrder.getVariable1() >= maxheap.peek().getVariable1()) {
								currentOrder.setVariable1(currentOrder.getVariable1() - maxheap.peek().getVariable1());
								System.out.println("\nMatch ");
								System.out.println("\nRemoving matching buy entry from maxHeap : \n" + maxheap.peek());
								maxheap.poll();
							} else if (currentOrder.getVariable1() < maxheap.peek().getVariable1()) {
								System.out.println("\nMatch : " + maxheap.peek());
								maxheap.peek()
										.setVariable1(maxheap.peek().getVariable1() - currentOrder.getVariable1());
								System.out.println("\nUpdating limit buy entry into maxHeap : \n" + maxheap);
							}
						}
						if (maxheap.peek().getVariable2() <= threshold) {
							System.out.println("\nless than threshold ......");
							if (!minheap.isEmpty()) {
								minheap.remove();
							}
							break;
						}
					}
				} else if (currentOrder.getOrderType().equals("cancel")) {
					System.out.println("cancelling order number : " + currentOrder.getOrderNumber() + " from minHeap");
					double number = currentOrder.getOrderNumber();

					for (Iterator iterator = minheap.iterator(); iterator.hasNext();) {
						Order order = (Order) iterator.next();
						if (order.getOrderNumber() == number) {
							System.out.println("Removing the order number : " + order);
							minheap.remove(order);
						}
					}

					if (maxheap.peek().getVariable2() <= threshold) {
						System.out.println("\nless than threshold ......");
						minheap.remove();
					}
				}
			}
		} // sell

		System.out.println("\n minHeap is :" + minheap);
		System.out.println("\n maxHeap is :" + maxheap);

		System.out.println("************************************************************************");

	}

}