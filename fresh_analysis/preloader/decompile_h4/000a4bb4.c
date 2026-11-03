// File offset entry: 0xa4bb4

undefined8 FUN_020a4ab4(undefined8 param_1,int param_2)

{
  undefined8 uVar1;
  
  if (param_2 == 0x200) {
    func_0x020915ac(0x2122f58,param_1,0x200);
    uVar1 = 2;
  }
  else if (param_2 == 0x180) {
    func_0x020915ac(0x2122dd8,param_1,0x180);
    uVar1 = 1;
  }
  else {
    if (param_2 != 0x100) {
      return 0x10101a;
    }
    func_0x020915ac(0x2122cd8,param_1,0x100);
    uVar1 = 0;
  }
  FUN_020a54d0(uVar1);
  uRam0000000002123158 = 1;
  return 0;
}


