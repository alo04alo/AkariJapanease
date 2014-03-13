package akari.jp.base;

public class Xml {
	public Xml() {
		name = new XmlContent();
		data = new XmlContent();
		firstChild = null;
		lastChild = null;
		next = null;
		valid = false;
		attribs = null;
	}

	public Xml(String doc) {
		setDoc(doc);
	}

	public Xml getChild() {
		return firstChild;
	}

	public Xml getChild(String name) {
		Xml xml = firstChild;
		while (xml != null) {
			if (xml.name.text.equalsIgnoreCase(name))
				return xml;
			xml = xml.next;
		}
		return null;
	}

	public Xml getNext() {
		return next;
	}

	public Xml getNext(String name) {
		Xml xml = next;
		while (xml != null) {
			if (xml.name.text.equalsIgnoreCase(name))
				return xml;
			xml = xml.next;
		}
		return null;
	}

	public String getName() {
		return name.text;
	}

	public String getAttrib(String name) {
		XmlAttrib attrib = attribs;
		while (attrib != null) {
			if (attrib.name.equalsIgnoreCase(name)) {
				return attrib.value;
			}
			attrib = attrib.next;
		}
		return null;
	}

	public String getData() {
		return data.text;
	}

	boolean isValid() {
		return valid;
	}

	private static final int PARSE_STATE_WAIT = 0;
	private static final int PARSE_STATE_OPENING = 1;
	private static final int PARSE_STATE_OPENED = 2;
	private static final int PARSE_STATE_CLOSING = 3;
	private static final int PARSE_STATE_COMPLETE = 4;

	private static final int ATTRIB_STATE_NAME = 0;
	private static final int ATTRIB_STATE_EQUAL = 1;
	private static final int ATTRIB_STATE_QUOTE = 2;
	private static final int ATTRIB_STATE_VALUE = 3;

	private XmlContent name;
	private XmlContent data;
	private Xml firstChild = null;
	private Xml lastChild = null;
	private Xml next = null;
	private boolean valid = false;
	private XmlAttrib attribs;

	private class XmlAttrib {
		public String name;
		public String value;
		XmlAttrib next;

		public XmlAttrib(String name, String value, XmlAttrib next) {
			this.name = name;
			this.value = value;
			this.next = next;
		}
	}

	private class XmlContent {
		public String text;
		public int index;

		public XmlContent() {
			text = null;
			index = 0;
		}

		public XmlContent(String text) {
			this.text = text;
			this.index = 0;
		}

		public boolean incomplete() {
			return index < text.length();
		}

		public void next() {
			index++;
		}

		public boolean startWith(String head) {
			XmlContent headContent = new XmlContent(head);
			int check = index;
			while (headContent.incomplete()) {
				if (text.charAt(check) != headContent.head())
					return false;
				check++;
				headContent.next();
			}
			index = check;
			return true;
		}

		public void skip(String head) {
			XmlContent check = new XmlContent(head);
			while (incomplete()) {
				if (check.head() == this.head()) {
					check.next();
					this.next();
					if (!check.incomplete())
						return;
				} else if (check.index != 0) {
					check.index = 0;
				} else
					this.next();
			}
		}

		public boolean extract(int start, int end, XmlContent text) {
			text.text = this.text.substring(start, end);
			return true;
		}

		public boolean makeWord(XmlContent word) {
			int wordIndex = 0;
			while (this.incomplete()) {
				if ((this.head() >= 'a' && this.head() <= 'z')
						|| (this.head() >= 'A' && this.head() <= 'Z')
						|| (this.head() >= '0' && this.head() <= '9')) {
					if (wordIndex == 0)
						wordIndex = this.index;

				} else {
					if (wordIndex != 0)
						return this.extract(wordIndex, this.index, word);
					else if (this.head() == '<' || this.head() == '>'
							|| this.head() == '/')
						return false;
				}
				this.next();
			}
			return false;
		}

		public char head() {
			return text.charAt(index);
		}
	}

	private boolean setDoc(String doc) {
		name = new XmlContent();
		data = new XmlContent();
		firstChild = null;
		lastChild = null;
		next = null;
		valid = false;
		attribs = null;
		if (doc != null)
			valid = parse(new XmlContent(doc));
		return valid;
	}

	boolean parse(XmlContent content) {
		int state = PARSE_STATE_WAIT;
		int dataIndex = 0;
		while (content.incomplete()) {
			if (content.startWith("<?")) {
				content.skip("?>");
				continue;
			} else if (content.startWith("<!--")) {
				content.skip("-->");
				continue;
			} else if (content.startWith("<![CDATA[")) {
				content.skip("]]>");
				continue;
			}
			switch (state) {
			case PARSE_STATE_WAIT:
				if (content.head() == '<') {
					content.next();
					content.makeWord(this.name);
					state = PARSE_STATE_OPENING;
					continue;
				}
				break;
			case PARSE_STATE_OPENING:
				if (content.head() == '>') {
					state = PARSE_STATE_OPENED;
					dataIndex = content.index + 1;
				} else if (content.head() == '/' && content.startWith("/>"))
					return true;
				else if (!extractAttribs(content))
					return false;
				else
					continue;
				break;
			case PARSE_STATE_OPENED:
				if (content.head() == '<') {
					if (content.startWith("</")) {
						if (firstChild == null) {
							content.extract(dataIndex, content.index - 2,
									this.data);
							this.data.text = this.data.text.trim();
						}
						XmlContent endName = new XmlContent();
						content.makeWord(endName);
						if (!endName.text.equals(this.name.text))
							return false;
						state = PARSE_STATE_CLOSING;
						continue;
					} else {
						Xml child = new Xml();
						if (child.parse(content)) {
							if (lastChild != null) {
								lastChild.next = child;
								lastChild = child;
							} else {
								firstChild = child;
								lastChild = child;
							}
							continue;
						} else {
							return false;
						}
					}
				}
				break;
			case PARSE_STATE_CLOSING:
				if (content.head() == '>')
					state = PARSE_STATE_COMPLETE;
				break;
			case PARSE_STATE_COMPLETE:
				return true;
			}
			content.next();
		}
		this.valid = state == PARSE_STATE_COMPLETE;
		return valid;
	}

	boolean extractAttribs(XmlContent text) {
		XmlContent name = new XmlContent();
		XmlContent value = new XmlContent();
		int valueIndex = 0;
		int state = ATTRIB_STATE_NAME;
		while (text.incomplete()) {
			switch (state) {
			case ATTRIB_STATE_NAME:
				if (!text.makeWord(name))
					return true;
				state = ATTRIB_STATE_EQUAL;
				continue;
			case ATTRIB_STATE_EQUAL:
				if (text.head() == '=') {
					state = ATTRIB_STATE_QUOTE;
				}
				break;
			case ATTRIB_STATE_QUOTE:
				if (text.head() == '"') {
					state = ATTRIB_STATE_VALUE;
					valueIndex = text.index + 1;
				}
				break;
			case ATTRIB_STATE_VALUE:
				if (text.head() == '"') {
					if (text.extract(valueIndex, text.index, value))
						attribs = new XmlAttrib(name.text, value.text, attribs);
					else
						return false;
					state = ATTRIB_STATE_NAME;
				}
				break;
			}
			text.next();
		}
		return false;
	}

}
