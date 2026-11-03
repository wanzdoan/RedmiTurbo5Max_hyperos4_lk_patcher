
undefined8
FUN_ffff0000be0905f8
          (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
          undefined8 param_5,undefined8 param_6,undefined8 param_7,ulong param_8,ulong *param_9)

{
  undefined8 uVar1;
  undefined8 local_58;
  ulong local_50;
  ulong local_48;
  
  local_48 = 0;
  local_58 = param_7;
  local_50 = param_8;
  FUN_ffff0000be0893ac(4,&local_58);
  FUN_ffff0000be08924c(param_2,param_1,&local_58);
  FUN_ffff0000be0893c4(0,&local_58);
  FUN_ffff0000be08924c(param_4,param_3,&local_58);
  FUN_ffff0000be08924c(param_6,param_5,&local_58);
  uVar1 = 0x10d002;
  if ((local_48 != 0xffffffffffffffff) && (local_48 <= local_50)) {
    uVar1 = 0;
    *param_9 = local_48;
  }
  return uVar1;
}


