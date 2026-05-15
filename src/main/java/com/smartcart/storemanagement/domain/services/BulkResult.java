package com.smartcart.storemanagement.domain.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BulkResult {
    private final int processed;
    private final int failed;
    private final List<String> errors;

    public BulkResult(int processed, int failed, List<String> errors) {
        this.processed = processed;
        this.failed = failed;
        this.errors = new ArrayList<>(errors);
    }

    public int getProcessed() {
        return processed;
    }

    public int getFailed() {
        return failed;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}

