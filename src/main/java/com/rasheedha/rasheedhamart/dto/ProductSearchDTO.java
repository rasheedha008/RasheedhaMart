package com.rasheedha.rasheedhamart.dto;

public class ProductSearchDTO {
    private String keyword;
    private String category;
    private int page = 1;
    private int pageSize = 10;

    public ProductSearchDTO() {}

    public ProductSearchDTO(String keyword, String category, int page, int pageSize) {
        this.keyword = keyword;
        this.category = category;
        this.page = page > 0 ? page : 1;
        this.pageSize = pageSize > 0 ? pageSize : 10;
    }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page > 0 ? page : 1; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize > 0 ? pageSize : 10; }

    public int getOffset() { return (page - 1) * pageSize; }
}
