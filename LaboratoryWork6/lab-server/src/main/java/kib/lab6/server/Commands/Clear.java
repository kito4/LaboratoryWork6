package kib.lab6.server.Commands;

import kib.lab6.common.util.Request;
import kib.lab6.common.util.SuccessMessage;
import kib.lab6.server.Config;
import kib.lab6.server.abstractions.AbstractCommand;
import kib.lab6.common.util.SuccessMessage;

public class Clear extends AbstractCommand {

    public Clear() {
        super("clear", "Очистить коллекцию");
    }

    @Override
    public Object execute(Request request) {
        Config.getCollectionManager().clearCollection();
        return new SuccessMessage("Коллекция успешно очищена");
    }
}