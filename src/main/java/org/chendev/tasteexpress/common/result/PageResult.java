package org.chendev.tasteexpress.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    /**
     * Total number of records that match the query.
     */
    private long total;

    /**
     * Current page data.
     */
    private List<T> records;
}
