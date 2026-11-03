// ffff00009072b1e0  size=404  FUN_ffff00009072b1e0

void FUN_ffff00009072b1e0(void)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  ulong local_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined4 local_28;
  
  lVar3 = FUN_ffff00009071935c();
  if ((lVar3 != 0) && (*(int *)(lVar3 + 0x80) == 0x76657231)) {
    DAT_ffff00009086d6d0 = 1;
  }
  lVar3 = FUN_ffff00009071935c();
  if (lVar3 != 0) {
    if (*(int *)(lVar3 + 0x74) == 0x454e454e) {
      lVar3 = FUN_ffff00009071935c();
      if (lVar3 != 0) {
        *(uint *)(lVar3 + 0x78) = *(uint *)(lVar3 + 0x78) & 0xfffffff0 | 2;
        uVar1 = FUN_ffff0000907366e8();
        FUN_ffff000090736740(uVar1 & 0xfffffff0 | 2);
      }
      if (DAT_ffff000090818f10 == 0xdeaddead) {
        uVar1 = FUN_ffff0000907366e8();
        if (0xf < (uVar1 & 0xff)) {
          DAT_ffff000090818f10 = uVar1 >> 4 & 0xf;
        }
      }
    }
    else {
      *(uint *)(lVar3 + 0x78) = *(uint *)(lVar3 + 0x78) & 0xfffffff0 | 2;
    }
    local_28 = 0;
    local_38 = 0;
    uStack_30 = 0;
    local_48 = 0;
    local_40 = 0;
    local_58 = 0;
    uStack_50 = 0;
    FUN_ffff00009077ee50(&local_58,0,0x34);
    iVar2 = FUN_ffff00009072ae44(&local_58);
    if (-1 < iVar2) {
      if (DAT_ffff000090818f10 == 0xdeaddead) {
        DAT_ffff000090818f10 = (uint)local_40 >> 4 & 0xf;
      }
      local_40 = local_40 & 0xfffffffffffffff0 | 2;
      lVar3 = FUN_ffff00009077c028(s_expdb_ffff0000907d5780);
      if (lVar3 == 0) {
        FUN_ffff00009077e11c
                  (s__s__Partition___s__is_not_exist__ffff0000907fd165,
                   s_LOG_STORE_LK_ffff0000907d75aa,s_expdb_ffff0000907d5780);
      }
      else {
        FUN_ffff00009077c144(lVar3,&local_58,*(long *)(lVar3 + 0x20) - *(long *)(lVar3 + 0x28),0x34)
        ;
        FUN_ffff00009077bfac(lVar3);
      }
    }
  }
  FUN_ffff000090722730(s_logstore_shutdown_callback_ffff0000907d5ec7,FUN_ffff00009072b394,0);
  FUN_ffff00009072adb4();
  return;
}


