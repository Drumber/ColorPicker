# ColorPicker
Java Swing ColorPicker  
![ColorPicker Dialog](https://user-images.githubusercontent.com/29163322/80603486-c700c080-8a30-11ea-994e-f602436075d2.png)

## Usage
#### Dialog
```java
// create a new basic color picker dialog
// this will block the thread until the user clicks Ok or closes the dialog
Color selected = ColorPicker.showDialog(String title, Color initialColor);

// create a new customized color picker dialog
Color selected = ColorPicker.showDialog(String title, String textOk, String textCancel, Dimension size, Color initialColor);
```

#### Use as Component
```java
// create a new color picker panel
ColorPicker cp = new ColorPicker(Color initialColor);
// add it to a JPanel (or any other swing component)
myJPanel.add(cp);

// add change listener
cp.addColorListener(new ColorListener() {
  @Override
	public void onColorChanged(Color color) {
    // do what you want
	  myJPanel.setBackground(color);
	}
});

// or simply get the selected color
Color myColor = cp.getColor();
```

## Download
#### Maven
Dependency:
```xml
<dependency>
  <groupId>com.github.Drumber</groupId>
  <artifactId>ColorPicker</artifactId>
  <version>v0.3</version>
</dependency> 
```
JitPack repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
    </repository>
</repositories>
```

#### Download Jar
The jar can be downloaded directly without Maven from the [GitHub Packages page](https://github.com/Drumber/ColorPicker/packages/204555).