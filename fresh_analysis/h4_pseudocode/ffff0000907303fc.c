
undefined8 FUN_ffff0000907303fc(uint param_1)

{
  long lVar1;
  ulong uVar2;
  int iVar3;
  ulong uVar4;
  undefined4 *puVar5;
  ulong uVar6;
  
  uVar6 = (ulong)param_1;
  if (param_1 - 1 == 0 || (int)param_1 < 1) {
    FUN_ffff000090730228
              (s__CCCI___s__d__Num_is_too_less__d_ffff0000907e48d2,
               s_alloc_mem_blk_list_ffff0000907d7955,0x144,uVar6);
    return 0xffffffff;
  }
  DAT_ffff00009086da30 = FUN_ffff00009077d514(uVar6 << 5);
  if (DAT_ffff00009086da30 == 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d__Alloc_fail_ffff0000907ee173,
               s_alloc_md_mem_blk_list_mem_ffff0000907ee18e,0xa5);
    return 0xffffffff;
  }
  DAT_ffff00009086da4c = param_1;
  FUN_ffff00009077ee50(DAT_ffff00009086da30,0,uVar6 << 5);
  lVar1 = DAT_ffff00009086da30;
  if (param_1 < 2) {
    uVar2 = 0;
  }
  else {
    uVar4 = 0;
    uVar2 = uVar6 & 0xfffffffe;
    puVar5 = (undefined4 *)(DAT_ffff00009086da30 + 0x38);
    do {
      iVar3 = (int)uVar4;
      uVar4 = uVar4 + 2;
      puVar5[-8] = iVar3 + 1;
      *puVar5 = (int)uVar4;
      puVar5 = puVar5 + 0x10;
    } while (uVar2 != uVar4);
    if (uVar2 == uVar6) goto LAB_ffff0000907304e4;
  }
  puVar5 = (undefined4 *)(lVar1 + uVar2 * 0x20 + 0x18);
  do {
    uVar2 = uVar2 + 1;
    *puVar5 = (int)uVar2;
    puVar5 = puVar5 + 8;
  } while (uVar6 != uVar2);
LAB_ffff0000907304e4:
  *(undefined4 *)(lVar1 + (ulong)(param_1 - 1) * 0x20 + 0x18) = 0xffffffff;
  DAT_ffff00009086da44 = 0;
  lVar1 = FUN_ffff00009077d514((long)(int)param_1 * 0x18);
  if (lVar1 == 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d__Alloc_memory_fail_ffff0000907f2b97,
               s_alloc_mem_blk_table_ffff0000907e92d7,0x151);
    thunk_FUN_ffff0000907c314c(DAT_ffff00009086da30);
    return 0xffffffff;
  }
  FUN_ffff00009077ee50(lVar1,0,(long)(int)param_1 * 0x18);
  DAT_ffff00009086da38 = lVar1;
  DAT_ffff00009086da40 = 1;
  return 0;
}


