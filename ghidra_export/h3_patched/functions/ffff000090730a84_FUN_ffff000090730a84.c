// ffff000090730a84  size=328  FUN_ffff000090730a84

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8
FUN_ffff000090730a84(ulong param_1,undefined4 param_2,int param_3,uint param_4,uint param_5)

{
  long lVar1;
  undefined4 *puVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  long lVar6;
  uint uVar7;
  ulong uVar8;
  
  uVar7 = _DAT_ffff00009086d9ac;
  if (_DAT_ffff00009086d998 == 0) {
    FUN_ffff00009072ff90
              (s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907ef88d,
               s_get_free_md_mem_blk_node_ffff0000907e7882,0xba);
  }
  else {
    uVar8 = (ulong)_DAT_ffff00009086d9ac;
    if ((int)_DAT_ffff00009086d9ac < 0) {
      FUN_ffff00009072ff90
                (s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b3b,
                 s_get_free_md_mem_blk_node_ffff0000907e7882,0xbe);
    }
    else {
      if ((int)_DAT_ffff00009086d9ac < _DAT_ffff00009086d9b4) {
        lVar6 = (param_1 & 0xffffffff) * 0x20;
        lVar1 = _DAT_ffff00009086d998 + uVar8 * 0x20;
        _DAT_ffff00009086d9ac = *(undefined4 *)(lVar1 + 0x18);
        FUN_ffff00009077e858(lVar1,_DAT_ffff00009086d998 + lVar6,0x20);
        lVar6 = _DAT_ffff00009086d998 + lVar6;
        puVar2 = (undefined4 *)(_DAT_ffff00009086d998 + uVar8 * 0x20);
        uVar3 = puVar2[2];
        uVar4 = puVar2[3];
        *puVar2 = param_2;
        puVar2[1] = param_3;
        iVar5 = *(int *)(lVar6 + 4);
        puVar2[6] = *(undefined4 *)(lVar6 + 0x18);
        *(uint *)(lVar6 + 0x18) = uVar7;
        puVar2[2] = uVar3 | param_4;
        puVar2[3] = uVar4 | param_5;
        *(int *)(lVar6 + 4) = iVar5 - param_3;
        return 1;
      }
      FUN_ffff00009072ff90
                (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2a16,
                 s_get_free_md_mem_blk_node_ffff0000907e7882,0xc3,_DAT_ffff00009086d9ac,
                 _DAT_ffff00009086d9b4 + -1);
    }
  }
  FUN_ffff00009072ff90
            (s__CCCI___s__d__Get_free_node_fail_ffff0000907cf6ab,s_insert_case_2_ffff0000907e789b,
             0x125);
  return 0xffffffff;
}


