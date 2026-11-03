// ffff000090722a50  size=140  FUN_ffff000090722a50

void FUN_ffff000090722a50(undefined4 param_1)

{
  int iVar1;
  long local_40 [4];
  
  iVar1 = FUN_ffff00009071c6e8();
  if (iVar1 == 2) {
    local_40[2] = 0;
    local_40[3] = 0;
    local_40[0] = 0;
    local_40[1] = 0;
    FUN_ffff00009071ebf4(0x82000101,param_1,0,0,0,0,0,0,local_40);
    if (local_40[0] != 0) {
      FUN_ffff00009077e11c
                (s__s_failed__ffff0000907f7f2b,s_devapc_protection_enable_ffff0000907e5f74);
    }
  }
  return;
}


