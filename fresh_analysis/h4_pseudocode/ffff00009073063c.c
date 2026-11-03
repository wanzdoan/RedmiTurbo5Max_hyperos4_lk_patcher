
undefined8 FUN_ffff00009073063c(undefined4 param_1)

{
  undefined4 *puVar1;
  uint uVar2;
  char *pcVar3;
  char *pcVar4;
  undefined8 uVar5;
  
  uVar2 = DAT_ffff00009086da44;
  if ((DAT_ffff00009086da40 & 1) == 0) {
    pcVar3 = s__CCCI___s__d__MD_memory_layout_o_ffff0000907f2b46;
    pcVar4 = s_ccci_create_1st_mem_node_ffff0000907d6129;
    uVar5 = 0x191;
  }
  else if (DAT_ffff00009086da30 == 0) {
    pcVar3 = s__CCCI___s__d__MD_mem_blk_node_NU_ffff0000907efa15;
    pcVar4 = s_get_free_md_mem_blk_node_ffff0000907e79dd;
    uVar5 = 0xba;
  }
  else {
    if (-1 < (int)DAT_ffff00009086da44) {
      if (DAT_ffff00009086da4c <= (int)DAT_ffff00009086da44) {
        FUN_ffff000090730228
                  (s__CCCI___s__d__Invalid_free_idx___ffff0000907f2bb9,
                   s_get_free_md_mem_blk_node_ffff0000907e79dd,0xc3,(ulong)DAT_ffff00009086da44,
                   DAT_ffff00009086da4c + -1);
        return 0xffffffff;
      }
      puVar1 = (undefined4 *)(DAT_ffff00009086da30 + (ulong)DAT_ffff00009086da44 * 0x20);
      DAT_ffff00009086da44 = puVar1[6];
      *(undefined8 *)(puVar1 + 2) = 0;
      *puVar1 = 0;
      puVar1[1] = param_1;
      puVar1[6] = 0xffffffff;
      DAT_ffff00009086da48 = uVar2;
      DAT_ffff00009086da50 = 1;
      DAT_ffff00009086da54 = 1;
      return 0;
    }
    pcVar3 = s__CCCI___s__d__MD_mem_blk_node_em_ffff0000907d2b78;
    pcVar4 = s_get_free_md_mem_blk_node_ffff0000907e79dd;
    uVar5 = 0xbe;
  }
  FUN_ffff000090730228(pcVar3,pcVar4,uVar5);
  return 0xffffffff;
}


