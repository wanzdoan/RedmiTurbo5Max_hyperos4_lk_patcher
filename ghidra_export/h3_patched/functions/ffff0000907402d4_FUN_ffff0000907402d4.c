// ffff0000907402d4  size=164  FUN_ffff0000907402d4

void FUN_ffff0000907402d4
               (undefined1 *param_1,undefined4 param_2,int param_3,int param_4,uint param_5)

{
  int iVar1;
  byte local_24 [4];
  
  local_24[0] = 0;
  iVar1 = (**(code **)(*(long *)(param_1 + 0x20) + 0x20))
                    (*(long *)(param_1 + 0x20),0x38,*param_1,param_2,local_24,1);
  if (iVar1 == 0) {
    local_24[0] = local_24[0] & ((byte)(param_4 << (ulong)(param_5 & 0x1f)) ^ 0xff) |
                  (byte)(param_3 << (ulong)(param_5 & 0x1f));
    (**(code **)(*(long *)(param_1 + 0x20) + 0x28))
              (*(long *)(param_1 + 0x20),0x30,*param_1,param_2,local_24,1);
  }
  return;
}


