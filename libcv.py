import cv2, time
from PIL import ImageGrab
import numpy as np

class Mod:
    def __init__(self, mod_name, function, *args, **kwargs):        
        self.mod_name = mod_name
        self.function = function
        self.args = args
        self.kwargs = kwargs    
        self.draw_guis = True

    def apply_gui(self, title):
        if self.draw_guis:
            for i, arg in enumerate(self.args):
                if isinstance(arg, tuple) and len(arg) == 3:
                    cv2.createTrackbar('r', title, 0, 255, lambda x: print(x))    



class LibCV:
    def __init__(self, title='Test', mods=[]):
        self.title = title
        self.mods = mods
        self.gui_applied = False

    def apply_guis(self):
        for mod in self.mods:
            mod.apply_gui(self.title)
        self.gui_applied = True

    def get_screenshot(self, as_np_array=True):
        screenshot = ImageGrab.grab()
        if as_np_array:
            return np.array(screenshot)
        return screenshot

    def get_screen_cv_image(self):
        screenshot = self.get_screenshot(as_np_array=True)
        img = cv2.cvtColor(screenshot, cv2.COLOR_RGB2BGR)
        return img    

    def show_screenshot(self):
        img = self.get_screen_cv_image()
        img = self.apply_mods(img)
        cv2.imshow(self.title, img)       

        # if not self.gui_applied:
        #     self.apply_guis() 
            
    def apply_mods(self, img):
        for mod in self.mods:
            try:            
                img = mod.function(img, *mod.args, **mod.kwargs)
            except Exception as e:
                print(f"MOD {mod.mod_name} FAILED: " + str(e))
        return img

    def show_screen_loop(self):
        print('hit esc to quit..')
        while True:            
            self.show_screenshot()
            if cv2.waitKey(33) & 0xFF in (ord('q'), 27):
                break

    ######### Util #######
    def rgb_to_bgr(self, rgb=(0,0,0)):  
        return (rgb[2], rgb[1], rgb[0])


    ######### Image Drawings ##########
    ''' Draw a rectangle on a cv2 image
        @arg start - an x and y coordinate
        @arg end - an x and y coordinate
        @arg color - a BGR value
        @arg thickness - thickness of the line, pass -1 to fill the rectangle
    '''
    def draw_rect(self, img, start=(0,0), end=(100,100), color=(0,0,0), thickness=1):
        return cv2.rectangle(img, start, end, color, thickness)

    def filter_by_color(self, img, rgb_start=(0,0,0), rgb_end=(100,100,100)):        
        lower = np.array(self.rgb_to_bgr(rgb_start), dtype="uint8")
        upper = np.array(self.rgb_to_bgr(rgb_end), dtype="uint8")
        mask = cv2.inRange(img, lower, upper)
        return cv2.bitwise_and(img, img, mask=mask)
        
        
def on_change(value):
    print(value)


if __name__ == '__main__':
    self = LibCV()
    m1 = Mod('Test Rect', self.draw_rect, start=(100,100), end=(500,500), color=(100,100,100)) 
    m2 = Mod('Test RGB Filter', self.filter_by_color, rgb_start=(0,0,0), rgb_end=(255,255,150))
    self.mods = [m1, m2]
    self.show_screen_loop()
    # self.show_screen_loop()


# screenshot = ImageGrab.grab()
# img = cv2.cvtColor(np.array(screenshot), cv2.COLOR_RGB2BGR)
# cv2.imshow('Screenshot', img)

# cv2.waitKey(0)
# cv2.destroyAllWindows()
