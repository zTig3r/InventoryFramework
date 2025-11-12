package de.ztiger.IF.inventory;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PaginatedPane extends Pane {

    /**
     * The current page number (0-indexed).
     * Defaults to 0.
     */
    protected int page = 0;

    /**
     * The list of panes for each page.
     */
    protected final List<Pane> pages = new ArrayList<>();

    /**
     * Adds a page to the paginated pane.
     *
     * @param pane The {@link Pane} to add
     */
    public void addPage(Pane pane) {
        this.pages.add(pane);
        if (this.chestInventory != null) {
            pane.setChestInventory(this.chestInventory);
        }
    }

    protected void setPage(int page) {
        if (page < 0 || page >= pages.size()) {
            throw new IndexOutOfBoundsException("Page index out of bounds: " + page);
        }

        if (this.chestInventory != null) {
            this.chestInventory.disablePane(this);

            this.page = page;

            this.chestInventory.updateByPane(this);
        }
    }

    /**
     * Navigates to the next page in the paginated pane.
     */
    public void nextPage() {
        if (page < pages.size() - 1) setPage(page + 1);
    }

    /**
     * Navigates to the previous page in the paginated pane.
     */
    public void previousPage() {
        if (page > 0) setPage(page - 1);
    }

    @Override
    public Map<Integer, InventoryItem> getInventoryItems() {
        if (pages.isEmpty()) return Collections.emptyMap();
        return pages.get(page).getInventoryItems();
    }

    @Override
    public void setChestInventory(ChestInventory chestInventory) {
        super.setChestInventory(chestInventory);
        for (Pane p : pages) {
            p.setChestInventory(chestInventory);
        }
        if (chestInventory != null) {
            chestInventory.updateByPane(this);
        }
    }
}
