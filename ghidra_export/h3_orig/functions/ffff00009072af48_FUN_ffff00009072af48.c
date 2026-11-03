// ffff00009072af48  size=404  FUN_ffff00009072af48

void FUN_ffff00009072af48(void)

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
  
  lVar3 = FUN_ffff000090719218();
  if ((lVar3 != 0) && (*(int *)(lVar3 + 0x80) == 0x76657231)) {
    DAT_ffff00009086d638 = 1;
  }
  lVar3 = FUN_ffff000090719218();
  if (lVar3 != 0) {
    if (*(int *)(lVar3 + 0x74) == 0x454e454e) {
      lVar3 = FUN_ffff000090719218();
      if (lVar3 != 0) {
        *(uint *)(lVar3 + 0x78) = *(uint *)(lVar3 + 0x78) & 0xfffffff0 | 2;
        uVar1 = FUN_ffff000090736450();
        FUN_ffff0000907364a8(uVar1 & 0xfffffff0 | 2);
      }
      if (DAT_ffff000090818ef0 == 0xdeaddead) {
        uVar1 = FUN_ffff000090736450();
        if (0xf < (uVar1 & 0xff)) {
          DAT_ffff000090818ef0 = uVar1 >> 4 & 0xf;
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
    FUN_ffff00009077eb88(&local_58,0,0x34);
    iVar2 = FUN_ffff00009072ac00(&local_58);
    if (-1 < iVar2) {
      if (DAT_ffff000090818ef0 == 0xdeaddead) {
        DAT_ffff000090818ef0 = (uint)local_40 >> 4 & 0xf;
      }
      local_40 = local_40 & 0xfffffffffffffff0 | 2;
      lVar3 = FUN_ffff00009077bd60(s_expdb_ffff0000907d5730);
      if (lVar3 == 0) {
        FUN_ffff00009077de54
                  (s__s__Partition___s__is_not_exist__ffff0000907fcfbd,
                   s_LOG_STORE_LK_ffff0000907d7514,s_expdb_ffff0000907d5730);
      }
      else {
        FUN_ffff00009077be7c(lVar3,&local_58,*(long *)(lVar3 + 0x20) - *(long *)(lVar3 + 0x28),0x34)
        ;
        FUN_ffff00009077bce4(lVar3);
      }
    }
  }
  FUN_ffff0000907224ec(s_logstore_shutdown_callback_ffff0000907d5e31,FUN_ffff00009072b0fc,0);
  FUN_ffff00009072ab70();
  return;
}


