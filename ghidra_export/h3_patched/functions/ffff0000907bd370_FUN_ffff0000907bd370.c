// ffff0000907bd370  size=164  FUN_ffff0000907bd370

undefined8
FUN_ffff0000907bd370
          (undefined8 param_1,undefined8 param_2,undefined8 param_3,long param_4,undefined8 *param_5
          )

{
  ulong uVar1;
  undefined8 local_60;
  long lStack_58;
  undefined8 local_50;
  undefined8 local_48;
  
  if (param_4 == 0) {
    param_4 = thunk_FUN_ffff00009077ef88(param_3);
  }
  local_50 = 0;
  local_48 = 0;
  local_60 = param_3;
  lStack_58 = param_4;
  uVar1 = FUN_ffff0000907bc7fc(param_1,param_2,FUN_ffff0000907bd414,&local_60);
  if ((uVar1 & 1) == 0) {
    if (param_5 != (undefined8 *)0x0) {
      *param_5 = local_48;
    }
  }
  else {
    local_50 = 0;
    if (param_5 != (undefined8 *)0x0) {
      *param_5 = 0;
    }
  }
  return local_50;
}


