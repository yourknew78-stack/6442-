# Question 4: File persistence, Tree Data Structure (20 Marks)


## Tasks

Read and process location data stored in XML format using the `org.w3c.dom` and `javax.xml` libraries.

### Task A: Loading a flat XML

**File Structure**:

The XML contains a `<Locations>` as the root. Each direct child element of `<Locations>` 
represents one **location**, and each of them must have a String `name` attribute, and can
optionally have exactly one inner `<Population>` element which contains an integer.

A sample XML representation is:

```xml
<?xml version="1.0" encoding="utf-8" standalone="no"?>
<Locations>
    <State name="New South Wales">
        <Population>8545100</Population>
    </State>
    <State name="Victoria">
        <Population>7011100</Population>
    </State>
    <State name="Tasmania">
    </State>
</Locations>
```

**Task**: Complete `loadPopulationInfo` (in the `LocationLoader` class), which reads the input `File` and returns a mapping of location names to their populations.
Each direct child element of `<Locations>` represents one location, and the tag name is arbitrary (it does not need to be `<State>`).
- Note that the `<Population>` tag is optional (see the XML above for the Tasmanian state), and the value in the map should be `null` if it is missing or blank.
- Return `null` if there is an error (e.g., wrong file path, file does not exist, malformed XML, or if the XML file does not follow the mentioned format).
- The basic test cases given can assist with your code development and understanding. 


### Task B: Loading a hierarchical XML

Load a hierarchical XML of locations into a tree of Location objects.

The XML contains a `<Locations>` element as the root, with each (direct or indirect) child element representing a location.
The tag name of each element within the `<Locations>` element indicates the location type (e.g., `<Country>`, `<State>`, `<Region>`, and `<City>`, but not limited to these), while the value of the name attribute specifies its name.
Locations can be structured hierarchically to reflect their spatial relationships (e.g., in this case, a state is within a country, and a state contains regions that contain cities). Here’s an example:


```xml
<?xml version="1.0" encoding="utf-8" standalone="no"?>
<Locations>
    <Country name="Australia" capital="Canberra" population="26000000">
        <State name="Tasmania">
            <Region name="South">
                <City name="Hobart"/>
            </Region>
            <Region name="North">
                <City name="Launceston"/>
            </Region>
        </State>
        <State name="New South Wales">
        </State>
    </Country>
</Locations>
```

**Task**: Complete `loadLocations` (in the `LocationLoader` class), which reads the input `File` and returns a set of `Location` instances 
(see the `Location` class) that are direct children element of `<Locations>`. Each of these top-level Location objects 
must correctly contain its own nested child Location instances according to the XML hierarchy.

- If the name attribute is missing from an element, skip the entire element (i.e., no need to create a `Location` instance for it).
- The population attribute is optional; set it to `null` if it is missing or blank.
- Ignore any additional tags and attributes.
- Return `null` if there is an error (e.g., file does not exist, malformed XML, or if the XML file does not follow the mentioned format).

  For the example above, instances of Location in the set include `Australia`, `Tasmania`, `South`, `Hobart`, `North`, `Launceston`, and `New South Wales`,
  and their hierarchical (parent-children) relationship can be shown as:
  ```
  Australia 
      Tasmania
          South
              Hobart
          North
              Launceston
      New South Wales
  ```


-----------
#### Hints:

When working with a Document (d), Element (e), or Node (n) from org.w3c.dom, 
the following methods may be useful for handling hierarchical XML data:
- `d.getElementsByTagName("*")` (you may refer to the docs in `Q4/Element.pdf`)
- `n.getParentNode()` (you may refer to the docs in `Q4/Node.pdf`)
- `e.getChildNodes()` (you may refer to the docs in `Q4/Node.pdf`)

-----------

#### Additional Notes:
- Read the `Location` class to understand what a `Location` instance is.
- You may import and use the org.w3c.dom and javax.xml packages.
- You may create helper methods only within files **in the submission list**, 
  but you may **NOT create** additional fields.
- You must not modify any field declarations or method signatures, as doing so will result in receiving 
  zero marks for the entire question. This means that you can not add, for example, 
  `throw IOException` to any methods.
- You must not change any existing methods, except for those explicitly marked
  as editable by comments. Only modify sections of code that are clearly
  indicated and delimited by these comments.
- The basic test cases given can assist with your code development and understanding. 
<br><br>


* * *


#### Submission: commit and push your updates in the following files to the GitLab exam repository:
* `LocationLoader.java`
