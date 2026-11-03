// ffff0000907c76f4  size=80  FUN_ffff0000907c76f4

void FUN_ffff0000907c76f4(undefined8 param_1,undefined8 param_2,int param_3)

{
  int local_2c;
  ulong local_28 [2];
  int *local_18;
  
  local_2c = -param_3;
  if (-1 < param_3) {
    local_2c = param_3;
  }
  local_18 = &local_2c;
  local_28[1] = 1;
  local_28[0] = (ulong)(ushort)(param_3 >> 0x1f) | 1;
  FUN_ffff0000907c8358(param_1,param_2,local_28);
  return;
}


