package com.example.solvers.utils;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.Executors;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.latex.JLatexMathPlugin;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;

public class MarkwonBuilder {
    public static Markwon build(Context context, float textSize){
        return Markwon.builder(context)
                .usePlugin(MarkwonInlineParserPlugin.create())
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(HtmlPlugin.create())
                .usePlugin(JLatexMathPlugin.create(textSize, builder -> {
                    // enable inlines (require `MarkwonInlineParserPlugin`), by default `false`
                    builder.inlinesEnabled(true);

                    // use pre-4.3.0 LaTeX block parsing (by default `false`)
                    builder.blocksLegacy(true);

                    // by default true
                    builder.blocksEnabled(true);

                    // @since 4.3.0
                    builder.errorHandler((latex, error) -> {
                        Log.e("latex","latex: "+latex+"\nerror: "+error);
                        error.printStackTrace();
                        return null;
                    });

                    // executor on which parsing of LaTeX is done (by default `Executors.newCachedThreadPool()`)
                    builder.executorService(Executors.newCachedThreadPool());
                }))
                .build();
    }
}
