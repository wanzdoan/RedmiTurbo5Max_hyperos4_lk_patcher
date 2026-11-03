// ffff000090730164  size=376  FUN_ffff000090730164

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090730164(uint param_1)

{
  long lVar1;
  ulong uVar2;
  int iVar3;
  ulong uVar4;
  undefined4 *puVar5;
  ulong uVar6;
  
  uVar6 = (ulong)param_1;
  if (param_1 - 1 == 0 || (int)param_1 < 1) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Num_is_too_less__d_ffff0000907e47b7,
               s_alloc_mem_blk_list_ffff0000907d78bf,0x144,uVar6);
    return 0xffffffff;
  }
  _DAT_ffff00009086d998 = FUN_ffff00009077d24c(uVar6 << 5);
  if (_DAT_ffff00009086d998 == 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Alloc_fail_ffff0000907edfeb,
               s_alloc_md_mem_blk_list_mem_ffff0000907ee006,0xa5);
    return 0xffffffff;
  }
  _DAT_ffff00009086d9b4 = param_1;
  FUN_ffff00009077eb88(_DAT_ffff00009086d998,0,uVar6 << 5);
  lVar1 = _DAT_ffff00009086d998;
  if (param_1 < 2) {
    uVar2 = 0;
  }
  else {
    uVar4 = 0;
    uVar2 = uVar6 & 0xfffffffe;
    puVar5 = (undefined4 *)(_DAT_ffff00009086d998 + 0x38);
    do {
      iVar3 = (int)uVar4;
      uVar4 = uVar4 + 2;
      puVar5[-8] = iVar3 + 1;
      *puVar5 = (int)uVar4;
      puVar5 = puVar5 + 0x10;
    } while (uVar2 != uVar4);
    if (uVar2 == uVar6) goto LAB_ffff00009073024c;
  }
  puVar5 = (undefined4 *)(lVar1 + uVar2 * 0x20 + 0x18);
  do {
    uVar2 = uVar2 + 1;
    *puVar5 = (int)uVar2;
    puVar5 = puVar5 + 8;
  } while (uVar6 != uVar2);
LAB_ffff00009073024c:
  *(undefined4 *)(lVar1 + (ulong)(param_1 - 1) * 0x20 + 0x18) = 0xffffffff;
  _DAT_ffff00009086d9ac = 0;
  lVar1 = FUN_ffff00009077d24c((long)(int)param_1 * 0x18);
  if (lVar1 == 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Alloc_memory_fail_ffff0000907f29f4,
               s_alloc_mem_blk_table_ffff0000907e917c,0x151);
    thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009086d998);
    return 0xffffffff;
  }
  FUN_ffff00009077eb88(lVar1,0,(long)(int)param_1 * 0x18);
  _DAT_ffff00009086d9a0 = lVar1;
  DAT_ffff00009086d9a8 = 1;
  return 0;
}


