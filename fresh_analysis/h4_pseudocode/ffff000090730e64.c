
undefined8 FUN_ffff000090730e64(ulong param_1,int param_2,int param_3,uint param_4,uint param_5)

{
  int *piVar1;
  int *piVar2;
  int *piVar3;
  uint uVar4;
  int iVar5;
  int iVar6;
  long lVar7;
  uint uVar8;
  char *pcVar9;
  undefined8 uVar10;
  long lVar11;
  long lVar12;
  
  if (DAT_ffff00009086da30 == 0) {
    pcVar9 = s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907efa15;
    uVar10 = 0xba;
LAB_ffff000090730f18:
    FUN_ffff000090730228(pcVar9,s_get_free_md_mem_blk_node_ffff0000907e79dd,uVar10);
    uVar8 = DAT_ffff00009086da44;
    if (DAT_ffff00009086da30 == 0) goto LAB_ffff000090731050;
joined_r0xffff000090730ed0:
    DAT_ffff00009086da44 = uVar8;
    if ((int)uVar8 < 0) {
      pcVar9 = s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b78;
      uVar10 = 0xbe;
      goto LAB_ffff000090731064;
    }
    if ((int)uVar8 < DAT_ffff00009086da4c) {
      uVar4 = *(uint *)(DAT_ffff00009086da30 + (ulong)uVar8 * 0x20 + 0x18);
      DAT_ffff00009086da44 = uVar4;
      if ((int)uVar4 < 0) {
        FUN_ffff000090730228
                  (s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b78,
                   s_get_free_md_mem_blk_node_ffff0000907e79dd,0xbe);
      }
      else {
        if ((int)uVar4 < DAT_ffff00009086da4c) {
          lVar12 = (ulong)uVar4 * 0x20;
          lVar11 = (ulong)uVar8 * 0x20;
          lVar7 = (param_1 & 0xffffffff) * 0x20;
          DAT_ffff00009086da44 = *(undefined4 *)(DAT_ffff00009086da30 + lVar12 + 0x18);
          FUN_ffff00009077eb20(DAT_ffff00009086da30 + lVar11,DAT_ffff00009086da30 + lVar7,0x20);
          FUN_ffff00009077eb20(DAT_ffff00009086da30 + lVar12,DAT_ffff00009086da30 + lVar7,0x20);
          piVar1 = (int *)(DAT_ffff00009086da30 + lVar7);
          piVar2 = (int *)(DAT_ffff00009086da30 + lVar12);
          piVar3 = (int *)(DAT_ffff00009086da30 + lVar11);
          iVar5 = *piVar1;
          piVar2[6] = piVar1[6];
          piVar1[6] = uVar8;
          piVar1[1] = param_2 - iVar5;
          *piVar3 = param_2;
          piVar3[1] = param_3;
          iVar5 = piVar1[1];
          piVar3[6] = uVar4;
          iVar6 = piVar2[1];
          piVar3[2] = piVar3[2] | param_4;
          piVar3[3] = piVar3[3] | (param_5 & 4) << 1 | param_5;
          *piVar2 = param_3 + param_2;
          piVar2[1] = iVar6 - (iVar5 + param_3);
          return 2;
        }
        FUN_ffff000090730228
                  (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2bb9,
                   s_get_free_md_mem_blk_node_ffff0000907e79dd,0xc3,uVar4,DAT_ffff00009086da4c + -1)
        ;
      }
      pcVar9 = s__CCCI___s__d__Get_free_node_fail_ffff000090800af0;
      uVar10 = 0xfd;
      goto LAB_ffff00009073107c;
    }
    FUN_ffff000090730228
              (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2bb9,
               s_get_free_md_mem_blk_node_ffff0000907e79dd,0xc3,uVar8,DAT_ffff00009086da4c + -1);
  }
  else {
    if ((int)DAT_ffff00009086da44 < 0) {
      pcVar9 = s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b78;
      uVar10 = 0xbe;
      goto LAB_ffff000090730f18;
    }
    if ((int)DAT_ffff00009086da44 < DAT_ffff00009086da4c) {
      uVar8 = *(uint *)(DAT_ffff00009086da30 + (ulong)DAT_ffff00009086da44 * 0x20 + 0x18);
      goto joined_r0xffff000090730ed0;
    }
    FUN_ffff000090730228
              (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2bb9,
               s_get_free_md_mem_blk_node_ffff0000907e79dd,0xc3,(ulong)DAT_ffff00009086da44,
               DAT_ffff00009086da4c + -1);
    uVar8 = DAT_ffff00009086da44;
    if (DAT_ffff00009086da30 != 0) goto joined_r0xffff000090730ed0;
LAB_ffff000090731050:
    pcVar9 = s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907efa15;
    uVar10 = 0xba;
LAB_ffff000090731064:
    FUN_ffff000090730228(pcVar9,s_get_free_md_mem_blk_node_ffff0000907e79dd,uVar10);
  }
  pcVar9 = s__CCCI___s__d__Get_free_node_fail_ffff0000907f2be5;
  uVar10 = 0xf8;
LAB_ffff00009073107c:
  FUN_ffff000090730228(pcVar9,s_insert_case_1_ffff0000907ee1b6,uVar10);
  return 0xffffffff;
}


