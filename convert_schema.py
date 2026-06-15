#!/usr/bin/env python3
"""
Script to convert AniList GraphQL introspection JSON to readable SDL format
"""
import json
import sys

def type_ref_to_string(type_ref):
    """Convert a type reference to GraphQL SDL string"""
    if type_ref is None:
        return ""

    kind = type_ref.get('kind')
    name = type_ref.get('name')
    of_type = type_ref.get('ofType')

    if kind == 'NON_NULL':
        return f"{type_ref_to_string(of_type)}!"
    elif kind == 'LIST':
        return f"[{type_ref_to_string(of_type)}]"
    else:
        return name or ""

def format_description(description, indent=""):
    """Format description as GraphQL comment"""
    if not description:
        return ""
    lines = description.strip().split('\n')
    if len(lines) == 1:
        return f'{indent}"""{lines[0]}"""\n'
    result = f'{indent}"""\n'
    for line in lines:
        result += f'{indent}{line}\n'
    result += f'{indent}"""\n'
    return result

def format_field(field, indent="  "):
    """Format a field definition"""
    result = ""
    if field.get('description'):
        result += format_description(field['description'], indent)

    name = field['name']
    args = field.get('args', [])
    return_type = type_ref_to_string(field['type'])

    if args:
        args_str = ", ".join([
            f"{arg['name']}: {type_ref_to_string(arg['type'])}" +
            (f" = {arg['defaultValue']}" if arg.get('defaultValue') else "")
            for arg in args
        ])
        result += f"{indent}{name}({args_str}): {return_type}\n"
    else:
        result += f"{indent}{name}: {return_type}\n"

    return result

def format_enum_value(enum_value, indent="  "):
    """Format an enum value"""
    result = ""
    if enum_value.get('description'):
        result += format_description(enum_value['description'], indent)
    result += f"{indent}{enum_value['name']}\n"
    return result

def format_input_value(input_value, indent="  "):
    """Format an input field"""
    result = ""
    if input_value.get('description'):
        result += format_description(input_value['description'], indent)

    name = input_value['name']
    type_str = type_ref_to_string(input_value['type'])
    default = input_value.get('defaultValue')

    if default:
        result += f"{indent}{name}: {type_str} = {default}\n"
    else:
        result += f"{indent}{name}: {type_str}\n"

    return result

def convert_schema_to_sdl(introspection_result):
    """Convert introspection JSON to GraphQL SDL"""
    schema = introspection_result['data']['__schema']
    types = schema['types']

    sdl = "# AniList GraphQL API Schema\n"
    sdl += "# Generated from introspection query\n\n"

    # Separate types by kind
    objects = []
    interfaces = []
    unions = []
    enums = []
    input_objects = []
    scalars = []

    for type_def in types:
        name = type_def.get('name', '')
        # Skip internal GraphQL types
        if name.startswith('__'):
            continue

        kind = type_def['kind']
        if kind == 'OBJECT':
            objects.append(type_def)
        elif kind == 'INTERFACE':
            interfaces.append(type_def)
        elif kind == 'UNION':
            unions.append(type_def)
        elif kind == 'ENUM':
            enums.append(type_def)
        elif kind == 'INPUT_OBJECT':
            input_objects.append(type_def)
        elif kind == 'SCALAR':
            scalars.append(type_def)

    # Format scalars
    if scalars:
        sdl += "# ===== SCALARS =====\n\n"
        for scalar in sorted(scalars, key=lambda x: x['name']):
            if scalar.get('description'):
                sdl += format_description(scalar['description'])
            sdl += f"scalar {scalar['name']}\n\n"

    # Format enums
    if enums:
        sdl += "# ===== ENUMS =====\n\n"
        for enum in sorted(enums, key=lambda x: x['name']):
            if enum.get('description'):
                sdl += format_description(enum['description'])
            sdl += f"enum {enum['name']} {{\n"
            for value in enum.get('enumValues', []):
                sdl += format_enum_value(value)
            sdl += "}\n\n"

    # Format input objects
    if input_objects:
        sdl += "# ===== INPUT TYPES =====\n\n"
        for input_obj in sorted(input_objects, key=lambda x: x['name']):
            if input_obj.get('description'):
                sdl += format_description(input_obj['description'])
            sdl += f"input {input_obj['name']} {{\n"
            for field in input_obj.get('inputFields', []):
                sdl += format_input_value(field)
            sdl += "}\n\n"

    # Format interfaces
    if interfaces:
        sdl += "# ===== INTERFACES =====\n\n"
        for interface in sorted(interfaces, key=lambda x: x['name']):
            if interface.get('description'):
                sdl += format_description(interface['description'])
            sdl += f"interface {interface['name']} {{\n"
            for field in interface.get('fields', []):
                sdl += format_field(field)
            sdl += "}\n\n"

    # Format unions
    if unions:
        sdl += "# ===== UNIONS =====\n\n"
        for union in sorted(unions, key=lambda x: x['name']):
            if union.get('description'):
                sdl += format_description(union['description'])
            possible_types = union.get('possibleTypes', [])
            types_str = " | ".join([t['name'] for t in possible_types])
            sdl += f"union {union['name']} = {types_str}\n\n"

    # Format objects (including Query and Mutation)
    if objects:
        sdl += "# ===== TYPES =====\n\n"
        # Put Query and Mutation first
        query_mutation = [t for t in objects if t['name'] in ['Query', 'Mutation']]
        other_objects = [t for t in objects if t['name'] not in ['Query', 'Mutation']]

        for obj in query_mutation + sorted(other_objects, key=lambda x: x['name']):
            if obj.get('description'):
                sdl += format_description(obj['description'])

            name = obj['name']
            interfaces = obj.get('interfaces', [])
            if interfaces:
                implements = " implements " + " & ".join([i['name'] for i in interfaces])
                sdl += f"type {name}{implements} {{\n"
            else:
                sdl += f"type {name} {{\n"

            for field in obj.get('fields', []):
                sdl += format_field(field)
            sdl += "}\n\n"

    return sdl

# Read introspection JSON
with open('/home/bustamante/AndroidStudioProjects/SeijakuList/anilist_schema.json', 'r') as f:
    introspection = json.load(f)

# Convert to SDL
sdl = convert_schema_to_sdl(introspection)

# Write to file
with open('/home/bustamante/AndroidStudioProjects/SeijakuList/anilist_schema.graphql', 'w') as f:
    f.write(sdl)

print(f"Schema converted successfully!")
print(f"Output: anilist_schema.graphql ({len(sdl)} characters)")
