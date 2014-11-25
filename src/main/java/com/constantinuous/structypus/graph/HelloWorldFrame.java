package com.constantinuous.structypus.graph;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

//import statements
//Check if window closes automatically. Otherwise add suitable code
public class HelloWorldFrame extends JFrame {

    private JGraphModelAdapter m_jgAdapter;

    public static void main(String args[]) {
        new HelloWorldFrame();
    }

    public HelloWorldFrame() {
        init();
        JGraph graph = new JGraph();

        JGraph jgraph = new JGraph(m_jgAdapter);

        JLabel jlbHelloWorld = new JLabel("Hello World");
        add(jgraph);
        this.setSize(100, 100);
        // pack();
        setVisible(true);
    }

    /**
     * @see java.applet.Applet#init().
     */
    public void init() {

        // create a JGraphT graph
        ListenableGraph g = new ListenableDirectedGraph(DefaultEdge.class);

        // create a visualization using JGraph, via an adapter
        m_jgAdapter = new JGraphModelAdapter(g);

        // add some sample data (graph manipulated via JGraphT)
        g.addVertex("v1");
        g.addVertex("v2");
        g.addVertex("v3");
        g.addVertex("v4");

        g.addEdge("v1", "v2");
        g.addEdge("v2", "v3");
        g.addEdge("v3", "v1");
        g.addEdge("v4", "v3");

        // position vertices nicely within JGraph component
        positionVertexAt("v1", 130, 40);
        positionVertexAt("v2", 60, 200);
        positionVertexAt("v3", 310, 230);
        positionVertexAt("v4", 380, 70);

        // that's all there is to it!...
    }

    private void positionVertexAt(Object vertex, int x, int y) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
        Map attr = cell.getAttributes();
        Rectangle2D b = GraphConstants.getBounds(attr);

        GraphConstants.setBounds(attr, new Rectangle(x, y, (int) b.getWidth(), (int) b.getHeight()));

        Map cellAttr = new HashMap();
        cellAttr.put(cell, attr);
        m_jgAdapter.edit(cellAttr, null, null, null);
    }
}
