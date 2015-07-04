/*
 * GraphMLWriter.java - This file is part of the Jakstab project.
 * Copyright 2007-2015 Johannes Kinder <jk@jakstab.org>
 * Copyright 2009 Daniel Reynaud <reynaud.daniel@gmail.com>
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, see <http://www.gnu.org/licenses/>.
 */

package org.jakstab.util;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.jakstab.util.Logger;

/**
 * @author Daniel Reynaud, Johannes Kinder
 */
public class GraphSNAPWriter implements GraphWriter {

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(GraphSNAPWriter.class);

	private int count = 0;
	private Map<String, Integer> idMap = new HashMap<String, Integer>();
	
	private final OutputStreamWriter nodesOut;
	private final OutputStreamWriter edgesOut;
	private String filename;
	private String nodesFilename;
	private String edgesFilename;

	public GraphSNAPWriter(String filename) throws IOException {
        this.filename = filename;
		this.nodesFilename = filename + ".blocks";
		this.edgesFilename = filename + ".edges";
		nodesOut = new OutputStreamWriter(new FileOutputStream(this.nodesFilename));
		edgesOut = new OutputStreamWriter(new FileOutputStream(this.edgesFilename));
	}

	@Override
	public void close() throws IOException {
		nodesOut.close();
		edgesOut.close();
	}

	@Override
	public final void writeNode(String id, String body) throws IOException {
		writeNode(id, body, null);
	}

	@Override
	public final void writeNode(String id, String body, Map<String,String> properties) throws IOException {
		//out.write("<node id=\""+toIdentifier(id)+"\">\n");
		String i = toIdentifier(id);
		for (Map.Entry<String, String> property : properties.entrySet()) {
		    nodesOut.write(property.getKey());
		    nodesOut.write(" ");
		    nodesOut.write(property.getValue());
		    nodesOut.write("_\t_");
		}
		nodesOut.write("\n");
	}

	@Override
	public final void writeEdge(String id1, String id2) throws IOException {
		writeEdge(id1, id2, (Map<String, String>)null);
	}

	public final void writeEdge(String id1, String id2, Map<String,String> properties) throws IOException {
		//out.write("<edge source=\""+toIdentifier(id1)+"\" target=\""+toIdentifier(id2)+"\">\n");
		String i1 = toIdentifier(id1);
		String i2 = toIdentifier(id2);
		edgesOut.write(i1 + "\t" + i2 + "\n");
	}

	@Override
	public void writeEdge(String id1, String id2, Color color)
			throws IOException {
		Map<String,String> map = new HashMap<String, String>();
		writeEdge(id1, id2, map);
	}

	@Override
	public final void writeEdge(String id1, String id2, String label) throws IOException {
		writeEdge(id1, id2, label, null);
	}

	@Override
	public final void writeEdge(String id1, String id2, String label, Color color) throws IOException {
		Map<String,String> map = new HashMap<String, String>();
		writeEdge(id1, id2, map);
	}

	@Override
	public void writeEdge(String id1, String id2, String label, Color color, 
			boolean weakEdge) throws IOException {
		writeEdge(id1, id2, label, color);		
	}

	private final String toIdentifier(String id) {
	    Integer value = idMap.get(id);
        if (value != null) {
        } else {
          value = count;
          idMap.put(id, value);
		  count++;
        }
        return value.toString();
	}

	@Override
	public String getFilename() {
		return filename;
	}
	
	private static String colorConvert(Color color) {
		return String.format("color=\"#%02x%02x%02x\"", color.getRed(), color.getGreen(), color.getBlue());
	}
}
