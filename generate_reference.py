#!/usr/bin/env python3
import json

# Load schema
with open('/home/bustamante/AndroidStudioProjects/SeijakuList/anilist_schema.json', 'r') as f:
    data = json.load(f)

schema = data['data']['__schema']
types = {t['name']: t for t in schema['types'] if not t['name'].startswith('__')}

# Define types we're interested in
important_types = [
    'Query',
    'Mutation',
    'Media',
    'MediaTitle',
    'MediaCoverImage',
    'MediaTrailer',
    'MediaExternalLink',
    'MediaTag',
    'MediaStats',
    'MediaRank',
    'MediaList',
    'Character',
    'CharacterName',
    'CharacterImage',
    'Staff',
    'StaffName',
    'StaffImage',
    'Studio',
    'AiringSchedule',
    'User',
    'MediaTrend',
    'Review',
    'Recommendation',
    'FuzzyDate'
]

# Important enums
important_enums = [
    'MediaType',
    'MediaFormat',
    'MediaStatus',
    'MediaSeason',
    'MediaSource',
    'MediaSort',
    'MediaListStatus',
    'CharacterRole',
    'StaffLanguage'
]

output = "# AniList GraphQL API - Referencia Rápida\n\n"
output += "Este documento contiene los tipos más importantes del API de AniList para desarrollo.\n\n"
output += "## Archivos Generados\n\n"
output += "- `anilist_schema.json` - Schema completo en formato JSON (introspection)\n"
output += "- `anilist_schema.graphql` - Schema completo en formato GraphQL SDL (legible)\n"
output += "- Este archivo - Referencia rápida de tipos principales\n\n"

output += "---\n\n"
output += "## TIPOS PRINCIPALES\n\n"

for type_name in important_types:
    if type_name not in types:
        continue

    type_def = types[type_name]
    output += f"### {type_name}\n\n"

    if type_def.get('description'):
        output += f"*{type_def['description']}*\n\n"

    fields = type_def.get('fields', [])
    if fields:
        output += "**Campos principales:**\n\n"
        for field in fields[:30]:  # Limit to first 30 fields
            field_name = field['name']
            field_type = field['type']

            # Simplify type representation
            type_str = ""
            current = field_type
            is_list = False
            is_required = False

            while current:
                if current['kind'] == 'NON_NULL':
                    is_required = True
                    current = current.get('ofType')
                elif current['kind'] == 'LIST':
                    is_list = True
                    current = current.get('ofType')
                else:
                    type_str = current.get('name', '')
                    break

            if is_list:
                type_str = f"[{type_str}]"
            if is_required:
                type_str += "!"

            desc = field.get('description', '')
            if desc:
                output += f"- **{field_name}**: `{type_str}` - {desc}\n"
            else:
                output += f"- **{field_name}**: `{type_str}`\n"

        if len(fields) > 30:
            output += f"\n*... y {len(fields) - 30} campos más (ver schema completo)*\n"

    output += "\n---\n\n"

output += "## ENUMS PRINCIPALES\n\n"

for enum_name in important_enums:
    if enum_name not in types:
        continue

    type_def = types[enum_name]
    output += f"### {enum_name}\n\n"

    if type_def.get('description'):
        output += f"*{type_def['description']}*\n\n"

    values = type_def.get('enumValues', [])
    if values:
        output += "**Valores:**\n\n"
        for value in values:
            name = value['name']
            desc = value.get('description', '')
            if desc:
                output += f"- `{name}` - {desc}\n"
            else:
                output += f"- `{name}`\n"

    output += "\n---\n\n"

# Add some useful queries examples
output += "## QUERIES ÚTILES\n\n"
output += "### Buscar Anime por ID\n\n"
output += "```graphql\n"
output += """query GetAnimeById($id: Int) {
  Media(id: $id, type: ANIME) {
    id
    title {
      romaji
      english
      native
    }
    description
    coverImage {
      large
      medium
    }
    bannerImage
    genres
    averageScore
    episodes
    status
    season
    seasonYear
  }
}
"""
output += "```\n\n"

output += "### Buscar Characters\n\n"
output += "```graphql\n"
output += """query GetCharacter($id: Int) {
  Character(id: $id) {
    id
    name {
      full
      native
    }
    image {
      large
      medium
    }
    description
    gender
    dateOfBirth {
      year
      month
      day
    }
  }
}
"""
output += "```\n\n"

output += "## ENDPOINTS\n\n"
output += "- **GraphQL API**: `https://graphql.anilist.co`\n"
output += "- **GraphiQL Explorer**: `https://anilist.co/graphiql`\n"
output += "- **Documentación**: `https://docs.anilist.co`\n\n"

output += "## RATE LIMITS\n\n"
output += "- **Rate Limit**: 90 requests per minute\n"
output += "- **Authentication**: No se requiere para queries públicas\n"
output += "- **Headers**: Se recomienda usar `Content-Type: application/json` y `Accept: application/json`\n"

# Save
with open('/home/bustamante/AndroidStudioProjects/SeijakuList/ANILIST_API_REFERENCE.md', 'w') as f:
    f.write(output)

print("Documento de referencia creado exitosamente!")
print(f"Total de caracteres: {len(output)}")
