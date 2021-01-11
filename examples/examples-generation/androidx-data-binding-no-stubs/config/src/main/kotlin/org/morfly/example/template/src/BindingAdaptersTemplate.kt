package org.morfly.example.template.src

import org.morfly.bazelgen.analyzer.PackageName


fun binding_adapters_template(
    packageName: PackageName,
    adapterName: String
) = """
package $packageName;

import androidx.databinding.BindingAdapter;
import android.widget.TextView;


public class BindingAdapters {

    @BindingAdapter("$adapterName")
    public static void $adapterName(TextView view, String $adapterName) {
        view.setText($adapterName);
    }
}

""".trimIndent()