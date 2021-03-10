package broccolai.tickets.api.model.message;

import net.kyori.adventure.text.minimessage.Template;
import net.kyori.examination.Examinable;

import java.util.List;

public interface Templatable extends Examinable {

    List<Template> templates();

}
