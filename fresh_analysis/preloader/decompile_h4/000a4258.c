// File offset entry: 0xa4258

undefined8
FUN_020a4158(undefined4 *param_1,undefined4 *param_2,undefined4 *param_3,undefined4 *param_4,
            undefined4 *param_5,undefined4 *param_6)

{
  undefined8 uVar1;
  
  uVar1 = 0x100006;
  if ((((param_1 != (undefined4 *)0x0) && (param_2 != (undefined4 *)0x0)) &&
      (param_3 != (undefined4 *)0x0)) &&
     (((param_4 != (undefined4 *)0x0 && (param_5 != (undefined4 *)0x0)) &&
      (param_6 != (undefined4 *)0x0)))) {
    uVar1 = 0;
    *param_1 = uRam0000000002122bfc;
    *param_2 = uRam0000000002122c00;
    *param_3 = uRam0000000002122c04;
    *param_4 = uRam0000000002122c08;
    *param_5 = uRam0000000002122c0c;
    *param_6 = uRam0000000002122c10;
  }
  return uVar1;
}


