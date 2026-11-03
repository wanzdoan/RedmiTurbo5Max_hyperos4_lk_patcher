// ffff000090730f28  size=268  FUN_ffff000090730f28

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff000090730f28(long param_1)

{
  uint *puVar1;
  uint uVar2;
  long lVar3;
  int iVar4;
  uint uVar5;
  ulong uVar6;
  ulong uVar7;
  uint *puVar8;
  long lVar9;
  undefined8 uVar10;
  
  uVar5 = _DAT_ffff00009086d9bc;
  iVar4 = _DAT_ffff00009086d9b4;
  lVar3 = _DAT_ffff00009086d998;
  uVar7 = (ulong)_DAT_ffff00009086d9bc;
  if (_DAT_ffff00009086d9bc == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_No_need_to_generate_ffff0000907f4692,
               s_ccci_update_base_to_mem_tbl_ffff0000907f6206,0x22a);
  }
  else if ((int)_DAT_ffff00009086d9bc < 1) {
    uVar6 = 0;
LAB_ffff000090730fe0:
    if ((uint)uVar6 == uVar5) {
      return uVar7;
    }
    FUN_ffff00009072ff90
              (s__CCCI___s__d__MD_mem_tbl_num_mis_ffff0000907d786c,
               s_ccci_update_base_to_mem_tbl_ffff0000907f6206,0x240);
  }
  else {
    uVar6 = 0;
    lVar9 = _DAT_ffff00009086d9a0 + 8;
    puVar8 = (uint *)&DAT_ffff00009086d9b0;
    while( true ) {
      uVar2 = *puVar8;
      if (iVar4 <= (int)uVar2) break;
      if (uVar2 == 0xffffffff) goto LAB_ffff000090730fe0;
      puVar1 = (uint *)(lVar3 + (long)(int)uVar2 * 0x20);
      uVar6 = uVar6 + 1;
      puVar8 = puVar1 + 6;
      uVar2 = *puVar1;
      *(uint *)(lVar9 + -8) = uVar2;
      uVar10 = *(undefined8 *)(puVar1 + 1);
      *(ulong *)(lVar9 + 8) = (ulong)uVar2 + param_1;
      *(undefined8 *)(lVar9 + -4) = uVar10;
      *(uint *)(lVar9 + 4) = puVar1[3];
      lVar9 = lVar9 + 0x18;
      if (uVar7 == uVar6) {
        return uVar7;
      }
    }
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Invalid_curr_id__d_ffff0000907f29d1,
               s_ccci_update_base_to_mem_tbl_ffff0000907f6206,0x231,uVar2);
  }
  return 0xffffffff;
}


