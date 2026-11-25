use deunicode::deunicode;
use uuid::Uuid;

pub fn generate_uuid() -> String {
    Uuid::new_v4().to_string()
}

pub fn generate_uuids(count: u32) -> Vec<String> {
    let count = count.clamp(1, 100);
    (0..count).map(|_| generate_uuid()).collect()
}

pub fn generate_slug(input: &str) -> String {
    let transliterated = deunicode(input).to_lowercase();
    let mut slug = String::with_capacity(transliterated.len());
    let mut pending_separator = false;

    for character in transliterated.chars() {
        if character.is_ascii_alphanumeric() {
            if pending_separator && !slug.is_empty() {
                slug.push('-');
            }
            slug.push(character);
            pending_separator = false;
        } else if character == '\'' || character == '’' {
            // Apostrophes are dropped without creating a separator: "can't" -> "cant".
        } else {
            pending_separator = true;
        }
    }

    slug
}

#[cfg(test)]
mod tests {
    use std::collections::HashSet;

    use super::*;

    #[test]
    fn generates_uuid_v4() {
        let value = generate_uuid();
        let parsed = Uuid::parse_str(&value).expect("UUID should parse");

        assert_eq!(parsed.get_version_num(), 4);
        assert_eq!(parsed.get_variant(), uuid::Variant::RFC4122);
    }

    #[test]
    fn generates_requested_number_of_uuids() {
        assert_eq!(generate_uuids(5).len(), 5);
    }

    #[test]
    fn clamps_uuid_count() {
        assert_eq!(generate_uuids(0).len(), 1);
        assert_eq!(generate_uuids(1_000).len(), 100);
    }

    #[test]
    fn generated_uuids_are_unique() {
        let values = generate_uuids(10);
        let unique: HashSet<&String> = values.iter().collect();

        assert_eq!(unique.len(), values.len());
    }

    #[test]
    fn slugifies_basic_text() {
        assert_eq!(generate_slug("Hello World"), "hello-world");
        assert_eq!(generate_slug("  Hello   World  "), "hello-world");
    }

    #[test]
    fn slugifies_punctuation_and_separators() {
        assert_eq!(generate_slug("Hello, World!"), "hello-world");
        assert_eq!(generate_slug("foo_bar--baz"), "foo-bar-baz");
        assert_eq!(generate_slug("--Already-Slug--"), "already-slug");
        assert_eq!(generate_slug("foo.bar/baz"), "foo-bar-baz");
        assert_eq!(generate_slug("can't stop"), "cant-stop");
        assert_eq!(generate_slug("C++"), "c");
    }

    #[test]
    fn transliterates_accented_text() {
        assert_eq!(generate_slug("Café déjà vu"), "cafe-deja-vu");
        assert_eq!(generate_slug("Straße & Co."), "strasse-co");
    }

    #[test]
    fn handles_empty_and_non_latin_text() {
        assert_eq!(generate_slug(""), "");
        assert_eq!(generate_slug("日本語"), "ri-ben-yu");
    }
}
