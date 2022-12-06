package ed.dpf.process.manager.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputConfiguration {
    private boolean mandatory;
    private Map<String, Object> selector;
}
