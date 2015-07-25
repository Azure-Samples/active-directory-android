/*
Copyright (c) Microsoft
All Rights Reserved
Apache 2.0 License
 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
See the Apache Version 2.0 License for specific language governing permissions and limitations under the License.
 */

package com.microsoft.aad.taskapplication.helpers;

/**
 * Represents an item in a ToDo list
 */
public class WorkItem {

	/**
	 * Item title
	 */
	@com.google.gson.annotations.SerializedName("Task")
	private String mTask;

	/**
	 * Item Id
	 */
	@com.google.gson.annotations.SerializedName("Id")
	private int mId;

	/**
	 * Indicates if the item is completed
	 */
	@com.google.gson.annotations.SerializedName("Owner")
	private String mComplete;

	/**
	 * WorkItem constructor
	 */
	public WorkItem() {

	}

	/**
	 * Initializes a new WorkItem
	 * 
	 * @param text
	 *            The item text
	 * @param id
	 *            The item id
	 */
	public WorkItem(String text, int id) {
		this.setTitle(text);
		this.setId(id);
	}

	@Override
	public String toString() {
		return getTitle();
	}

	/**
	 * Returns the item text
	 */
	public String getTitle() {
		return mTask;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setTitle(String text) {
		mTask = text;
	}

	/**
	 * Returns the item id
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Sets the item id
	 * 
	 * @param id
	 *            id to set
	 */
	public final void setId(int id) {
		mId = id;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof WorkItem && ((WorkItem) o).mId == mId;
	}

}
