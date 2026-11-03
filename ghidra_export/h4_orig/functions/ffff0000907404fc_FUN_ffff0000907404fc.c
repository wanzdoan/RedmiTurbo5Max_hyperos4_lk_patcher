// ffff0000907404fc  size=112  FUN_ffff0000907404fc

void FUN_ffff0000907404fc
               (undefined1 *param_1,undefined4 param_2,byte *param_3,byte param_4,uint param_5)

{
  int iVar1;
  byte local_14 [4];
  
  local_14[0] = 0;
  iVar1 = (**(code **)(*(long *)(param_1 + 0x20) + 0x20))
                    (*(long *)(param_1 + 0x20),0x38,*param_1,param_2,local_14,1);
  if (iVar1 == 0) {
    *param_3 = local_14[0] >> (ulong)(param_5 & 0x1f) & param_4;
  }
  return;
}


