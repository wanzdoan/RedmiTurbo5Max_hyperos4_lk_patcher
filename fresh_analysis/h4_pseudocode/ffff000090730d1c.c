
undefined8
FUN_ffff000090730d1c(ulong param_1,undefined4 param_2,int param_3,uint param_4,uint param_5)

{
  long lVar1;
  undefined4 *puVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  long lVar6;
  uint uVar7;
  ulong uVar8;
  
  uVar7 = DAT_ffff00009086da44;
  if (DAT_ffff00009086da30 == 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907efa15,
               s_get_free_md_mem_blk_node_ffff0000907e79dd,0xba);
  }
  else {
    uVar8 = (ulong)DAT_ffff00009086da44;
    if ((int)DAT_ffff00009086da44 < 0) {
      FUN_ffff000090730228
                (s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b78,
                 s_get_free_md_mem_blk_node_ffff0000907e79dd,0xbe);
    }
    else {
      if ((int)DAT_ffff00009086da44 < DAT_ffff00009086da4c) {
        lVar6 = (param_1 & 0xffffffff) * 0x20;
        lVar1 = DAT_ffff00009086da30 + uVar8 * 0x20;
        DAT_ffff00009086da44 = *(undefined4 *)(lVar1 + 0x18);
        FUN_ffff00009077eb20(lVar1,DAT_ffff00009086da30 + lVar6,0x20);
        lVar6 = DAT_ffff00009086da30 + lVar6;
        puVar2 = (undefined4 *)(DAT_ffff00009086da30 + uVar8 * 0x20);
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
      FUN_ffff000090730228
                (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2bb9,
                 s_get_free_md_mem_blk_node_ffff0000907e79dd,0xc3,DAT_ffff00009086da44,
                 DAT_ffff00009086da4c + -1);
    }
  }
  FUN_ffff000090730228
            (s__CCCI___s__d__Get_free_node_fail_ffff0000907cf6ca,s_insert_case_2_ffff0000907e79f6,
             0x125);
  return 0xffffffff;
}


