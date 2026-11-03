// File offset entry: 0xa4920

undefined8 FUN_020a4820(long param_1,uint param_2)

{
  undefined8 uVar1;
  
  if (param_1 == 0) {
    uVar1 = 0x100000;
  }
  else if ((iRam0000000002122c18 != 0) ||
          (uVar1 = func_0x020a43c8(uRam0000000002122c14), (int)uVar1 == 0)) {
    if (uRam0000000002122c44 < 3) {
      uVar1 = 0x100001;
      if ((((0x1f < param_2) || (uRam0000000002122c44 != 0)) &&
          ((0x2f < param_2 || (1 < uRam0000000002122c44 - 1)))) &&
         ((iRam0000000002122c18 != 0 ||
          (uVar1 = func_0x020a43c8(uRam0000000002122c14), (int)uVar1 == 0)))) {
        func_0x020915ac(param_1,0x2122c48,param_2);
        uVar1 = 0;
      }
    }
    else {
      uVar1 = 0x10101b;
    }
  }
  return uVar1;
}


