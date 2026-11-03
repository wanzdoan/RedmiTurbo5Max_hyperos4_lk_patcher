// File offset entry: 0xa4468

undefined8 FUN_020a4368(undefined4 *param_1)

{
  undefined8 uVar1;
  
  if (param_1 == (undefined4 *)0x0) {
    uVar1 = 0x100000;
  }
  else {
    *param_1 = 0;
    if ((iRam0000000002122c18 != 0) || (uVar1 = FUN_020a43c8(uRam0000000002122c14), (int)uVar1 == 0)
       ) {
      uVar1 = 0;
      *param_1 = uRam0000000002122c40;
    }
  }
  return uVar1;
}


