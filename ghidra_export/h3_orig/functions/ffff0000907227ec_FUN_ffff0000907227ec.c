// ffff0000907227ec  size=32  FUN_ffff0000907227ec

void FUN_ffff0000907227ec(uint param_1)

{
  int iVar1;
  long local_40 [4];
  
  if (6 < param_1) {
    FUN_ffff00009077de54
              (s__s_invaild_parameter__ffff0000907fcdf0,s_devapc_protection_enable_ffff0000907e5e60)
    ;
    return;
  }
  iVar1 = FUN_ffff00009071c55c();
  if (iVar1 == 2) {
    local_40[2] = 0;
    local_40[3] = 0;
    local_40[0] = 0;
    local_40[1] = 0;
    FUN_ffff00009071ea6c(0x82000101,param_1,0,0,0,0,0,0,local_40);
    if (local_40[0] != 0) {
      FUN_ffff00009077de54
                (s__s_failed__ffff0000907f7d8f,s_devapc_protection_enable_ffff0000907e5e60);
    }
  }
  return;
}


