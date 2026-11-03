// ffff000090719320  size=308  FUN_ffff000090719320

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090719320(void)

{
  long lVar1;
  int iVar2;
  char *pcVar3;
  ulong uVar4;
  ulong uVar5;
  uint uVar6;
  undefined8 local_38;
  
  local_38 = 0;
  if (_DAT_ffff000090843c28 == 0) {
    return;
  }
  uVar5 = *(ulong *)(_DAT_ffff000090843c28 + 0x14);
  if ((uVar5 == 0) || (*(int *)(_DAT_ffff000090843c28 + 0x1c) == 0)) {
    DAT_ffff000090843c30 = 1;
    uVar6 = 0x40000;
    uVar4 = FUN_ffff000090719798
                      (0x40000,0x10000,0xffffffffffffffff,0,1,s_log_store_new_ffff0000907ea6bd);
    lVar1 = _DAT_ffff000090843c28;
    if (uVar4 == 0) {
      pcVar3 = s__s__mblock_alloc_error_for_log_s_ffff0000907df520;
      goto LAB_ffff00009071942c;
    }
    *(ulong *)(_DAT_ffff000090843c28 + 0x14) = uVar4;
    *(undefined4 *)(lVar1 + 0x1c) = 0x40000;
    *(undefined4 *)(lVar1 + 0x10) = 0;
  }
  else {
    uVar4 = uVar5 & 0xffffffffffff0000;
    uVar6 = *(int *)(_DAT_ffff000090843c28 + 0x1c) + ((uint)uVar5 & 0xffff) + 0xffff & 0xffff0000;
  }
  DAT_ffff000090843c30 = 1;
  iVar2 = FUN_ffff00009077b79c
                    (DAT_ffff000090839908,s_log_store_ffff0000907d5a4f,uVar6,&local_38,0x10,uVar4,0,
                     1);
  DAT_ffff000090843c30 = 0;
  if (iVar2 == 0) {
    _DAT_ffff000090843c38 = local_38;
    *(uint *)(_DAT_ffff000090843c28 + 0x10) = *(uint *)(_DAT_ffff000090843c28 + 0x10) | 0x200;
    return;
  }
  pcVar3 = s__s__map_error_for_log_store_ffff0000907ea6cb;
LAB_ffff00009071942c:
  DAT_ffff000090843c30 = 1;
  FUN_ffff00009077de54(pcVar3,s_LOG_STORE_LK_ffff0000907d7514);
  DAT_ffff000090843c30 = 0;
  return;
}


