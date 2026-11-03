// File offset entry: 0xa4868

undefined8 FUN_020a4768(undefined4 *param_1,int *param_2)

{
  undefined8 uVar1;
  
  uVar1 = 0x100000;
  if (((param_1 != (undefined4 *)0x0) && (param_2 != (int *)0x0)) &&
     ((iRam0000000002122c18 != 0 || (uVar1 = func_0x020a43c8(uRam0000000002122c14), (int)uVar1 == 0)
      ))) {
    if ((iRam0000000002122c24 == 1) && (iRam0000000002122c2c == 1)) {
      if (iRam0000000002122c28 == iRam0000000002122c30) {
        uVar1 = 0;
        *param_2 = iRam0000000002122c28;
        *param_1 = 1;
      }
      else {
        uVar1 = 0x104000;
      }
    }
    else {
      uVar1 = 0;
      *param_1 = 0;
      *param_2 = 0;
    }
  }
  return uVar1;
}


