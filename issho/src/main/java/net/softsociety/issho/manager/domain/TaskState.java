
package net.softsociety.issho.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskState {
	int requested;
	int progress;
	int done;
	int pending;
}
