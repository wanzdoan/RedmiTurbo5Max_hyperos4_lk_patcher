// ffff000090730bcc  size=664  FUN_ffff000090730bcc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090730bcc(ulong param_1,int param_2,int param_3,uint param_4,uint param_5)

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
  
  if (_DAT_ffff00009086d998 == 0) {
    pcVar9 = s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907ef88d;
    uVar10 = 0xba;
LAB_ffff000090730c80:
    FUN_ffff00009072ff90(pcVar9,s_get_free_md_mem_blk_node_ffff0000907e7882,uVar10);
    uVar8 = _DAT_ffff00009086d9ac;
    if (_DAT_ffff00009086d998 == 0) goto LAB_ffff000090730db8;
joined_r0xffff000090730c38:
    _DAT_ffff00009086d9ac = uVar8;
    if ((int)uVar8 < 0) {
      pcVar9 = s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b3b;
      uVar10 = 0xbe;
      goto LAB_ffff000090730dcc;
    }
    if ((int)uVar8 < _DAT_ffff00009086d9b4) {
      uVar4 = *(uint *)(_DAT_ffff00009086d998 + (ulong)uVar8 * 0x20 + 0x18);
      _DAT_ffff00009086d9ac = uVar4;
      if ((int)uVar4 < 0) {
        FUN_ffff00009072ff90
                  (s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b3b,
                   s_get_free_md_mem_blk_node_ffff0000907e7882,0xbe);
      }
      else {
        if ((int)uVar4 < _DAT_ffff00009086d9b4) {
          lVar12 = (ulong)uVar4 * 0x20;
          lVar11 = (ulong)uVar8 * 0x20;
          lVar7 = (param_1 & 0xffffffff) * 0x20;
          _DAT_ffff00009086d9ac = *(undefined4 *)(_DAT_ffff00009086d998 + lVar12 + 0x18);
          FUN_ffff00009077e858(_DAT_ffff00009086d998 + lVar11,_DAT_ffff00009086d998 + lVar7,0x20);
          FUN_ffff00009077e858(_DAT_ffff00009086d998 + lVar12,_DAT_ffff00009086d998 + lVar7,0x20);
          piVar1 = (int *)(_DAT_ffff00009086d998 + lVar7);
          piVar2 = (int *)(_DAT_ffff00009086d998 + lVar12);
          piVar3 = (int *)(_DAT_ffff00009086d998 + lVar11);
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
        FUN_ffff00009072ff90
                  (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2a16,
                   s_get_free_md_mem_blk_node_ffff0000907e7882,0xc3,uVar4,_DAT_ffff00009086d9b4 + -1
                  );
      }
      pcVar9 = s__CCCI___s__d__Get_free_node_fail_ffff000090800914;
      uVar10 = 0xfd;
      goto LAB_ffff000090730de4;
    }
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2a16,
               s_get_free_md_mem_blk_node_ffff0000907e7882,0xc3,uVar8,_DAT_ffff00009086d9b4 + -1);
  }
  else {
    if ((int)_DAT_ffff00009086d9ac < 0) {
      pcVar9 = s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b3b;
      uVar10 = 0xbe;
      goto LAB_ffff000090730c80;
    }
    if ((int)_DAT_ffff00009086d9ac < _DAT_ffff00009086d9b4) {
      uVar8 = *(uint *)(_DAT_ffff00009086d998 + (ulong)_DAT_ffff00009086d9ac * 0x20 + 0x18);
      goto joined_r0xffff000090730c38;
    }
    FUN_ffff00009072ff90
              (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2a16,
               s_get_free_md_mem_blk_node_ffff0000907e7882,0xc3,(ulong)_DAT_ffff00009086d9ac,
               _DAT_ffff00009086d9b4 + -1);
    uVar8 = _DAT_ffff00009086d9ac;
    if (_DAT_ffff00009086d998 != 0) goto joined_r0xffff000090730c38;
LAB_ffff000090730db8:
    pcVar9 = s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907ef88d;
    uVar10 = 0xba;
LAB_ffff000090730dcc:
    FUN_ffff00009072ff90(pcVar9,s_get_free_md_mem_blk_node_ffff0000907e7882,uVar10);
  }
  pcVar9 = s__CCCI___s__d__Get_free_node_fail_ffff0000907f2a42;
  uVar10 = 0xf8;
LAB_ffff000090730de4:
  FUN_ffff00009072ff90(pcVar9,s_insert_case_1_ffff0000907ee02e,uVar10);
  return 0xffffffff;
}


