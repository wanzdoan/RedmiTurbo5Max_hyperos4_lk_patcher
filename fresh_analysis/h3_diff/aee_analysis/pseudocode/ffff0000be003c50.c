
undefined8 FUN_ffff0000be003c50(undefined4 param_1,ushort *param_2)

{
  undefined8 uVar1;
  byte local_4 [4];
  
  local_4[0] = 0;
  uVar1 = FUN_ffff0000be0302b4(DAT_ffff0000be0f9020,param_1,local_4,1);
  if (-1 < (int)uVar1) {
    uVar1 = 0;
    *param_2 = (ushort)local_4[0];
  }
  return uVar1;
}


