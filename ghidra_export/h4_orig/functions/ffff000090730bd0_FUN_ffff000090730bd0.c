// ffff000090730bd0  size=332  FUN_ffff000090730bd0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090730bd0(ulong param_1,int param_2,int param_3,uint param_4,uint param_5)

{
  long lVar1;
  int *piVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  long lVar6;
  uint uVar7;
  ulong uVar8;
  
  uVar7 = _DAT_ffff00009086da44;
  if (_DAT_ffff00009086da30 == 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907efa15,
               s_get_free_md_mem_blk_node_ffff0000907e79dd,0xba);
  }
  else {
    uVar8 = (ulong)_DAT_ffff00009086da44;
    if ((int)_DAT_ffff00009086da44 < 0) {
      FUN_ffff000090730228
                (s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b78,
                 s_get_free_md_mem_blk_node_ffff0000907e79dd,0xbe);
    }
    else {
      if ((int)_DAT_ffff00009086da44 < _DAT_ffff00009086da4c) {
        lVar6 = (param_1 & 0xffffffff) * 0x20;
        lVar1 = _DAT_ffff00009086da30 + uVar8 * 0x20;
        _DAT_ffff00009086da44 = *(undefined4 *)(lVar1 + 0x18);
        FUN_ffff00009077eb20(lVar1,_DAT_ffff00009086da30 + lVar6,0x20);
        lVar6 = _DAT_ffff00009086da30 + lVar6;
        piVar2 = (int *)(_DAT_ffff00009086da30 + uVar8 * 0x20);
        uVar3 = *(uint *)(lVar6 + 8);
        uVar4 = *(uint *)(lVar6 + 0xc);
        piVar2[6] = *(int *)(lVar6 + 0x18);
        *(uint *)(lVar6 + 0x18) = uVar7;
        *(int *)(lVar6 + 4) = param_3;
        *(uint *)(lVar6 + 8) = uVar3 | param_4;
        iVar5 = piVar2[1];
        *(uint *)(lVar6 + 0xc) = uVar4 | param_5;
        *piVar2 = param_3 + param_2;
        piVar2[1] = iVar5 - param_3;
        return 1;
      }
      FUN_ffff000090730228
                (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2bb9,
                 s_get_free_md_mem_blk_node_ffff0000907e79dd,0xc3,_DAT_ffff00009086da44,
                 _DAT_ffff00009086da4c + -1);
    }
  }
  FUN_ffff000090730228
            (s__CCCI___s__d__Get_free_node_fail_ffff0000907cf6ca,s_insert_case_0_ffff0000907ee1a8,
             0xd9);
  return 0xffffffff;
}


