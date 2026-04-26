CREATE OR REPLACE FUNCTION notify_update() RETURNS TRIGGER AS $$
BEGIN
    EXECUTE 'NOTIFY ticket_action_channel, ''' || NEW.id::text || '''';
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_trigger
    AFTER INSERT ON tickets_action
    FOR EACH ROW EXECUTE FUNCTION notify_update();
