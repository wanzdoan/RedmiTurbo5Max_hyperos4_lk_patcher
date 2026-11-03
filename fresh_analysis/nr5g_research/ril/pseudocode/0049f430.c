
/* getNrbByBw(int) */

undefined8 getNrbByBw(int param_1)

{
  if (param_1 < 100) {
    if (param_1 == 0xe) {
      return 6;
    }
    if (param_1 == 0x1e) {
      return 0xf;
    }
    if (param_1 == 0x32) {
      return 0x19;
    }
  }
  else {
    if (param_1 == 100) {
      return 0x32;
    }
    if (param_1 == 0x96) {
      return 0x4b;
    }
    if (param_1 == 200) {
      return 100;
    }
  }
  return 0;
}


