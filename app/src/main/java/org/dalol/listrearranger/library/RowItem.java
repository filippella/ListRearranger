/*
 * Copyright (c) 2016 Filippo Engidashet <filippo.eng@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package org.dalol.listrearranger.library;

/*
 * <h1>Add Row in the List!</h1>
 * Any view shown on the ListRearranger list must implement this abstraction
 *
 * <b>Note:</b> You can implement this interface and add additional
 * properties to the child class
 *
 * Ex: row name, row icon
 *
 * @author Filippo
 * @version 1.0.0
 * @since 1/26/2016
 */
public interface RowItem {

    /**
     * This method is responsible for identifying the category of the view
     *
     * @return true if the #RowItem is Favourite
     */
    boolean isFavourite();

    /**
     * This method is responsible for setting the category of the item
     *
     * @param favourite
     */
    void setFavourite(boolean favourite);
}
