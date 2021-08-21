/*
 * Copyright (c) 2021 Redfield AB.
*/
package se.redfield.textprocessing.nodes.vectorizer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.container.CellFactory;
import org.knime.core.data.container.SingleCellFactory;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.node.ExecutionContext;

import se.redfield.textprocessing.nodes.base.SpacyBaseNodeModel;
import se.redfield.textprocessing.nodes.base.SpacyNodeSettings;

public class SpacyVectorizerNodeModel extends SpacyBaseNodeModel {

	protected SpacyVectorizerNodeModel() {
		super(new SpacyNodeSettings());
	}

	@Override
	protected String getSpacyMethod() {
		return "SpacyVectorizer";
	}

	@Override
	protected DataColumnSpec createOutputColumnSpec() {
		return new DataColumnSpecCreator(settings.getColumn(), ListCell.getCollectionType(DoubleCell.TYPE))
				.createSpec();
	}

	@Override
	protected CellFactory createCellFactory(int inputColumn, int resultColumn, ExecutionContext exec) {
		return new SpacyVectorizerCellFactory(createOutputColumnSpec(), resultColumn);
	}

	private class SpacyVectorizerCellFactory extends SingleCellFactory {

		private final int columnIdx;

		public SpacyVectorizerCellFactory(DataColumnSpec newColSpec, int columnIdx) {
			super(newColSpec);
			this.columnIdx = columnIdx;
		}

		@Override
		public DataCell getCell(DataRow row) {
			List<DoubleCell> cells = Arrays.stream(row.getCell(columnIdx).toString().split(",")).map(Double::valueOf)
					.map(DoubleCell::new).collect(Collectors.toList());
			return CollectionCellFactory.createListCell(cells);
		}

	}
}